### 写在最前
**如果想快速部署pigX，请完全参考本篇文档，如果有个性化的修改（例如：oauth2配置、token 个性化需求），请参考本篇运行起来以后，自行修改。循序渐进**

### 架构图
![image](http://p0hpm86wj.bkt.clouddn.com/PIGX.png)
### 一、项目下载

```
git https://www.gitee.wang/pig/pigx.git
```

### 二、配置数据库

```
版本： mysql5.7+

```
基本参数如下：
![image](https://gitee.com/uploads/images/2018/0610/142651_72fad599_410595.png)


### 三、pig配置修改
pigx/pigx-config/src/main/resources/config/application-dev.yml

```
# redis 相关
spring:
  redis:
    password:
    host: localhost
```
pigx/pigx-config/src/main/resources/config/pigx-auth-dev.yml  
pigx/pigx-config/src/main/resources/config/pigx-upms-dev.yml

```
# 数据源
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.jdbc.Driver
    username: root
    password: lengleng
    url: jdbc:mysql://127.0.0.1:3306/pigx?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
```


### 四、pigx-ui：

```
git clone https://www.gitee.wang/pig/pigx-ui.git
npm run dev
```
请确保启动顺序
1.eureka   
2.config  
3.gateway  
4.auth  
5.upms  
