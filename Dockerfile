FROM ubuntu:16.04
MAINTAINER James Hagborg <jameshagborg@gmail.com>
CMD bash

RUN apt-get update
RUN apt-get -y install openjdk-8-jdk openjfx xvfb