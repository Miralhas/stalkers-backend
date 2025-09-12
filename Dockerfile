FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Generate RSA key pair for the authentication to work
RUN mkdir -p src/main/resources/keys && \
    openssl genrsa -out src/main/resources/keys/private.pem 2048 && \
    openssl rsa -in src/main/resources/keys/private.pem -pubout -out src/main/resources/keys/public.pem

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

RUN mkdir -p /app/images

ENTRYPOINT ["java","-jar","/app.jar"]
