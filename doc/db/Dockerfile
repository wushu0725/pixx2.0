FROM mysql:5.7
MAINTAINER lengleng(wangiegie@gmail.com)
COPY 1schema.sql /docker-entrypoint-initdb.d
COPY 2pigxx.sql /docker-entrypoint-initdb.d
COPY 3pigxx_ac.sql /docker-entrypoint-initdb.d