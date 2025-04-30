# Java Calculator API with Kafka

This is a Java Spring Boot project that provides a REST API for basic math operations using asynchronous communication via Apache Kafka between two modules:

  - API REST – sends requests (Kafka producer)

  - Calculator Service – processes operations (Kafka consumer)

## 1. Technologies Used
- Java 17
- Spring Boot 3.x
- Apache Kafka
- Docker & Docker Compose
- Maven

## 2. Project Structure

    ├── api/
    │   ├── Dockerfile
    │   └── target/api-0.0.1-SNAPSHOT.jar
    ├── calculator/
    │   ├── Dockerfile
    │   └── target/calculator-0.0.1-SNAPSHOT.jar
    ├── common/ (shared module containnig common DTOs)
    ├── logs/ (Folder for application logs)     
    ├── docker-compose.yml
    └── README.md

## 2. How to Run the Project

### 2.1. Prerequisites
- Java 17 installed
- Maven installed
- Docker and Docker Compose installed
- Postman(optional) – or any other tool you prefer to make HTTP requests

### 2.2. Build the Modules
Run the following commands in the terminal:

#### 2.2.1. Build the API module
    cd api
    mvn clean package -DskipTests

#### 2.2.2. Build the Calculator module
    cd ../calculator
    mvn clean package -DskipTests

### 2.3. Run with Docker Compose
After building the modules, from the project root run:
      
      docker-compose up --build

This will:
- Start Apache Kafka and Zookeeper
- Launch the api and calculator services

The application will be ready to use immediately!

### Stop the Application
To stop the application cleanly, run:

    docker-compose down -v

This will shut down all containers and release any used resources (such as ports and volumes).
Alternatively, you can shut it down using the Docker Desktop application.

## 3. API Endpoints

|Method|Endpoint|Parameters|Description|
|--------|----------|------------|------------------------------------|
|GET| /sum|	a, b	|Returns the sum of a and b|
|GET|	/sub|	a, b	|Returns the subtraction of a and b|
|GET|	/mul|	a, b	|Returns the product of a and b|
|GET|	/div|	a, b	|Returns the division of a by b|

### 3.1 Example Request

link: http://localhost:8080/sum?a=10&b=5

    Example Response:

    {
      "requestId": "d85f2ef0-82e4-4c9f-b9e2-61fd27ab0a91",
      "result": 15  
    }  