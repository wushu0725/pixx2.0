# 如何在pigx Swagger中OAuth2.0 授权

## 增加客户端

默认对所有终端进行验证码校验，但是swagger 模拟的时候不需要。

- 通过界面的形式

  ![](http://obq1lvsd9.bkt.clouddn.com/20180725132807.png)

- 直接操作**sys_oauth_client_details**表

```sql
INSERT INTO `pigx`.`sys_oauth_client_details` (
	`authorities`,
	`authorized_grant_types`,
	`web_server_redirect_uri`,
	`scope`,
	`additional_information`,
	`autoapprove`,
	`resource_ids`,
	`refresh_token_validity`,
	`client_secret`,
	`client_id`,
	`access_token_validity`
)
VALUES
	(
		NULL,
		'password,refresh_token',
		NULL,
		'server',
		NULL,
		'true',
		NULL,
		NULL,
		'test',
		'test',
		NULL
	);
```

## 过滤指定客户端

pigx-gateway-dev.yml

```yml
# 不校验验证码终端
ignore:
  clients:
    - test
```

## 填写客户端信息

![](http://obq1lvsd9.bkt.clouddn.com/20180725133119.png)

![](http://obq1lvsd9.bkt.clouddn.com/20180725133206.png)