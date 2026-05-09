# Estágio 1: Build (Compilação)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copia os arquivos de configuração do Maven e o código fonte
COPY pom.xml .
COPY src ./src
# Executa o build do Maven ignorando os testes para ser mais rápido
RUN mvn clean package -DskipTests

# Estágio 2: Run (Execução)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copia apenas o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

RUN useradd -ms /bin/bash spring
RUN chown spring:spring app.jar
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]