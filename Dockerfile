# Use lightweight JDK image
FROM eclipse-temurin:17-jdk-jammy

# Create app directory
WORKDIR /app

# Copy jar (generic name avoids version mismatch)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Run as non-root user (security best practice)
RUN useradd -m springuser
USER springuser

# Expose application port
EXPOSE 8080

# JVM tuning for containers
ENTRYPOINT ["java","-Xms256m","-Xmx512m","-jar","app.jar"]
