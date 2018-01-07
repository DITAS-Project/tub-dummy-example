#!/bin/bash 

logstash-6.1.1/bin/logstash -f /etc/logstash/conf.d/logstash.conf
dir /var/log/nginx/

nginx -g "daemon off;"
service nginx restart

#echo hallo
