FROM maven:3.8.6-openjdk-18-slim AS build
WORKDIR /app

COPY ./home /app/home
WORKDIR /app/home

# Build the project
RUN mvn -f /app/home/pom.xml clean package -DskipTests

# Stage 2: Run
FROM openjdk:18-jdk-alpine
WORKDIR /app

# Copy compiled artifacts from the build stage
COPY --from=build /app/home/target/*.jar /app/app.jar

# Exposing the port that your Spring Boot app will listen on
EXPOSE 9090

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
