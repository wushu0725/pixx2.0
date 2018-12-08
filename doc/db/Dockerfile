FROM mysql:5.7

MAINTAINER lengleng(wangiegie@gmail.com)

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

COPY ./doc/db/1schema.sql /docker-entrypoint-initdb.d

COPY ./doc/db/2pigxx.sql /docker-entrypoint-initdb.d

COPY ./doc/db/3pigxx_ac.sql /docker-entrypoint-initdb.d