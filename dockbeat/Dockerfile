#golang:1.7
FROM golang:1.7
MAINTAINER Ingensi labs <contact@ingensi.com>

# install pyyaml
RUN cd /tmp && wget http://pyyaml.org/download/pyyaml/PyYAML-3.11.tar.gz && tar -zxvf PyYAML-3.11.tar.gz
RUN cd /tmp/PyYAML-3.11 && python setup.py install
# install glide
RUN go get github.com/Masterminds/glide

RUN go get github.com/ingensi/dockbeat
#COPY . $GOPATH/src/github.com/ingensi/dockbeat
RUN cd $GOPATH/src/github.com/ingensi/dockbeat && make && make
ADD dockbeat.yml /etc/dockbeat/

RUN mkdir -p /etc/dockbeat/ \
    && cp $GOPATH/src/github.com/ingensi/dockbeat/dockbeat /usr/local/bin/dockbeat
    #&& cp $GOPATH/src/github.com/ingensi/dockbeat/dockbeat.yml /etc/dockbeat/dockbeat.yml

WORKDIR $GOPATH/src/github.com/ingensi/dockbeat/
ENTRYPOINT ["./dockbeat"]

CMD ["-c", "dockbeat.yml", "-v", "-e" ]