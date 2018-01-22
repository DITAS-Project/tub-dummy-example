#!/bin/bash 

envsubst '${OPENTRACING}' < /nginx.conf > /etc/nginx/nginx.conf
tail /etc/nginx/nginx.conf
exec logstash-6.1.1/bin/logstash -f /etc/logstash/conf.d/logstash.conf & nginx -g "daemon off;"

