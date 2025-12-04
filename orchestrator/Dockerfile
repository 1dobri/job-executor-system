# === Build stage ===
FROM gradle:8-jdk21-alpine AS build
WORKDIR /home/gradle/project

# Copy everything (for a simple setup)
COPY . .

# Build the Spring Boot fat jar
RUN gradle bootJar --no-daemon

# === Runtime stage ===
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]