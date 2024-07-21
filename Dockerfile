# https://stackoverflow.com/a/50467205
FROM gradle:jdk17 AS BUILDER

ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY build.gradle.kts gradle.properties gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew --console verbose --full-stacktrace shadowJar || return 0
COPY . .
RUN ./gradlew --console verbose --full-stacktrace shadowJar

FROM debian:bookworm-20231120

ENV ARTIFACT_NAME=shadow-1.0-SNAPSHOT-all.jar
ENV APP_HOME=/app

WORKDIR $APP_HOME

# https://github.com/sunim2022/Jenkins_Docker/blob/9b55a490d3d83590a3eed3b064d73397a42d9de1/selenium-in-docker/Dockerfile
# Install tools.
RUN apt-get update -y  \
    && apt-get install -y wget unzip
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y tzdata

RUN apt-get install -y openjdk-17-jdk

# https://dev.to/sunim2022/run-your-selenium-tests-inside-docker-container-part-1-4b02
# Install Firefox
ARG FIREFOX_VERSION=120.0.1
RUN echo "deb http://deb.debian.org/debian/ unstable main contrib non-free" >> /etc/apt/sources.list.d/debian.list
RUN apt-get update -qqy \
  && apt-get -qqy --no-install-recommends install firefox
#  && rm -rf /var/lib/apt/lists/* /var/cache/apt/*

RUN apt-get -qqy install firefox

#  && wget --no-verbose -O /tmp/firefox.tar.bz2 https://download-installer.cdn.mozilla.net/pub/firefox/releases/$FIREFOX_VERSION/linux-x86_64/en-US/firefox-$FIREFOX_VERSION.tar.bz2 \
#  && apt-get -y purge firefox \
#  && rm -rf /opt/firefox \
#  && tar -C /opt -xjf /tmp/firefox.tar.bz2 \
#  && rm /tmp/firefox.tar.bz2 \
#  && mv /opt/firefox /opt/firefox-$FIREFOX_VERSION \
#  && ln -fs /opt/firefox-$FIREFOX_VERSION/firefox /usr/bin/firefox

# Install GeckoDriver
ARG GECKODRIVER_VERSION=0.34.0
RUN wget --no-verbose -O /tmp/geckodriver.tar.gz https://github.com/mozilla/geckodriver/releases/download/v$GECKODRIVER_VERSION/geckodriver-v$GECKODRIVER_VERSION-linux64.tar.gz \
  && rm -rf /opt/geckodriver \
  && tar -C /opt -zxf /tmp/geckodriver.tar.gz \
  && rm /tmp/geckodriver.tar.gz \
  && mv /opt/geckodriver /opt/geckodriver-$GECKODRIVER_VERSION \
  && chmod 755 /opt/geckodriver-$GECKODRIVER_VERSION \
  && ln -fs /opt/geckodriver-$GECKODRIVER_VERSION /usr/bin/geckodriver

RUN apt-get install curl wget -y

#RUN curl -fSsL https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor | tee /usr/share/keyrings/google-chrome.gpg >> /dev/null \
#    && echo deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main | tee /etc/apt/sources.list.d/google-chrome.list

RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub > linux_signing_key.pub \
    && install -D -o root -g root -m 644 linux_signing_key.pub /etc/apt/keyrings/linux_signing_key.pub

RUN echo "deb [arch=amd64 signed-by=/etc/apt/keyrings/linux_signing_key.pub] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list

RUN apt-get update \
    && apt-get install google-chrome-stable -y

RUN wget https://chromedriver.storage.googleapis.com/114.0.5735.90/chromedriver_linux64.zip \
    && unzip chromedriver_linux64.zip \
    && mv chromedriver /usr/bin/chromedriver \
    && chmod +x /usr/bin/chromedriver

RUN apt-get install -y curl unzip xvfb libxi6 libgconf-2-4

COPY --from=BUILDER $APP_HOME/build/libs/$ARTIFACT_NAME .
ENTRYPOINT xvfb-run java -Dwebdriver.chrome.driver=/usr/bin/chromedriver -jar $ARTIFACT_NAME

