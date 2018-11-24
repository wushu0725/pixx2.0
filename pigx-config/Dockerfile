FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN mkdir -p /pigx/bin/com.pig4cloud/pigx-config

WORKDIR /pigx/bin/com.pig4cloud/pigx-config

EXPOSE 8888

ADD ./pigx-config/target/pigx-config.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pigx-config.jar
