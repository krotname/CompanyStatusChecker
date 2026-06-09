FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

COPY target/checker-corporate-1.1.0.jar app.jar

ENV DADATA_TOKEN=""

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
