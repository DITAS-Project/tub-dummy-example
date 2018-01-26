FROM maven:3-jdk-8-slim as builder

WORKDIR /opt/vdc
ADD ./src /opt/vdc/src
ADD ./pom.xml /opt/vdc/pom.xml
ADD ./deploy/application.properties /opt/vdc/src/main/resources/application.properties
ADD ./deploy/vdc-agent /opt/vdc/vdc-agent
ADD ./deploy/run.sh /opt/vdc/run.sh
RUN mvn install compile

EXPOSE 8080
CMD [ "sh", "run.sh" ]