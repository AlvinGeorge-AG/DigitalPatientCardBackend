# # Stage 1: Build
# FROM maven:3.8.8-eclipse-temurin-17 AS build
# WORKDIR /workspace
# COPY pom.xml .
# COPY src ./src
# RUN mvn -B -DskipTests package

# # Stage 2: Run
# FROM eclipse-temurin:17-jre
# WORKDIR /app
# COPY --from=build /workspace/target/*.jar app.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "/app/app.jar"]
# We use a lightweight Java image
FROM eclipse-temurin:17-jdk-alpine 
# Copy the compiled .jar from the target folder into the container
COPY target/DigitalPatientCardBackend-0.0.1.jar app.jar
# Run the app
ENTRYPOINT ["java", "-jar", "/app.jar"]
