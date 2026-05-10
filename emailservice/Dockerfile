# ====== STAGE 1: BUILD ======
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests


# ====== STAGE 2: RUN ======
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN apk update && apk upgrade

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /app/target/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", \
"-XX:+UseContainerSupport", \
"-XX:MaxRAMPercentage=75.0", \
"-Djava.security.egd=file:/dev/./urandom", \
"-jar", \
"app.jar"]