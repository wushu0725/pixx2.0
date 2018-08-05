## 代码生成使用

v 1.4.0 提供了代码图形化生成功能。

### 功能支持

##### 前端：

> api.js

> crud.js
>
> index.vue

##### 后端

> entity
>
> mapper
>
> service
>
> Controller

### 升级要点

>  升级pigx-ui pigx 到 **v 1.4.0**版本
>
> 更新数据库脚本，doc/pigx.sql

### 开始使用

##### 项目启动

1.  启动 pigx-eureka、pigx-config、pigx-gateway、pigx-auth、pigx-upms-biz
2. 启动**PigxCodeGenApplication**
3. 启动**pigx-ui**

##### 界面操作

1. 代码生成模块

![](http://obq1lvsd9.bkt.clouddn.com/20180803083802.png)

2. 选择要生成的表

   > 以下为空则从**pigx-codegen/generator.properties** 获取

   ![](http://obq1lvsd9.bkt.clouddn.com/20180803084058.png)

3. 解压下载的**pigx_code_gen.zip**

   > 生成代码结构，安装前后端 maven 、vue-cli 目录生成，可以覆盖到指定业务模块

   ![](http://obq1lvsd9.bkt.clouddn.com/20180803084524.png)

4. **重点讲解生成的SQL使用**

   > 生成的SQL脚本，不要直接执行，完善 菜单、按钮的菜单ID 的层级
   >
   > PS: 为什么菜单ID 不自动生成呢？
   >
   > - [x] ​	通过自己录入保证，数据库展示层级。
   >
   > - [x] ​	比如 父菜单 ID 为 1,子菜单的ID 则为 11，按钮ID 为 12，13，14

   ```lua
   1
   ├── 11
   ├── 12
   ├── 13
   ├── 14
   ```

![](http://obq1lvsd9.bkt.clouddn.com/20180803084905.png)

5. **配置路由**
前端 index.js
```js
          '/code': {
              target: baseUrl,
              changeOrigin: true,
              pathRewrite: {
                  '^/code': '/code'
              }
          },
```

##### 最后给角色分配，你新增的菜单和按钮喔
