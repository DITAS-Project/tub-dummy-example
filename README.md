# DITAS OSR Use Case
This project aims to simulated testbed (dummy example) of the DITAS runtime environment based on the preliminary use case example given by OSR (hospital san rafael).

All elements of this project are just mock versions of the final runtime (VDC, app, databases) and can be used to simulate other components, e.g., monitoring.

This example represents a snapshot of an already deployed VDC with two attached databases and one application connected ot the vdc.

### Usage:
use ```docker-compose up ``` to start the vdc and its databases.

### Available Components:
* VDC /vdc
    * MySQL Database with dummy data
    * Cassandra Database with dummy data
* APP Client /app
