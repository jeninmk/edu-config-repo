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
