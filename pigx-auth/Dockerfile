FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN mkdir -p /pigx/bin/com.pig4cloud/pigx-auth

WORKDIR /pigx/bin/com.pig4cloud/pigx-auth

EXPOSE 1025

ADD ./pigx-auth/target/pigx-auth.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pigx-auth.jar
