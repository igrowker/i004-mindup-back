# Etapa 1: Construcción de la aplicación con Maven
FROM maven:3.8.7-openjdk-18-slim AS builder

WORKDIR /app

# Copiar el archivo pom.xml y los poms de los módulos que quieres construir
COPY pom.xml ./
COPY eureka/pom.xml ./eureka/
COPY gateway/pom.xml ./gateway/
COPY chat/pom.xml ./chat/
COPY core/pom.xml ./core/

# Descargar dependencias sin hacer compilación
RUN mvn dependency:go-offline

# Copiar el código fuente de los módulos
COPY eureka/src ./eureka/src
COPY gateway/src ./gateway/src
COPY chat/src ./chat/src
COPY core/src ./core/src

# Construir los módulos sin ejecutar tests
RUN mvn clean package -DskipTests

# Etapa 2: Preparación de la imagen de producción
FROM openjdk:18-jdk-slim AS runner

WORKDIR /app

# Copiar los archivos .jar de cada servicio desde el builder
COPY --from=builder /app/eureka/target/*.jar eureka.jar
COPY --from=builder /app/gateway/target/*.jar gateway.jar
COPY --from=builder /app/chat/target/*.jar chat.jar
COPY --from=builder /app/core/target/*.jar core.jar

# Exponer los puertos necesarios
EXPOSE 8761 8080 8081 8082

# Limpieza de artefactos de construcción
RUN rm -rf /root/.m2

# Comando para iniciar todos los servicios en paralelo
CMD java -jar eureka.jar & \
    java -jar gateway.jar & \
    java -jar chat.jar & \
    java -jar core.jar & \
    wait
