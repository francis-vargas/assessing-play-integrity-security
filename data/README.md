
# Dataset – Play Integrity API (Resultados Brutos)

Este repositório contém os **dados brutos** resultados dos experimentos do trabalho:

> *Assessing the Security Coverage of the Google Play Integrity API on Android*

Os dados aqui publicados complementam o repositório principal de código e têm como objetivo apoiar a **reprodutibilidade** e a análise independente dos resultados.

---

## Conteúdo

Atualmente o repositório contém:

- `playintegrity_results.csv`  
  Arquivo em formato CSV com os **resultados brutos** das requisições feitas à Google Play Integrity API.  
  Cada linha representa **uma requisição** da aplicação Android ao backend, incluindo:
  - informações de contexto do experimento (como cenário/modo de uso da API),
  - principais vereditos retornados (por exemplo, `appRecognitionVerdict`, `deviceRecognitionVerdict`, etc.),
  - timestamps de quando o backend recebeu e processou o token de integridade.

> Observação: os nomes exatos das colunas podem ser consultados diretamente no próprio CSV.

---

## Ambiente de Execução dos Testes

Os experimentos registrados neste CSV foram executados em um ambiente controlado, com o seguinte setup de alto nível:

- **Aplicativo Android de teste**
  - App de demonstração desenvolvido para o experimento, com suporte aos modos Classic e Standard da Play Integrity API.
  - Assinado com chave específica de laboratório (não reutilizada em apps de produção).
  - Aplicativo versionado como teste interno na Google Play Console.

- **Servidor backend**
  - Sistema operacional: **Windows 10**
  - Memória RAM: **16 GB**
  - Processador: 
  - Tecnologias principais:
    - **Python** (3.x)
    - **Flask** para exposição da API HTTP
    - Bibliotecas da Google para validação dos tokens da Play Integrity API
  - Responsabilidades:
    - receber o token enviado pelo app,
    - validar a assinatura e decodificar o veredito,
    - registrar os resultados em formato estruturado e exportá-los para CSV.

- **Cliente (dispositivo/emulador Android)**
  - Dispositivo físico ou emulador Android com Google Play Services,
  - Autenticado com uma conta Google válida para permitir o uso da Play Integrity API.

---

## Timestamps e Período dos Testes

Os horários presentes no arquivo `playintegrity_results.csv` correspondem ao momento em que o **backend recebeu e processou** cada requisição de verificação de integridade.

De forma resumida:

- Todos os registros do CSV foram coletados durante um **período concentrado de experimentos**, em ambiente controlado.
- O timestamp de cada linha deve ser usado como referência temporal para:
  - ordenar os testes,
  - correlacionar cenários,
  - identificar possíveis variações de comportamento ao longo do tempo.

Quando necessário, o artigo principal detalha como esses timestamps foram utilizados na análise (por exemplo, seleção de amostras em cada cenário).

---

## Uso e Referência

Este dataset pode ser utilizado para:

- reproduzir (total ou parcialmente) as análises do trabalho;
- explorar o comportamento da Play Integrity API em diferentes cenários;
- servir de base para estudos comparativos ou extensões da pesquisa.

Ao utilizar este repositório em outros trabalhos, por favor cite o artigo correspondente:

> Vargas, F. *Assessing the Security Coverage of the Google Play Integrity API on Android*, 20XX.  
> (Atualize o ano e dados finais conforme a publicação.)

---

## Repositório de Código

O código-fonte do aplicativo Android de teste e do backend utilizado para gerar este CSV encontra-se em:

- Repositório principal: **`francis-vargas/assessing-play-integrity-security`**

Este repositório atual é dedicado apenas aos **dados brutos** da pesquisa.
