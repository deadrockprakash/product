# --- STAGE 1: Build Stage (The "Kitchen") ---
# We use a full JDK image to compile our application.
#This is like having a fully equipped kitchen to prepare our meal. Once the meal is ready, we will serve it in a much smaller and more efficient dining room (runtime stage).

FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy only the pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# --- STAGE 2: Runtime Stage (The "Dining Room") ---
# We switch to JRE (Java Runtime Environment) because it is
# much smaller and more secure than the JDK.
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy ONLY the compiled JAR from the build stage
# This ensures the final image does not contain Maven or your source code
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "app.jar"]