FROM maven:3-jdk-8-slim

ADD ./src /opt/vdc/src
ADD ./deploy/application.properties /opt/vdc/src/main/resources/application.properties
ADD ./pom.xml /opt/vdc/pom.xml
ADD ./deploy/vdc-agent /opt/vdc/vdc-agent
ADD ./deploy/run.sh /opt/vdc/run.sh

WORKDIR /opt/vdc
RUN chmod +x vdc-agent & mvn install compile
EXPOSE 8080
CMD [ "sh", "run.sh" ]