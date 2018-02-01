#!/bin/bash 
echo "starting monitoring proxy for ${VDC_NAME}"
curl -H 'Content-Type: application/json' -XPUT 'http://elasticsearch:9200/_ingest/pipeline/nginx-pipeline' -d@pipeline.json
envsubst '${OPENTRACING}' < /nginx.conf > /etc/nginx/nginx.conf
exec  service filebeat start & nginx -g "daemon off;" 