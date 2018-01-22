#!/bin/sh 
echo "starting monitoring proxy for ${VDC_NAME}"
exec /logstash-6.1.1/bin/logstash -f "/etc/logstash/conf.d/logstash.conf" & /usr/sbin/nginx -g "daemon off;"