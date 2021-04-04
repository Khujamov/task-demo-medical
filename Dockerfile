FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/test-app.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]