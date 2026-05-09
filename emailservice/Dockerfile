# Usamos a versão Alpine: muito menor e com menos vulnerabilidades que o Jammy
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Atualiza pacotes internos para corrigir falhas de segurança conhecidas
RUN apk update && apk upgrade

# Cria o usuário 'spring' de forma segura no Alpine
RUN addgroup -S spring && adduser -S spring -G spring

# IMPORTANTE: Para este comando COPY funcionar no Render, 
# você precisa ter o arquivo .jar na raiz ou usar o Multi-stage build.
COPY target/emailservice-0.0.1-SNAPSHOT.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

# Mantive seus parâmetros de performance exatamente como você queria
ENTRYPOINT ["java", \
"-XX:+UseContainerSupport", \
"-XX:MaxRAMPercentage=75.0", \
"-Djava.security.egd=file:/dev/./urandom", \
"-jar", \
"app.jar"]