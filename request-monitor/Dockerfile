FROM nginx:latest

RUN apt-get update && apt-get install -y wget apt-utils  && apt-get install --no-install-recommends --no-install-suggests -y \
    gnupg1\
    bzip2 \
		unzip \
		xz-utils
	 

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

# add a simple script that can auto-detect the appropriate JAVA_HOME value
# based on whether the JDK or only the JRE is installed
RUN { \
		echo '#!/bin/sh'; \
		echo 'set -e'; \
		echo; \
		echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; \
	} > /usr/local/bin/docker-java-home \
	&& chmod +x /usr/local/bin/docker-java-home

# do some fancy footwork to create a JAVA_HOME that's cross-architecture-safe
RUN ln -svT "/usr/lib/jvm/java-8-openjdk-$(dpkg --print-architecture)" /docker-java-home
ENV JAVA_HOME /docker-java-home/jre

ENV JAVA_VERSION 8u151
ENV JAVA_DEBIAN_VERSION 8u151-b12-1~deb9u1

# see https://bugs.debian.org/775775
# and https://github.com/docker-library/java/issues/19#issuecomment-70546872
ENV CA_CERTIFICATES_JAVA_VERSION 20170531+nmu1

RUN set -ex; \
	\
# deal with slim variants not having man page directories (which causes "update-alternatives" to fail)
	if [ ! -d /usr/share/man/man1 ]; then \
		mkdir -p /usr/share/man/man1; \
	fi; \
	\
	apt-get update; \
	apt-get install -y \
		openjdk-8-jre-headless="$JAVA_DEBIAN_VERSION" \
		ca-certificates-java="$CA_CERTIFICATES_JAVA_VERSION" \
	; \
	\
# verify that "docker-java-home" returns what we expect
	[ "$(readlink -f "$JAVA_HOME")" = "$(docker-java-home)" ]; \
	\
# update-alternatives so that future installs of other OpenJDK versions don't change /usr/bin/java
	update-alternatives --get-selections | awk -v home="$(readlink -f "$JAVA_HOME")" 'index($3, home) == 1 { $2 = "manual"; print | "update-alternatives --set-selections" }'; \
# ... and verify that it actually worked for one of the alternatives we care about
	update-alternatives --query java | grep -q 'Status: manual'

# see CA_CERTIFICATES_JAVA_VERSION notes above
RUN /var/lib/dpkg/info/ca-certificates-java.postinst configure && mkdir /var/log/nginx/customlog && apt-get update && apt-get install -y --no-install-recommends curl \

#get filebeat image
&& curl -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-6.1.2-amd64.deb \
&& dpkg -i filebeat-6.1.2-amd64.deb
 
# && wget -q -P / https://artifacts.elastic.co/downloads/logstash/logstash-6.1.1.tar.gz && tar -zxf logstash-6.1.1.tar.gz --directory /



RUN set -x \
# install nginx-opentracing package dependencies
  && apt-get install --no-install-recommends --no-install-suggests -y \
              libcurl4-openssl-dev \
              libprotobuf-dev \
              protobuf-compiler \
# save list of currently-installed packages so build dependencies can be cleanly removed later
	&& savedAptMark="$(apt-mark showmanual)" \
# new directory for storing sources and .deb files
	&& tempDir="$(mktemp -d)" \
	&& chmod 777 "$tempDir" \
			\
# (777 to ensure APT's "_apt" user can access it too)
## Build OpenTracing package and tracers
  && apt-get install --no-install-recommends --no-install-suggests -y \
              build-essential \
              cmake \
              git \
              ca-certificates \
              pkg-config \
              wget \
              golang \
              libz-dev \
              automake \
              autogen \
              autoconf \
              libtool \
# reset apt-mark's "manual" list so that "purge --auto-remove" will remove all build dependencies
# (which is done after we install the built packages so we don't have to redownload any overlapping dependencies)
	&& apt-mark showmanual | xargs apt-mark auto > /dev/null \
	&& { [ -z "$savedAptMark" ] || apt-mark manual $savedAptMark; } \
	\
  && cd "$tempDir" \
## Build opentracing-cpp
  && git clone https://github.com/opentracing/opentracing-cpp.git \
  && cd opentracing-cpp \
  && mkdir .build && cd .build \
  && cmake -DCMAKE_BUILD_TYPE=Release \
           -DBUILD_TESTING=OFF .. \
  && make && make install \
  && cd "$tempDir" \
## Build zipkin-cpp-opentracing
  && apt-get --no-install-recommends --no-install-suggests -y install libcurl4-gnutls-dev \
  && git clone https://github.com/rnburn/zipkin-cpp-opentracing.git \
  && cd zipkin-cpp-opentracing \
  && mkdir .build && cd .build \
  && cmake -DBUILD_SHARED_LIBS=1 -DCMAKE_BUILD_TYPE=Release -DBUILD_TESTING=OFF .. \
  && make && make install \
  && cd "$tempDir" \
