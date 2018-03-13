FROM ubuntu:16.04
MAINTAINER James Hagborg <jameshagborg@gmail.com>
CMD bash

RUN apt-get update && apt-get -y install git