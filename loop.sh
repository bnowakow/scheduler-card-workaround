#!/bin/bash -x

git pull

# for error "network bridge not found" do sudo ~/code/bash_configs/nas/change-iptables-bridge-docker-settings.sh
# TODO check if build was successfull
# docker compose build
# above fails on nas so as workadound we're building below on laptop
# https://www.docker.com/blog/multi-arch-build-and-images-the-simple-way/
# docker buildx create --use
#docker buildx build --push --platform linux/amd64 --tag bnowakow/scheduler-card-thermostat-workaround:latest .


while true; do

  for browser in chrome firefox ; do

    echo browser=$browser
    cp Dockerfile.$browser Dockerfile
    cp src/main/resources/home-assistant.properties.$browser src/main/resources/home-assistant.properties
    # TODO do separate image for chrome and firefox and run them to save time to not build on each iteration
    docker buildx build --platform linux/amd64 --tag bnowakow/scheduler-card-thermostat-workaround:latest .
    timeout 600 docker compose up
    docker compose down
    date
    rm Dockerfile src/main/resources/home-assistant.properties
  done
  sleep 30m
done

