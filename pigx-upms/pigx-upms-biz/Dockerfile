FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN mkdir -p /pigx/bin/com.pig4cloud/pigx-upms

WORKDIR /pigx/bin/com.pig4cloud/pigx-upms

EXPOSE 4000

ADD ./target/pigx-upms-biz.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pigx-upms-biz.jar
