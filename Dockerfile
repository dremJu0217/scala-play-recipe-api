FROM hseeberger/scala-sbt:latest as builder

WORKDIR /app
COPY . .

RUN sbt stage

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/universal/stage /app

ENV PORT=8080
EXPOSE 8080

CMD ["bin/recipe-play-api-test", "-Dplay.server.http.port=${PORT}"]
