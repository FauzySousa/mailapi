<div align="center">

# 🚀 MailAPI

### API profissional de envio de e-mails com Java + Spring Boot

<p align="center">
Backend robusto, frontend moderno responsivo, proteção contra spam, retry automático,
documentação Swagger/OpenAPI e arquitetura preparada para ambientes enterprise.
</p>

<br>

<img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" />
<img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot" />
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf" />
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker" />
<img src="https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger" />
<img src="https://img.shields.io/badge/Status-Production_Ready-success?style=for-the-badge" />

<br><br>

<img width="100%" src="https://via.placeholder.com/1200x500/0f172a/ffffff?text=MailAPI+Preview" />

</div>

---

# 📚 Sumário

- [📖 Sobre o Projeto](#-sobre-o-projeto)
- [⚙️ Tecnologias](#️-tecnologias)
- [✨ Funcionalidades](#-funcionalidades)
- [🏗 Arquitetura](#-arquitetura)
- [🔐 Segurança](#-segurança)
- [🚦 Rate Limiting](#-rate-limiting)
- [🔁 Retry Automático](#-retry-automático)
- [📡 Endpoints](#-endpoints)
- [📦 Estrutura de Pastas](#-estrutura-de-pastas)
- [🐳 Docker](#-docker)
- [📑 Swagger/OpenAPI](#-swaggeropenapi)
- [📊 Monitoramento](#-monitoramento)
- [🚀 Como Executar](#-como-executar)
- [🌍 Variáveis de Ambiente](#-variáveis-de-ambiente)
- [📨 Configuração SMTP](#-configuração-smtp)
- [📬 Exemplo de Requisição](#-exemplo-de-requisição)
- [📨 Exemplo de Resposta](#-exemplo-de-resposta)
- [✅ Boas Práticas Aplicadas](#-boas-práticas-aplicadas)
- [🛣 Features Futuras](#-features-futuras)
- [☁️ Deploy](#️-deploy)
- [👨‍💻 Autor](#-autor)
- [📄 Licença](#-licença)

---

# 📖 Sobre o Projeto

O **MailAPI** é uma API RESTful profissional desenvolvida para gerenciamento e envio de e-mails utilizando o ecossistema Spring.

O projeto foi construído com foco em:

- ✅ Arquitetura limpa
- ✅ Escalabilidade
- ✅ Segurança
- ✅ Resiliência
- ✅ Boas práticas enterprise
- ✅ Performance
- ✅ Experiência do usuário

Além do backend robusto, o sistema também possui um frontend institucional moderno integrado diretamente à API utilizando Thymeleaf.

---

# ⚙️ Tecnologias

| Tecnologia | Descrição |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot | Framework backend |
| Spring Web | API REST |
| Spring Validation | Validação de dados |
| Thymeleaf | Renderização HTML |
| Java Mail Sender | Envio de e-mails |
| Spring Retry | Retry automático |
| Bucket4j | Rate limiting |
| Swagger/OpenAPI | Documentação |
| Spring Actuator | Monitoramento |
| Docker | Containerização |
| Maven | Gerenciamento de dependências |

---

# ✨ Funcionalidades

## 📬 Sistema de E-mails

- Envio de e-mails HTML
- Processamento assíncrono
- Retry automático em falhas SMTP
- Templates HTML com Thymeleaf
- Logs estruturados
- Tratamento global de erros

## 🌐 Frontend Integrado

- Landing page institucional
- Formulário de contato
- Feedback visual em tempo real
- Interface responsiva
- Integração completa com API REST

## 🔒 Segurança

- Proteção contra spam
- Rate limiting por IP
- Estrutura preparada para API Key
- Tratamento global de exceções
- Validação robusta de entrada

---

# 🏗 Arquitetura

```text
Cliente/Frontend
        │
        ▼
Controller Layer
        │
        ▼
Service Layer
        │
        ▼
Async Executor + Retry
        │
        ▼
SMTP Provider

📡 Endpoints
📧 Enviar E-mail
POST /api/v1/emails/send
Request Body
{
  "to": "destinatario@email.com",
  "subject": "Bem-vindo",
  "body": "<h1>Olá Mundo</h1>"
}

📨 Formulário de Contato
POST /api/v1/emails/contact
Request Body
{
  "name": "Fauzy Sousa",
  "senderEmail": "fauzy@email.com",
  "message": "Olá, gostaria de conhecer sua API."
}

🔐 Segurança
Recurso	Implementação
Rate Limit	Bucket4j
Validação	Bean Validation
Retry	Spring Retry
Tratamento Global	@RestControllerAdvice
Logs	SLF4J
Async	@Async
🚦 Rate Limiting

Proteção contra spam e abuso utilizando Bucket4j.
Configuração Atual
✅ 5 requisições por minuto por IP
✅ Bloqueio automático
✅ Resposta HTTP 429
Exemplo de resposta
{
  "error": "Limite de requisições excedido. Tente novamente em 1 minuto."
}

🔁 Retry Automático
A API possui retry automático para falhas SMTP temporárias.
Estratégia
✅ Máximo de 3 tentativas
✅ Backoff exponencial
✅ Delay progressivo
✅ Recover para falha definitiva

📦 Estrutura de Pastas
src
 ┣ main
 ┃ ┣ java
 ┃ ┃ ┗ com.fauzy.emailservice
 ┃ ┃ ┣ config
 ┃ ┃ ┣ controller
 ┃ ┃ ┣ dto
 ┃ ┃ ┣ exception
 ┃ ┃ ┣ service
 ┃ ┃ ┗ service.impl
 ┃ ┣ resources
 ┃ ┃ ┣ static
 ┃ ┃ ┃ ┣ css
 ┃ ┃ ┃ ┣ js
 ┃ ┃ ┃ ┗ images
 ┃ ┃ ┣ templates
 ┃ ┃ ┃ ┣ index.html
 ┃ ┃ ┃ ┗ contact-template.html
 ┃ ┃ ┗ application.properties
 ┗ test
   ┗ java

🐳 Docker
Build da imagem
docker build -t mailapi .
Executar container
docker run -p 8080:8080 mailapi

📑 Swagger/OpenAPI
Documentação interativa disponível em:
http://localhost:8080/swagger-ui.html

📊 Monitoramento
O projeto utiliza Spring Boot Actuator para monitoramento.
Endpoints disponíveis
/actuator/health
/actuator/info
/actuator/metrics

🚀 Como Executar
1️⃣ Clonar repositório
git clone https://github.com/seuusuario/mailapi.git
2️⃣ Entrar no projeto
cd mailapi
3️⃣ Executar aplicação
./mvnw spring-boot:run

🌍 Variáveis de Ambiente
server.port=8080

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=SEU_EMAIL
spring.mail.password=SUA_SENHA

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

📨 Configuração SMTP
O projeto utiliza SMTP do Gmail.
Requisitos
Ativar autenticação em 2 fatores
Criar senha de aplicativo Google

📬 Exemplo de Requisição
Curl — Envio de E-mail
curl --request POST \
  --url http://localhost:8080/api/v1/emails/send \
  --header 'Content-Type: application/json' \
  --data '{
    "to": "user@email.com",
    "subject": "Teste",
    "body": "<h1>Email enviado com sucesso</h1>"
}'

📨 Exemplo de Resposta
✅ Sucesso
{
  "message": "E-mail encaminhado para processamento",
  "timestamp": "2026-05-08T14:22:10"
}
❌ Erro de Validação
{
  "status": 400,
  "error": "Validation Error",
  "message": "E-mail inválido",
  "path": "/api/v1/emails/send",
  "timestamp": "2026-05-08T14:22:10"
}

✅ Boas Práticas Aplicadas
✔ Clean Code
✔ DTO Pattern
✔ Separation of Concerns
✔ Async Processing
✔ Retry Pattern
✔ Global Exception Handler
✔ Structured Logging
✔ Validation Layer
✔ Layered Architecture
✔ RESTful API Design
✔ Dockerização
✔ Documentação Swagger
✔ Responsividade no Frontend
✔ Rate Limiting
✔ Monitoramento com Actuator

🛣 Features Futuras
 Integração com RabbitMQ
 Integração com Kafka
 API Key Authentication
 JWT Authentication
 Dashboard administrativo
 Persistência em banco de dados
 Histórico de e-mails enviados
 Sistema de filas
 Templates dinâmicos
 Observabilidade com Prometheus + Grafana
 Deploy Kubernetes
 Integração AWS SES

☁️ Deploy
Plataforma	Compatível
Docker	       ✅
Render	       ✅
Railway	       ✅
AWS	           ✅
Azure	       ✅
DigitalOcean   ✅

👨‍💻 Autor
Fauzy Sousa
Backend Developer Java & Spring Boot
🌐 Redes
GitHub: https://github.com/seuusuario
LinkedIn: https://linkedin.com/in/seuperfil

📄 Licença
Este projeto está sob a licença MIT.

⭐ Considerações Finais
O MailAPI foi desenvolvido com foco em arquitetura profissional, escalabilidade e boas práticas modernas do ecossistema Spring.
O projeto demonstra conhecimentos sólidos em:
APIs REST
Backend Java
Segurança
Docker
Observabilidade
Resiliência
Processamento assíncrono
Arquitetura enterprise