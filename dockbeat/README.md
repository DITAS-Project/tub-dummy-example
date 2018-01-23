# Readme
Dockbeat container is accessing the docker.sock from the host system to check health status of the other running containers. It checks different metrics e.g. CPU usage, memory usage, I/O of container etc. and writes them into a Elasticsearch.

### Usage:
use ```sudo docker-compose up ``` to start the Dockbeat container with the other containers.
```sudo```is necessary because the docker.sock socket from the host system is exposed to the container.

Checking how much ressources a certain container uses is done by using the container name in the queries.


