FROM openjdk:17-alpine
WORKDIR /app
COPY  /target/*.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["sh" ,"-c"]
CMD ["exec java  -jar /app.jar  "]