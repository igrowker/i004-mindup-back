# Etapa 1: Construcción de la aplicación con Maven
FROM maven:3.8.7-openjdk-18-slim AS builder

WORKDIR /app

# Copia el archivo pom principal y los poms de los módulos
COPY pom.xml ./
COPY eureka/pom.xml ./eureka/
COPY gateway/pom.xml ./gateway/
COPY chat/pom.xml ./chat/
COPY core/pom.xml ./core/

# Descargar dependencias sin hacer compilación
RUN mvn dependency:go-offline

# Copiar el código fuente de todos los módulos
COPY eureka/src ./eureka/src
COPY gateway/src ./gateway/src
COPY chat/src ./chat/src
COPY core/src ./core/src

# Construir todos los módulos sin ejecutar tests
RUN mvn clean package -DskipTests

# Etapa 2: Preparación de la imagen de producción
FROM openjdk:18-jdk-slim AS runner

WORKDIR /app

# Copiar los archivos .jar de cada módulo desde el builder
COPY --from=builder /app/eureka/target/*.jar eureka.jar
COPY --from=builder /app/gateway/target/*.jar gateway.jar
COPY --from=builder /app/chat/target/*.jar chat.jar
COPY --from=builder /app/core/target/*.jar core.jar

# Exponer los puertos necesarios (modificar según los servicios que se usen)
#EXPOSE 8761    # puerto común para Eureka
#EXPOSE 8080    # puerto para gateway
#EXPOSE 8081    # puerto para chat
#EXPOSE 8082    # puerto para core

# Limpieza de artefactos de construcción
RUN rm -rf /root/.m2

# Comando para iniciar los servicios. Aquí puedes ajustar para ejecutar un servicio específico
CMD ["java", "-jar", "gateway.jar"]
