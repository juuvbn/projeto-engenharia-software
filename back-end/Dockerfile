# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

ENV SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/cuidarplus \
    SPRING_DATASOURCE_USERNAME=cuidarplus \
    SPRING_DATASOURCE_PASSWORD=cuidarplus123 \
    JWT_SECRET=Y3VpZGFycGx1cy1qd3Qtc2VjcmV0LWtleS0zMi1ieXRlcw== \
    JWT_EXPIRATION_MS=86400000

COPY --from=builder /app/target/cuidarplus-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
