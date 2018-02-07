FROM nodered/node-red-docker

COPY flow.json /data/flow.json
COPY flow_cred.json /data/flow_cred.json
COPY settings.js /data/settings.js
COPY vdc-agent vdc-agent
COPY run.sh run.sh
ENV FLOWS /data/flow.json
ENV PORT 8080

RUN npm install node-red-node-mysql \
    && npm install node-red-contrib-cassandra

CMD [ "sh","./run.sh" ]
