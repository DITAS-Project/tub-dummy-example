#!/bin/bash 

envsubst '${OPENTRACING}' < /nginx.conf > /etc/nginx/nginx.conf
exec  nginx -g "daemon off;" & service filebeat start

