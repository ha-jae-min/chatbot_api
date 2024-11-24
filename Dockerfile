FROM openjdk:17-jdk-alpine

COPY build/libs/*.jar app.jar

# .env 파일 복사
COPY .env .env

ENTRYPOINT ["java", "-jar", "/app.jar"]