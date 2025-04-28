FROM openjdk:20-jdk-slim
WORKDIR /app
COPY manqueappi-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]