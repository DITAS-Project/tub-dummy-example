FROM ubuntu:latest
COPY config/blueprint.json /tmp/blueprint.json
COPY config/users.json /tmp/users.json
COPY kcc-linux-amd64 /tmp/kcc
WORKDIR /tmp
CMD ["./kcc","--headless","--address","http://keycloak:8000","--bl","./blueprint.json","--cf","./users.json"] 
