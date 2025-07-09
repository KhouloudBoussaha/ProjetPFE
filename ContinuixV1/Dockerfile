# Use Java 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the specific JAR file from the target directory
COPY target/Continuix-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (default for Spring Boot is 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]