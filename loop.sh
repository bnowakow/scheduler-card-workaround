#!/bin/bash

# for error "network bridge not found" do sudo ~/code/bash_configs/nas/change-iptables-bridge-docker-settings.sh 
# TODO check if build was successfull
docker compose build

while true; do
    docker compose up
    docker compose down
    date
    sleep 3600
done

