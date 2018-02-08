FROM maven:3-jdk-8-slim as runner
WORKDIR /opt/vdc
ADD ./deploy/vdc-agent /opt/vdc/vdc-agent

COPY ./deploy/VDCMonitor.jar /opt/vdc/VDCMonitor.jar
ADD ./deploy/mnt /opt/vdc/mnt

ADD ./src /opt/vdc/src
ADD ./pom.xml /opt/vdc/pom.xml
ADD ./deploy/application.properties /opt/vdc/src/main/resources/application.properties
ADD ./deploy/run.sh /opt/vdc/run.sh
RUN mvn install compile

ENV elasticURI 192.122.123.123
EXPOSE 8080

RUN apt-get update \
    && apt-get install -y iptraf-ng \
    && rm -rf /var/lib/apt/lists/*

RUN chmod +x /opt/vdc/vdc-agent

CMD ["sh","run.sh"]
