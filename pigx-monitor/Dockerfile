FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN mkdir -p /pigx/bin/com.pig4cloud/pigx-monitor

WORKDIR /pigx/bin/com.pig4cloud/pigx-monitor

EXPOSE 1025

ADD ./pigx-monitor/target/pigx-monitor.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pigx-monitor.jar
