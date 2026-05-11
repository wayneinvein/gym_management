# ================================
# Stage 1 — Build the JAR
# ================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml first and download dependencies
# This is cached by Docker — if pom.xml hasn't changed
# Docker skips this step on next build (faster builds)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ================================
# Stage 2 — Run the JAR
# ================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy only the built JAR from Stage 1
# Final image has no Maven, no source code — just the JRE and JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]