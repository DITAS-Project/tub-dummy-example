FROM golang:1.10.1-alpine AS vdc-traffic
RUN apk update && apk add git && \
    go get -u github.com/golang/dep/cmd/dep && \
    go get github.com/DITAS-Project/VDC-Throughput-Agent && \
    cd src/github.com/DITAS-Project/VDC-Throughput-Agent && \
    dep ensure && \
    CGO_ENABLED=0 go build -a --installsuffix cgo --ldflags="-s" -o vdc-traffic

FROM golang:1.10.1 AS build
ENV SOURCEDIR=/go/src/github.com/DITAS-Project/VDC-DUMMY
RUN mkdir -p ${SOURCEDIR}
WORKDIR ${SOURCEDIR}
COPY . .
RUN CGO_ENABLED=0 go build -a --installsuffix cgo --ldflags="-s" -o dummy

FROM alpine:3.8

COPY --from=vdc-traffic /go/src/github.com/DITAS-Project/VDC-Throughput-Agent/vdc-traffic /opt/monitoring/vdc-traffic

COPY --from=build /go/src/github.com/DITAS-Project/VDC-DUMMY/dummy /opt/dummy

ENV BUILD_DEPS="gettext alpine-sdk git autoconf automake libtool"  \
    RUNTIME_DEPS="libintl libpcap-dev ncurses-dev "

# add nassesary command line tools
RUN  apk add --update $RUNTIME_DEPS && \
    apk add --virtual build_deps $BUILD_DEPS   \
    && cd /tmp && git clone https://github.com/dleonard0/pktstat.git \
        && cd pktstat \
        && libtoolize --force &&  aclocal && autoheader && automake --force-missing --add-missing && autoconf && ./configure \
        && make install && cd ../ rm -rf pktstat \
        && cp /usr/bin/envsubst /usr/local/bin/envsubst  \
    && apk del build_deps \
    && rm -rf /var/cache/apk/*

COPY run.sh /run.sh

EXPOSE 80

CMD [ "sh","/run.sh" ]