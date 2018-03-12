FROM ditas/vdc_base_image:latest
WORKDIR /opt/vdc

RUN  apt-get update &&  apt-get install -y maven openjdk-8-jdk

ADD ./src /opt/vdc/src
ADD ./pom.xml /opt/vdc/pom.xml
ADD ./deploy/application.properties /opt/vdc/src/main/resources/application.properties
ADD ./deploy/run.sh /opt/vdc/run.sh
RUN mvn install compile

EXPOSE 8080


CMD ["mvn","spring-boot:run"]
