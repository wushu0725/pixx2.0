FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN mkdir -p /pigx-auth

WORKDIR /pigx-auth

EXPOSE 3000

ADD ./pigx-auth/target/pigx-auth.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pigx-auth.jar
