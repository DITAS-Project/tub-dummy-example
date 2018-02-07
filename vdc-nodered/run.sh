#!/bin/sh
./vdc-agent -zipkin "http://zipkin:9411/api/v1/spans" -vdc "http://0.0.0.0:8080" &
exec npm start -- --userDir /data