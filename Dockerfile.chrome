FROM markhobson/maven-chrome:jdk-17

ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY build.gradle.kts gradle.properties gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew --console verbose --full-stacktrace shadowJar || return 0
COPY . .
RUN ./gradlew --console verbose --full-stacktrace shadowJar

ENV ARTIFACT_NAME=shadow-1.0-SNAPSHOT-all.jar

ENTRYPOINT java -jar $APP_HOME/build/libs/$ARTIFACT_NAME

