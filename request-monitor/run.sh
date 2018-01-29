#!/bin/bash 
echo "starting monitoring proxy for ${VDC_NAME}"
envsubst '${OPENTRACING}' < /nginx.conf > /etc/nginx/nginx.conf
exec  service filebeat start & nginx -g "daemon off;" 
