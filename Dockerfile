FROM eclipse-temurin:17-jre-alpine

WORKDIR /opt/app

COPY build/libs/bingo-*.jar /opt/app/bingo.jar

ENTRYPOINT ["java", "-jar", "/opt/app/bingo.jar"]

EXPOSE 80