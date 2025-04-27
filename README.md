# 📊 Java Calculator API com Kafka

Projeto Java com Spring Boot para disponibilizar uma API REST de operações matemáticas usando comunicação assíncrona via Apache Kafka entre dois módulos:

- **API REST** (produtor de mensagens)
- **Calculator Service** (consumidor de mensagens)

---

## 📦 Tecnologias

- Java 21
- Spring Boot 3.x
- Apache Kafka
- Docker & Docker Compose
- Maven

---

## 📐 Estrutura
/ ├── api/ │ ├── Dockerfile │ └── target/api-0.0.1-SNAPSHOT.jar ├── calculator/ │ ├── Dockerfile │ └── target/calculator-0.0.1-SNAPSHOT.jar ├── docker-compose.yaml └── README.md


## 🚀 Como Executar

### 📌 Pré-requisitos:

- Java 21
- Maven
- Docker e Docker Compose

---

### 📦 Build dos módulos

1. Compilar o módulo `api`
```bash```
cd api
mvn clean package -DskipTests

2. Compilar o módulo `calculator`

cd ../calculator
mvn clean package -DskipTests

3. Subir os containers

docker-compose up --build

Método | Endpoint | Parâmetros | Descrição
GET | /sum | a, b | Retorna a soma de a e b
GET | /sub | a, b | Retorna a subtração de a e b
GET | /mul | a, b | Retorna o produto de a e b
GET | /div | a, b | Retorna a divisão de a por b

4. Exemplo de Request
url "http://localhost:8080/sum?a=10&b=5"

5. Resposta

{
  "requestId": "d85f2ef0-82e4-4c9f-b9e2-61fd27ab0a91",
  "result": 15
}

