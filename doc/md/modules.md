## 模块
``` lua
pigx
├── pigx-ui -- 前端工程[8000]
├── pigx-auth -- 授权服务提供[3000]
├── pigx-common -- 系统公共模块 
├    ├── pigx-common-core -- 公共工具类核心包
├    ├── pigx-common-cache -- 缓存
├    ├── pigx-common-job -- 定时任务
├    ├── pigx-common-log -- 日志服务
├    └── pigx-common-security -- 安全工具类
├    └── pigx-common-swagger -- Swagger Api文档生成
├    └── pigx-common-transaction -- 分布式事务工具包
├── pigx-config -- 配置中心[8888]
├── pigx-eureka -- 服务注册与发现[8761]
├── pigx-gateway -- Spring Cloud Gateway网关[9999]
├── pigx-upms -- 通用用户权限管理模块
├    └── pigx-upms-api -- 通用用户权限管理系统公共api模块
├    └── pigx-upms-biz -- 通用用户权限管理系统业务处理模块[4000]
└── pigx-visual  -- 图形化模块 
├    ├── pigx-monitor -- Spring Boot Admin监控 [5001]
├    ├── pigx-daemon -- 分布式调度中心[5002]
├    └── pigx-code-gen -- 图形化代码生成[5003]
├    └── pigx-tx-manager -- pigx分布式事务解决方案[5004]
├    └── pigx-activiti -- 工作流模块[5005]
	 
```
