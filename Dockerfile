FROM eclipse-temurin:17.0.7_7-jre
WORKDIR /rony-commerce
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} rony-commerce.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","rony-comme   rce.jar"]