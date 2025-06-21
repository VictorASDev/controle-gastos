# Controle de Gastos

Sistema de controle de gastos pessoais desenvolvido em Spring Boot, com autenticação JWT, integração com banco de dados MySQL e API RESTful.

## Funcionalidades

- Cadastro de usuários com senha criptografada
- Login com geração de token JWT
- Cadastro, listagem e remoção de gastos por usuário autenticado
- Controle de permissões por papéis (USER, ADMIN)
- Integração com banco de dados MySQL
- Documentação automática via Swagger/OpenAPI

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Maven

## Estrutura do Projeto

- Código-fonte: `src/main/java/com/projetos/controle_gastos`
- Configurações e scripts: `src/main/resources`
- Testes: `src/test/java/com/projetos/controle_gastos`

## Configuração

### 1. Banco de Dados

- Crie um banco MySQL chamado `controle_gastos_db`.
- Ajuste usuário e senha em `src/main/resources/application.properties`.

### 2. Chaves JWT

**As chaves não são enviadas no repositório por segurança.**  
Gere suas próprias chaves RSA para o JWT:

```sh
openssl genrsa -out src/main/resources/app.key
openssl rsa -in src/main/resources/app.key -pubout -out src/main/resources/app.pub
```

Adicione as linhas abaixo ao seu `.gitignore`:

```
src/main/resources/app.key
src/main/resources/app.pub
```

### 3. Tabelas e Papéis

O script `src/main/resources/data.sql` cria a tabela de papéis e insere os papéis iniciais.

## Como Executar

```sh
./mvnw spring-boot:run
```
ou no Windows:
```sh
mvnw.cmd spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## Endpoints Principais

- `POST /users` — Cadastro de usuário
- `POST /login` — Login e obtenção do token JWT
- `POST /expenses` — Cadastro de gasto (autenticado)
- `GET /expenses` — Listagem de gastos do usuário autenticado
- `DELETE /expenses/{id}` — Remoção de gasto (autenticado)

## Documentação da API

Acesse a documentação Swagger em:  
`http://localhost:8080/swagger-ui/index.html`

Desenvolvido por [Victor Augusto](https://github.com/VictorASDev)