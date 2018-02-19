######################
#  gradle image to   # 
#  build the app     #
######################
FROM gradle as GRADLE
WORKDIR /tmp
COPY src /tmp/src
COPY build.gradle /tmp
COPY settings.gradle /tmp
RUN gradle installDist 
RUN dir


#java runtime environment for the app

FROM openjdk:8-jre-slim
COPY --from=GRADLE  /tmp/build/install/vdcClient /opt/app
WORKDIR /opt/app
CMD ["bin/vdcClient"]
