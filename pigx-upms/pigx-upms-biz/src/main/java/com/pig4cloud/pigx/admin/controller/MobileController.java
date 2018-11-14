/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.service.MobileService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author lengleng
 * @date 2018/11/14
 * <p>
 * 手机验证码
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mobile")
@Api(value = "mobile",description = "手机管理模块")
public class MobileController {
	private final MobileService mobileService;

	@ResponseBody
	@GetMapping("/{mobile}")
	@ApiOperation(value = "发送手机验证码", httpMethod = "GET")
	@ApiImplicitParam(name = "mobile", value = "手机号", paramType = "path", type = "string", required = true)
	public R<Boolean> code(@PathVariable String mobile) {
		return mobileService.sendSmsCode(mobile);
	}
}
