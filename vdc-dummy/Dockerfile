FROM ditas/vdc-base-image
WORKDIR /opt/vdc

ENV MAVEN_VERSION 3.5.2
ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH

RUN wget http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
mv apache-maven-$MAVEN_VERSION /usr/lib/mvn

ADD ./src /opt/vdc/src
ADD ./pom.xml /opt/vdc/pom.xml
ADD ./deploy/application.properties /opt/vdc/src/main/resources/application.properties
RUN mvn install compile

EXPOSE 8080
ENV WORKINGDIR=/opt/vdc
CMD ["mvn","spring-boot:run"]