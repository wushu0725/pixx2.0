# Elastic-Job作业监控平台搭建

## 获取源码
首先是上github下载elastic-job的源码，目前最新的地址如下：  https://github.com/elasticjob/elastic-job-lite

下载前要注意源码需要和你使用的elastic-job版本一致。

![20189416555](http://p7sojn4oj.bkt.clouddn.com/20189416555.png)


1.  在你喜欢的终端应用里执行以下命令:

```bash
git clone https://github.com/elasticjob/elastic-job-lite.git
```

2.  下载完成以后cd到仓库

3.  以PigX所使用的2.1.5版本为例，执行以下命令切换到2.1.5标签的代码:

```bash
git checkout 2.1.5
```

## 编译源码

这个过程很简单，执行命令：

```bash
mvn package -DskipTests
```

## 开始使用

1.  进入/elastic-job-lite/elastic-job-lite-console/target文件夹，有一个叫做```elastic-job-lite-console-2.1.5.tar.gz```的文件就是我们需要的了。

2. 解压之，执行bin目录下的start脚本，这货默认运行在```8899```端口，以用户名:```root```和密码：```root```进行登录

3. 知道大家可能看不懂，右上角切换一波语言吧！

4. 以pigx最新的1.6.0的稳定版代码为例，查看配置中心下的```pigx-daemon-dev.yml```文件,与我们目前相关的配置文件如下：

```yml
## 定时任务
spring:
  # 保存定时任务的数据源
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
    url: jdbc:mysql://mysql:3306/pigxx?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai
  elasticjob:
    # 分布式任务协调依赖zookeeper
    zookeeper:
      server-lists: 127.0.0.1:2181
      namespace: pigx-daemon
```

通过全局配置-注册中心配置,填入以下参数:

![201894174025](http://p7sojn4oj.bkt.clouddn.com/201894174025.png)

然后修改全局配置-事件追踪数据源配置,填入以下参数:

![20189417457](http://p7sojn4oj.bkt.clouddn.com/20189417457.png)

在操作处点击连接，出现已连的字样表示连接成功。

![201894174634](http://p7sojn4oj.bkt.clouddn.com/201894174634.png)

最终效果如下:

![20189418110](http://p7sojn4oj.bkt.clouddn.com/20189418110.png)
