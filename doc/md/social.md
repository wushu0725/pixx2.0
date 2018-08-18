社交登录

源码提供微信登录测试参数，欢迎群里同学共享QQ 等其他社交的测试参数

### 修改host

```shell
127.0.0.1   daoweicloud.com
```

###  修改前端端口

1. 修改为80

![](http://obq1lvsd9.bkt.clouddn.com/20180818115000.png)

2. 使用mac 的同学，nginx 代理一下

```shell
   location / {
      proxy_pass http://localhost:8000;
    }
```

### 浏览器访问

```
http://daoweicloud.com
```

1. 首先使用账号密码登录系统绑定微信

![](http://obq1lvsd9.bkt.clouddn.com/20180818120253.png)



![](http://obq1lvsd9.bkt.clouddn.com/20180818120339.png)

2. 绑定后使用退出，使用社交等 选择微信登录即可。





### QA：

1. 选择社交登录 提示回调地址错误。 请使用 http://daoweicloud.com 访问

   ![](http://obq1lvsd9.bkt.clouddn.com/20180818120543.png)

1. 选择微信登录提示： 未绑定账户，请使用密码登录后绑定，需要先登录后绑定微信，才可以使用微信

![](http://obq1lvsd9.bkt.clouddn.com/20180818120045.png)
