# base image
FROM node:latest

# set working directory
RUN mkdir /usr/src/app
WORKDIR /usr/src/app

COPY . ./
# add `/usr/src/app/node_modules/.bin` to $PATH
ENV PATH /usr/src/app/node_modules/.bin:$PATH




EXPOSE 3000

CMD ["sh", "start.sh"]
