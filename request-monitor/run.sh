#!/bin/bash 
echo "starting monitoring proxy for ${VDC_NAME}"

# create an ingest node in the ES to map the logdata send by filebeat
curl -H 'Content-Type: application/json' -XPUT 'http://elasticsearch:9200/_ingest/pipeline/nginx-pipeline' -d@pipeline.json

# subsitute the env set in the docker-compose file
envsubst '${OPENTRACING}' < /nginx.conf > /etc/nginx/nginx.conf
envsubst '${VDC_NAME}' < /etc/filebeat/filebeat.yml > /etc/filebeat/filebeat.yml

#start filebeat and nginx
exec  service filebeat start & nginx -g "daemon off;" 
