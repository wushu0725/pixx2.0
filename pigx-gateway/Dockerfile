FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN mkdir -p /pigx/bin/com.pig4cloud/pigx-gateway

WORKDIR /pigx/bin/com.pig4cloud/pigx-gateway

EXPOSE 1025

ADD ./pigx-gateway/target/pigx-gateway.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pigx-gateway.jar
