FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /api
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/api-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]