FROM openjdk:11-jdk
EXPOSE 8080
COPY ${JAR_FILE} ${JAR_FILE}
ADD target/solid-lendig-platform-*.jar solid-lendig-platform.jar
ENTRYPOINT ["java","-jar","/solid-lendig-platform.jar"]
