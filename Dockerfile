# --- STAGE 1: Build Stage ---
# CHANGE: Use maven image with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# --- STAGE 2: Runtime Stage ---
# CHANGE: Use JRE 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]