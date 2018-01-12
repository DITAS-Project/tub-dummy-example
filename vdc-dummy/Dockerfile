FROM maven:3-jdk-8-slim

ADD ./src /opt/vdc/src
ADD ./deploy/application.properties /opt/vdc/src/main/resources/application.properties
ADD ./pom.xml /opt/vdc/pom.xml

WORKDIR /opt/vdc
EXPOSE 8080
CMD [ "mvn", "spring-boot:run" ]