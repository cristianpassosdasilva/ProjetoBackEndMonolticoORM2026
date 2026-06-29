# Trabalho - Mecanismo de Autenticação

Projeto Spring Boot com autenticação via JWT (Spring Security + OAuth2 Resource Server), seguindo o exercício de Mecanismos de Autenticação: entidades `Role` e `Permissao` relacionadas via `@ManyToMany` a `Usuario`, com controle de acesso por verbo HTTP usando `hasAuthority(...)`.

## Pré-requisitos

- Java 21
- Maven (ou usar o `./mvnw` incluso no projeto)
- MySQL Server rodando localmente

## 1. Configurar o banco de dados

O schema `db_autenticacao` é criado automaticamente na primeira execução (`createDatabaseIfNotExist=true`), então só é preciso ter um MySQL Server rodando com um usuário/senha válidos.

Copie o arquivo de exemplo e ajuste a senha do seu MySQL:

```bash
cd autenticacao/src/main/resources
cp application.properties.example application.properties
```

Edite `application.properties` e coloque a senha do seu usuário MySQL em `spring.datasource.password`.

## 2. Gerar as chaves JWT (RSA)

A aplicação assina e valida os tokens JWT com um par de chaves RSA que **não está no repositório** (são credenciais, ficam fora do git). Gere o seu próprio par com o Git Bash (ou qualquer terminal com OpenSSL):

```bash
cd autenticacao/src/main/resources
openssl genrsa -out app.key 2048
openssl rsa -in app.key -pubout -out app.pub
```

Isso cria os arquivos `app.key` (chave privada) e `app.pub` (chave pública) na pasta `resources`, exatamente onde o `application.properties` espera encontrá-los (`classpath:app.key` e `classpath:app.pub`).

## 3. Rodar a aplicação

```bash
cd autenticacao
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. As tabelas são recriadas a cada execução (`ddl-auto=create-drop`) e populadas automaticamente pelo `data.sql` com:

| Usuário | Senha     | Role  | Permissões                      |
|---------|-----------|-------|----------------------------------|
| cadu    | cadu123   | ADMIN | CLIENTE_READ, CLIENTE_WRITE      |
| joao    | joao123   | USER  | CLIENTE_READ                     |

## 4. Testar com a collection do Postman

O arquivo [`Mecanismos-Autenticacao.postman_collection.json`](Mecanismos-Autenticacao.postman_collection.json), na raiz deste projeto, já tem todas as requisições prontas.

1. Abra o Postman → **File → Import** → selecione o arquivo `Mecanismos-Autenticacao.postman_collection.json`.
2. Com a aplicação rodando, execute a pasta **"1 - Autenticacao"** primeiro (`POST /autenticacao` para `cadu` e para `joao`). Cada requisição já salva o token JWT retornado em uma variável da collection (`token_cadu` / `token_joao`) automaticamente.
3. Depois execute a pasta **"2 - Clientes"**. Resultados esperados:

| Requisição                              | Status esperado |
|------------------------------------------|------------------|
| GET /clientes (sem token)                | 401              |
| GET /clientes (cadu)                     | 200              |
| GET /clientes (joao)                     | 200              |
| POST /clientes (cadu)                    | 200              |
| POST /clientes (joao)                    | 403              |

O 403 no POST do joao é esperado: ele está autenticado (token válido), mas não tem a permissão `CLIENTE_WRITE`.
