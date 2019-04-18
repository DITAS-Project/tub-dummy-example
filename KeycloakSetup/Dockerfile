FROM ubuntu:latest
RUN apt update && apt install -y curl
COPY config/blueprint.json /tmp/blueprint.json
COPY config/users.json /tmp/users.json
COPY kcc-linux-amd64 /tmp/kcc
COPY setup.sh /tmp/setup.sh
WORKDIR /tmp
CMD ["sh","./setup.sh"]
#CMD ["./kcc","--headless","--address","http://keycloak:8000","--bl","./blueprint.json","--cf","./users.json"] 
