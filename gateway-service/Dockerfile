<<<<<<< HEAD
# gateway-service Dockerfile example
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/gateway-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
=======
# Use OpenJDK 17 base image
FROM openjdk:17-jdk-slim

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=dev

# Add jar to container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","/app.jar"]
>>>>>>> 18b3f5cbe4e2b027e2f5f420a9624f9ff2fb6d3d
