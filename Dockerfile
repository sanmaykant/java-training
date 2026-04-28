# Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run Stage
# Using Temurin instead of the deprecated openjdk image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# We use a wildcard to find the jar in the target folder
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
