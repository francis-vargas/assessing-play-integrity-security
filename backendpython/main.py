from flask import Flask, request, jsonify
from google.oauth2 import service_account
from google.auth.transport.requests import AuthorizedSession
import hashlib
import base64
import time
import json
import os
import threading

app = Flask(__name__)

# Nome do pacote
PACKAGE_NAME = "com.exemplo.playintegritydemo"

# Arquivo de credenciais da conta de serviço
SERVICE_ACCOUNT_FILE = "engenharia-reversa-461115-6c26741e54cf.json"
SCOPES = ["https://www.googleapis.com/auth/playintegrity"]

# Sessão autenticada
credentials = service_account.Credentials.from_service_account_file(
    SERVICE_ACCOUNT_FILE, scopes=SCOPES
)
authed_session = AuthorizedSession(credentials)

# Armazenamento temporário de nonces para evitar replay
NONCE_STORE = {}
NONCE_EXPIRATION_SECONDS = 120  # 2 minutos

# Limpeza automática de nonces expirados
def limpar_nonces_expirados():
    while True:
        agora = time.time()
        expirados = [k for k, v in NONCE_STORE.items() if agora > v]
        for k in expirados:
            del NONCE_STORE[k]
        time.sleep(30)

threading.Thread(target=limpar_nonces_expirados, daemon=True).start()

@app.route("/verify", methods=["POST"])
def verify():
    try:
        body = request.json
        integrity_token = body.get("integrity_token")
        tipo_requisicao = body.get("tipo_requisicao")
        valor_unico = body.get("valor_unico")

        if not integrity_token:
            return jsonify({"error": "Token ausente"}), 400

        url = f"https://playintegrity.googleapis.com/v1/{PACKAGE_NAME}:decodeIntegrityToken"
        response = authed_session.post(url, json={"integrityToken": integrity_token})
        response.raise_for_status()
        decoded = response.json()

        print("✅ Resposta da API Google:", decoded)

        # Validação da requisição combinada
        if tipo_requisicao == "combinado":
            if not valor_unico:
                return jsonify({"error": "valor_unico ausente"}), 400

            # Verifica replay
            if valor_unico in NONCE_STORE:
                return jsonify({"error": "Nonce já utilizado (replay)"}), 400

            # Valida hash do nonce
            received_nonce = decoded['tokenPayloadExternal']['requestDetails']['nonce']
            expected_hash = base64.urlsafe_b64encode(hashlib.sha256(f"acao_valiosa:{valor_unico}".encode()).digest()).decode().rstrip("=")


            if received_nonce.rstrip('=') != expected_hash.rstrip('='):
                return jsonify({"error": "Nonce inválido ou adulterado"}), 400


            # Armazena nonce como usado
            NONCE_STORE[valor_unico] = time.time() + NONCE_EXPIRATION_SECONDS

        return jsonify(decoded)

    except Exception as e:
        print("❌ Erro:", str(e))
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0")
