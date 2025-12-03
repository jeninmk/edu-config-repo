<<<<<<< HEAD
# Use Eclipse Temurin 17 base image
FROM eclipse-temurin:17-jdk-alpine
=======
# Use OpenJDK 17 base image
FROM openjdk:17-jdk-slim
>>>>>>> 18b3f5cbe4e2b027e2f5f420a9624f9ff2fb6d3d

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=dev

# Add jar to container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","/app.jar"]
