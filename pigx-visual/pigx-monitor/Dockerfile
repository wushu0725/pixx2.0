FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN mkdir -p /pigx/bin/com.pig4cloud/pigx-daemon

WORKDIR /pigx/bin/com.pig4cloud/pigx-daemon

EXPOSE 5001

ADD ./pigx-daemon/target/pigx-daemon.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pigx-daemon.jar
