#!/bin/bash -x

git pull

# TODO for linux check if firefox is installed via snap, if it is remove it and install it from mozzilla ppa https://askubuntu.com/a/1403204

# for error "network bridge not found" do sudo ~/code/bash_configs/nas/change-iptables-bridge-docker-settings.sh
# TODO check if build was successfull
# docker compose build
# above fails on nas so as workadound we're building below on laptop
# https://www.docker.com/blog/multi-arch-build-and-images-the-simple-way/
# docker buildx create --use
#docker buildx build --push --platform linux/amd64 --tag bnowakow/scheduler-card-thermostat-workaround:latest .

# TODO read from optional cli argument $1
intelij_manual_execution=true

while true; do

#  for browser in chrome firefox; do
  for browser in chrome; do

    echo browser=$browser
    cp Dockerfile.$browser Dockerfile
    cp src/main/resources/home-assistant.properties.$browser src/main/resources/home-assistant.properties

    if [ "$intelij_manual_execution" == "true" ]; then
      ./gradlew --console verbose --full-stacktrace shadowJar
      # TODO do a function that does timelimit or timeout or none if the binary is present
        timelimit -t 600 java -jar build/libs/shadow-1.0-SNAPSHOT-all.jar
    else
      # TODO do separate image for chrome and firefox and run them to save time to not build on each iteration
      docker buildx build --platform linux/amd64 --tag bnowakow/scheduler-card-thermostat-workaround:latest .
#      timeout 600 docker compose up
      docker compose up
      docker compose down
    fi
    date
    rm Dockerfile src/main/resources/home-assistant.properties
  done
  sleep 30m
done

