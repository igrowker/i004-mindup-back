
# Etapa 1: Construcción de la aplicación con Maven
FROM maven:3.8.7-openjdk-18-slim AS builder

WORKDIR /app

COPY pom.xml ./
COPY eureka/pom.xml ./eureka/
COPY gateway/pom.xml ./gateway/
COPY chat/pom.xml ./chat/
COPY core/pom.xml ./core/

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Preparación de la imagen de producción
FROM openjdk:24-jdk-slim AS runner

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080 

# Limpieza de artefactos de construcción
RUN rm -rf /app/src \
    && rm -rf /root/.m2

CMD ["java", "-jar", "app.jar"]


#docker build -t springboot-docker crear imagen 
#docker run -p 8080:8080 springboot-docker
