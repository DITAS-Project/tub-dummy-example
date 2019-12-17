FROM elasticsearch:6.8.0


#enable security for ES 6.8
ENV discovery.type single-node
ENV xpack.security.enabled true

# Set Password for superuser
RUN elasticsearch-keystore create
RUN echo "ditasmetrics" | elasticsearch-keystore add bootstrap.password --stdin
RUN elasticsearch-users useradd admin -p ditasmetrics -r superuser
