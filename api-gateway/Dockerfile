# Multi-stage build for user-service
FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder

# Set working directory in the container
WORKDIR /app

# Copy Maven wrapper and project files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies offline - leverage build cache
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application, skipping tests
RUN ./mvnw clean package -X -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Set working directory
WORKDIR /app

# Copy the jar from the builder stage
COPY --from=builder /app/target/*.jar api-gateway.jar

# Expose the port the app runs on
EXPOSE 8080

# Environment variables for configuration
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"

## Health check
#HEALTHCHECK --interval=30s --timeout=3s \
#    CMD wget -q --spider http://localhost:9020/users/actuator/health || exit 1

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]