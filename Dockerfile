FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} KalturaCompatibilityService.jar
ENTRYPOINT ["java","-jar","/KalturaCompatibilityService.jar"]