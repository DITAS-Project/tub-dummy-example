#!/bin/sh
set -e

echo "Starting the monitoring services"
cd /opt/monitoring
exec ./vdc-traffic &

cd /opt

exec ./dummy