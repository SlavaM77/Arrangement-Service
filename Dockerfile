FROM openjdk:21-jdk-slim

WORKDIR /app/arrangement.service

COPY target/*.jar app.jar

EXPOSE ${nTLS_PORT} ${TLS_PORT} ${DEBUG_PORT}

ENTRYPOINT java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${DEBUG_PORT} -jar app.jar
