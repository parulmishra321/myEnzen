FROM gradle:7.1.1-jdk11 as builder
COPY . ./
RUN gradle clean build

FROM openjdk:11-jdk
COPY ./src/main/resources/ .
COPY --chown=root:root --from=builder /home/gradle/build/libs/*SNAPSHOT.jar /enzen-be.jar
ENTRYPOINT ["java", "-jar", "-Dspring.cloud.bootstrap.location=/bootstrap.yml", "/enzen-be.jar"]

