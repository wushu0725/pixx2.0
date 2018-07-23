/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiXunHuan
 * @date 2018/1/16
 * 短信通道模板
 */
@Getter
@AllArgsConstructor
public enum EnumSmsChannelTemplate {
	/**
	 * 登录验证
	 */
	LOGIN_NAME_LOGIN("loginCodeChannel", "登录验证"),
	/**
	 * 服务异常提醒
	 */
	SERVICE_STATUS_CHANGE("serviceStatusChange", "Pig4Cloud");

	/**
	 * 模板名称
	 */
	private final String template;
	/**
	 * 模板签名
	 */
	private final String signName;
}