## Build yaml-cpp
  && git clone https://github.com/jbeder/yaml-cpp.git \
  && cd yaml-cpp \
  && mkdir .build && cd .build \
  && cmake -DCMAKE_BUILD_TYPE=Release \
           -DBUILD_TESTING=OFF .. \
  && make && make install \
  && cd "$tempDir" \
### Build Jaeger cpp-client
# && git clone https://github.com/jaegertracing/cpp-client.git jaeger-cpp-client \
# && apt-get update \
# && apt-get --no-install-recommends --no-install-suggests -y install yaml-cpp \
# && cd jaeger-cpp-client \
# && mkdir .build && cd .build \
# && cmake -DCMAKE_BUILD_TYPE=Release \
#          -DBUILD_TESTING=OFF \
#          -DJAEGERTRACING_WITH_YAML_CPP=OFF .. \
# && make && make install \
# && export HUNTER_INSTALL_DIR=$(cat _3rdParty/Hunter/install-root-dir) \
# && cd "$tempDir" \
### Build gRPC
 && git clone -b v1.4.x https://github.com/grpc/grpc \
 && cd grpc \
 && git submodule update --init \
 && make HAS_SYSTEM_PROTOBUF=false && make install \
 && make && make install \
 && cd "$tempDir" \
### Build lightstep-tracer-cpp
# && git clone https://github.com/lightstep/lightstep-tracer-cpp.git \
# && cd lightstep-tracer-cpp \
# && mkdir .build && cd .build \
# && cmake -DBUILD_SHARED_LIBS=1 -DCMAKE_BUILD_TYPE=Release -DBUILD_TESTING=OFF .. \
# && make && make install \
# && cd "$tempDir" \
### Build nginx-opentracing modules
  && git clone https://github.com/opentracing-contrib/nginx-opentracing.git \
  && NGINX_VERSION=`nginx -v 2>&1` && NGINX_VERSION=${NGINX_VERSION#*nginx/} \
  && echo "deb-src http://nginx.org/packages/mainline/debian/ stretch nginx" >> /etc/apt/sources.list \
  && apt-get update \
  && apt-get build-dep -y nginx=${NGINX_VERSION} \
  && wget -O nginx-release-${NGINX_VERSION}.tar.gz https://github.com/nginx/nginx/archive/release-${NGINX_VERSION}.tar.gz \
  && tar zxf nginx-release-${NGINX_VERSION}.tar.gz \
  && cd nginx-release-${NGINX_VERSION} \
  && NGINX_MODULES_PATH=$(nginx -V 2>&1 | grep -oP "modules-path=\K[^\s]*") \
  && auto/configure \
        --with-compat \
        --add-dynamic-module=${tempDir}/nginx-opentracing/opentracing \
        --add-dynamic-module=${tempDir}/nginx-opentracing/zipkin \
 #       --add-dynamic-module=${tempDir}/nginx-opentracing/lightstep \
  #      --add-dynamic-module=${tempDir}/nginx-opentracing/jaeger \
        --with-cc-opt="-I$HUNTER_INSTALL_DIR/include" \
        --with-ld-opt="-L$HUNTER_INSTALL_DIR/lib" \
  && make modules \
  && cp objs/ngx_http_opentracing_module.so $NGINX_MODULES_PATH/ \
  && cp objs/ngx_http_zipkin_module.so $NGINX_MODULES_PATH/ \
#  && cp objs/ngx_http_lightstep_module.so $NGINX_MODULES_PATH/ \
#  && cp objs/ngx_http_jaeger_module.so $NGINX_MODULES_PATH/ \
	# if we have leftovers from building, let's purge them (including extra, unnecessary build deps)
  && rm -rf $HOME/.hunter \
  && if [ -n "$tempDir" ]; then \
  	apt-get purge -y --auto-remove \
  	&& rm -rf "$tempDir" /etc/apt/sources.list.d/temp.list; \
  fi \
#  && rm -rf logstash-6.1.1.tar.gz 
  && rm -rf /var/lib/apt/lists/* 

# add script to run logstash and nginx
ADD run.sh /run.sh
# add custom logstash config to directory
#ADD logstash.conf  /etc/logstash/conf.d/
# add custom nginx config
ADD nginx.conf /nginx.conf
#setup custom filebeat config file
ADD filebeat.yml /etc/filebeat/

ENV OPENTRACING off 

STOPSIGNAL SIGTERM

EXPOSE 8000
CMD ["sh", "/run.sh"]

