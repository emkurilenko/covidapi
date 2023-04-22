# Build app from sources
FROM gradle:7.4.1-jdk17 AS build
COPY --chown=gradle:gradle src /home/gradle/src/src
COPY --chown=gradle:gradle build.gradle /home/gradle/src/
COPY --chown=gradle:gradle settings.gradle /home/gradle/src/
COPY --chown=gradle:gradle gradle /home/gradle/src/gradle
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test --info

# Build image from jar 
FROM openjdk:17-slim
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
CMD ["java", "-jar", "/app/spring-boot-application.jar"]
