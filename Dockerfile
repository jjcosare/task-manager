FROM amazoncorretto:17.0.6-al2023-headless
MAINTAINER jjcosare@gmail.com
ARG JAR_FILE=./build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]