FROM maven:3-jdk-8-slim

ADD ./src /opt/vdc/src
ADD ./deploy/application.properties /opt/vdc/src/main/resources/application.properties
ADD ./pom.xml /opt/vdc/pom.xml
ADD ./deploy/VDCMonitor.jar /opt/vdc/VDCMonitor.jar
ADD ./deploy/run.sh /opt/vdc/run.sh

WORKDIR /opt/vdc

ENV elasticURI 192.122.123.123
EXPOSE 8080

RUN apt-get update \
    && apt-get install -y iptraf-ng \
    && rm -rf /var/lib/apt/lists/*


#CMD [ "mvn", "spring-boot:run" ]
CMD ["sh","run.sh"]
