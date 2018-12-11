### 写在最前
**如果想快速部署pigX，请完全参考本篇文档。**    
如果有个性化的修改，请参考本篇运行起来以后，自行修改。循序渐进

### 特别说明 
- flushdb 清空redis ， 2.0 数据结构不同
- 确保你的IDE 已经安装lombok 
- 代码git clone ,不要下载 zip 源码包
- 建议使用 IDEA 2018+ 启动效果会更好
- 内存较小开发机 每个微服务建议设置  -Xms128m -Xmx256m  
![](http://a.pigx.top/20181210181538.png)

### 环境说明
- jdk 1.8 
- mysql 5.7
- redis 3.2+
- node 8.0+
- npm 6.0+
- zookeeper 3.4+


### 一、项目下载

```
git https://www.gitee.wang/pig/pigx.git
```

### 二、配置数据库
- 参数说明
```
版本： mysql5.7+
默认字符集: utf8mb4
默认排序规则: utf8mb4_general_ci
```

- 脚本说明 
2.0 把核心库和业务库分开，建议使用两个库   

```sql
1scheme.sql    建库语句
2pigxx.sql     核心数据库
3pigxx_ac.sql   工作流相关数据库

```


### 三、pig配置修改   （建议批量替换）

- redis 修改  
pigx/pigx-config/src/main/resources/config/application-dev.yml

```
# redis 相关
spring:
  redis:
    password:
    host: pigx-redis
``` 
- 数据库配置   
pigx/pigx-config/src/main/resources/config/pigx-auth-dev.yml    
pigx/pigx-config/src/main/resources/config/pigx-upms-dev.yml  
pigx/pigx-config/src/main/resources/config/pigx-codegen-dev.yml  
pigx/pigx-config/src/main/resources/config/pigx-daemon-dev.yml  
pigx/pigx-config/src/main/resources/config/pigx-activiti-dev.yml  

```
# 数据源
spring:
  datasource:
    username: root
    password: lengleng
    url: jdbc:mysql://pigx-mysql:3306/pigxx?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai
```

### 四、配置本地hosts 建议使用 switchhost）

修改本地hosts文件中存在以下片段


```
# 本地测试环境  
127.0.0.1   pigx-mysql
127.0.0.1	pigx-zookeeper
127.0.0.1	pigx-redis
127.0.0.1	pigx-gateway
127.0.0.1	pigx-eureka
```
### 五、启动顺序（基础模块） 
```java
1. PigxEurekaApplication   
2. PigxConfigApplication  
3. PigxGatewayApplication  
4. PigxAuthApplication 
5. PigxAdminApplication  
```

### 六、启动前端

```
git clone https://www.gitee.wang/pig/pigx-ui.git

# 安装cnpm 镜像
npm run pre

# 安装依赖
npm install

# 启动
npm run dev
```


