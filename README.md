# DITAS OSR Use Case
This project aims to simulated testbed (dummy example) of the DITAS runtime environment based on the preliminary use case example given by OSR (hospital san rafael).

All elements of this project are just mock versions of the final runtime (VDC, app, databases) and can be used to simulate other components, e.g., monitoring.

This example represents a snapshot of an already deployed VDC with two attached databases and one application connected ot the vdc.

![Architecture Image](https://raw.githubusercontent.com/DITAS-Project/tub-dummy-example/master/Architecture.png)
### Usage:
use ```docker-compose -f docker-compose.java.yml up ``` to start the vdc and its databases.

use ```docker-compose up ``` to start the node-red vdc and its databases.
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
    
