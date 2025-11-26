# 🔐 Backend - Verificação com Google Play Integrity API

Este projeto é um backend simples em **Python (Flask)** que serve para **verificar tokens de integridade** gerados pelo aplicativo Android usando a **Play Integrity API** do Google.

---

## 📦 O que este backend faz?

1. Recebe um **token de integridade** enviado pelo aplicativo Android.
2. Envia esse token para a API oficial da Google (Play Integrity).
3. Retorna os resultados da verificação, como:
   - Estado do dispositivo (`MEETS_DEVICE_INTEGRITY`)
   - Validação do pacote e assinatura do app
   - Outros indicadores de segurança

---

## ⚙️ Pré-requisitos

- Python 3.8 ou superior
- Conta no Google Cloud com o **Play Integrity API** ativado
- Um arquivo de **Service Account JSON** com permissão para acessar a Play Integrity API

---

## 🛠️ Instalação

1. Clone este repositório:

```bash
git clone https://gitlab.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

2. Instale as dependências:

```bash
pip install -r requirements.txt
```

3. Coloque o seu arquivo `service-account.json` (obtido no console do Google Cloud) no diretório do projeto.

4. Edite o arquivo `main.py` para configurar o nome do arquivo JSON da service account:

```python
credentials = service_account.Credentials.from_service_account_file(
    'engenharia-reversa-461115-6c26741e54cf.json'
)
```

---

## 🚀 Executando o servidor

```bash
python main.py
```

O backend ficará acessível localmente em:  
`http://127.0.0.1:5000`

---

## 📲 Endpoint disponível

### `POST /verify`

Envia um token de integridade para validação no Google.

**Corpo da requisição JSON:**

```json
{
  "integrity_token": "eyJhbGciOiJBM..."
}
```

**Exemplo de resposta:**

```json
{
  "deviceIntegrity": {
    "deviceRecognitionVerdict": ["MEETS_DEVICE_INTEGRITY"]
  },
  "appIntegrity": {
    "appRecognitionVerdict": "PLAY_RECOGNIZED"
  }
}
```

---

## 🔐 Obtendo o arquivo `service-account.json`

1. Acesse o [Google Cloud Console](https://console.cloud.google.com/).
2. Vá até "IAM e administrador" > "Contas de serviço".
3. Crie uma conta de serviço (caso não tenha).
4. Vá em "Chaves" > "Adicionar chave" > "Criar nova chave" > Tipo: JSON.
5. Baixe o arquivo e coloque no diretório do projeto.

---

## 📋 Observações

- O aplicativo Android **deve obter um token válido** com o método `requestIntegrityToken()`.
- O token deve ser enviado ao backend.
- O backend deve decodificar e verificar o token com a API da Google.
- Esse fluxo **não funciona em emuladores** — apenas em dispositivos físicos com o Google Play Services.

---

## 👨‍💻 Autor

Projeto criado para fins educacionais e de validação de segurança mobile com a Play Integrity API.