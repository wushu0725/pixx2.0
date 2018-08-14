FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN mkdir -p /pigx/bin/com.pig4cloud/pigx-codegen

WORKDIR /pigx/bin/com.pig4cloud/pigx-codegen

EXPOSE 5001

ADD ./pigx-visual/pigx-codegen/target/pigx-codegen.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar pigx-codegen.jar
