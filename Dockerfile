# ==========================
# Etapa de compilación
# ==========================
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests


# ==========================
# Imagen final
# ==========================
FROM eclipse-temurin:21-jre-alpine

# Herramientas necesarias en runtime
# ffmpeg -> generación de thumbnails
# wget   -> Docker HEALTHCHECK
RUN apk add --no-cache \
    ffmpeg \
    wget

WORKDIR /app

COPY --from=builder /app/target/VortexVideo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080


# Verifica que Spring Boot esté listo
HEALTHCHECK --interval=30s \
            --timeout=3s \
            --start-period=30s \
            --retries=3 \
CMD wget --spider -q http://localhost:8080/actuator/health/readiness || exit 1


ENTRYPOINT ["java","-jar","app.jar"]