FROM openjdk:8-jdk-alpine
VOLUME /opt
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} church-management-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/church-management-1.0-SNAPSHOT.jar"]