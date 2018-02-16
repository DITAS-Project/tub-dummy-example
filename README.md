# DITAS OSR Use Case
This project aims to simulated testbed (dummy example) of the DITAS runtime environment based on the preliminary use case example given by OSR (hospital san rafael).

All elements of this project are just mock versions of the final runtime (VDC, app, databases) and can be used to simulate other components, e.g., monitoring.

This example represents a snapshot of an already deployed VDC with two attached databases and one application connected ot the vdc.

![Architecture Image](https://raw.githubusercontent.com/DITAS-Project/tub-dummy-example/master/Architecture.png)
### Usage:
All components can be build and run via docker-compose. The current version was tested on Windows and OSX with Docker version 17.12 and docker-compose version 1.18.0.
Currently there are four different compose files:
* ```docker-compose.demo.yml```- starting 8 containers with a Java implementation of the VDC (containers: mysql, cassandra, elasticsearch, vdc, dockbeat, request-monitor, zipkin, kibana)
* ```docker-compose.yml```- starting 7 containers with a Node-Red implementation of the VDC (containers: mysql, cassandra, elasticsearch, vdc, dockbeat, request-monitor, zipkin)
* ```docker-compose.java.yml```- starting 6 containers with a Java implementation of the VDC (containers: mysql, cassandra, elasticsearch, vdc, request-monitor, zipkin)
* ```docker-compose.dev.yml```- starting 5 containers with no implementation of the VDC (containers: mysql, cassandra, elasticsearch, request-monitor, zipkin)

### System Requirmentes:
In order to run our example your docker enviroment needs to have at least 6 GB of system memory. If you are running Docker for OSX or Windows, make sure to change the setting of the docker vm, otherwise some containers might not start or terminate unexpectily. 

We recommend using docker version:
```
$docker --version
Docker version 17.12.0-ce, build c97c6d6
``` 
or higher, and docker compose version:
```
$docker-compose.exe --version
docker-compose version 1.18.0, build 8dd22a96
``` 
or higher.

### Running the Example:
To start the containers for the above choosen scenario use the following command: 

```docker-compose -f <YOUR-COMPOSE-FILE-TO-CHOOSE> up ``` 

i.e. for the demo scenario: ```docker-compose -f docker-compose.demo.yml up ```

This will also build the necessary parts for the containers before starting them. 

##### UPDATE
If you want to update the sources from github:
 - stop the containers first (Ctrl + C)
 - pull the new sources from github (```git pull``)
 - remove the containers (```docker-compose rm```)
 - rebuild and start the containers (```docker-compose -f <YOUR-COMPOSE-FILE-TO-CHOOSE> up --build``)

### Available Components:
* VDC /vdc
    * MySQL Database with dummy data
    * Cassandra Database with dummy data
* APP Client /app
* Request-Monitor

### Available Routes:
This example has two different VDC's that can be deployed for different testing purposes. On is the a Mock VDC written in JAVA and one is written in NodeRed.
#### JAVA-VDC API:
| Method | Path               | Details                      | Returns |
| :--- | :---| --- | --- |
| GET    | /patient/{ssn}     | ssn: number betwenn 0 and 50 | Patient |
| GET    | /exam/{ssn}        | ssn: number betwenn 0 and 50 | \[Exam,...\] |
| GET    | /find?...          | minAge=0-100, maxAge=0-100,startDate=yyyy/MM/dd,endDate=yyyy/MM/dd, gender=(m/f)|  \[Exam,...\] |

#### Node-Red-VDC API:
| Method | Path               | Details                      | Returns |
| :--- | :---| --- | --- |
| GET    | /patient/{ssn}     | ssn: number betwenn 0 and 50 | Patient |

### Environment Variables

* request-monitor
    * VDC_NAME - set the name of the connected VDC, it is used for indexing in the ES 
    * OPENTRACING - controls if nginx uses opentracing or not, default is off
* zipkin
    * COLLECTOR_SAMPLE_RATE - determines the percentage of traces collected by the zipkin database, can be set between 0.0 an 1.0 
    * STORAGE_TYPE - defines the Database type in which the traces are stored, possible types are : mem, mysql, cassandra, elasticsearch
    * ES_HOSTS - sets the elasticsearch host, only used when the storage type is elasticsearch
    
