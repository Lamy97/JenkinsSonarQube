# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the project files
COPY pom.xml .
COPY personne-ms/pom.xml personne-ms/
COPY runner-ms/pom.xml runner-ms/
COPY tp-ms/pom.xml tp-ms/

# Copy source code
COPY personne-ms/src personne-ms/src
COPY runner-ms/src runner-ms/src
COPY tp-ms/src tp-ms/src

# Build the project
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the generated JAR from the runner-ms module
COPY --from=build /app/runner-ms/target/runner-ms-*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
