#!/bin/bash 

exec logstash-6.1.1/bin/logstash -f /etc/logstash/conf.d/logstash.conf & nginx -g "daemon off;"



#echo hallo
