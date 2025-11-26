# 📱 Play Integrity Demo App

Este projeto é um aplicativo Android de demonstração para uso da [Play Integrity API](https://developer.android.com/google/play/integrity) do Google, com backend em Flask para decodificar o token de integridade gerado pelo app.

## 🔐 Objetivo

Verificar a integridade do dispositivo, do aplicativo e da conta, utilizando o serviço da Play Integrity para detectar fraudes, dispositivos modificados, e builds não oficiais.

---

## 📁 Estrutura

- `app/` – Código-fonte do aplicativo Android.
- `main.py` (no repositório do backend) – Servidor Flask que recebe o token de integridade e consulta a API do Google para validá-lo.

---

## ⚙️ Requisitos

- Android Studio (versão compatível com AGP 8+ e Java 17)
- Projeto criado no [Google Cloud Console](https://console.cloud.google.com/)
- API Play Integrity ativada
- Um número de projeto (Project Number) do Google Cloud
- Backend em Flask (com a chave de conta de serviço do Google)

---

## 📲 Configuração do App Android

1. **Abra o projeto no Android Studio**
2. No arquivo `MainActivity.java`, substitua o valor do número do projeto:

```java
.setCloudProjectNumber(123456789012L) // <- Substitua aqui
```

3. Altere também a `BACKEND_URL`:

```java
String backendUrl = "https://<SEU_NGROK_OU_BACKEND>/verify";
```

---

## 🧠 Funcionamento (Alto Nível)

1. O app gera um `nonce` e solicita um token de integridade ao Google.
2. O token é enviado ao backend.
3. O backend envia esse token para a API do Google Play Integrity (via chave de conta de serviço JSON).
4. A resposta é decodificada e exibida no app.

---

## 🔧 Configuração do Backend

1. Instale dependências:

```bash
pip install flask google-auth google-auth-httplib2 google-auth-oauthlib google-api-python-client
```

2. Crie uma chave de **conta de serviço** com permissão para usar a API Play Integrity.
3. Salve o JSON como, por exemplo: `service-account.json`.

4. Defina a variável de ambiente no Windows:

```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS="C:\caminho\para\service-account.json"
```

Ou no Linux/macOS:

```bash
export GOOGLE_APPLICATION_CREDENTIALS="/caminho/para/service-account.json"
```

5. Inicie o backend:

```bash
python main.py
```

---

## 🚀 Teste com Dispositivo Físico

A Play Integrity **não funciona em emuladores**. É necessário instalar o APK em um celular Android com acesso à Play Store.

Para gerar o APK:

```bash
./gradlew assembleDebug
```

O APK será gerado em `app/build/outputs/apk/debug/app-debug.apk`.

Transfira para o celular e instale.

---

## 🔒 Observações

- **Não suba seu arquivo `service-account.json` para o Git.** Ele contém credenciais sensíveis.
- Adicione ao seu `.gitignore`:

```
service-account.json
```

---

## 📫 Contato

Este projeto foi desenvolvido para fins acadêmicos. Para dúvidas, sugestões ou contribuições, entre em contato com o autor do repositório.

