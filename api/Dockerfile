FROM openjdk:21-jdk
LABEL authors="lolman15"

WORKDIR /api

COPY /target/api-0.0.1-SNAPSHOT.jar api.jar

ENTRYPOINT ["java", "-jar", "api.jar"]