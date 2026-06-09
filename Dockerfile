FROM eclipse-temurin:21-jre-jammy AS runtime

LABEL org.opencontainers.image.title="CompanyStatusChecker" \
      org.opencontainers.image.description="Java 21 service for Russian company INN validation and DaData status checks." \
      org.opencontainers.image.source="https://github.com/krotname/CompanyStatusChecker" \
      org.opencontainers.image.licenses="GPL-3.0-only"

WORKDIR /app

COPY target/checker-corporate-*.jar app.jar

ENV DADATA_TOKEN=""

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
