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

package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oauth_client_details")
public class SysOauthClientDetails extends Model<SysOauthClientDetails> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "client_id", type = IdType.INPUT)
	private String clientId;
	@TableField("resource_ids")
	private String resourceIds;
	@TableField("client_secret")
	private String clientSecret;
	private String scope;
	@TableField("authorized_grant_types")
	private String authorizedGrantTypes;
	@TableField("web_server_redirect_uri")
	private String webServerRedirectUri;
	private String authorities;
	@TableField("access_token_validity")
	private Integer accessTokenValidity;
	@TableField("refresh_token_validity")
	private Integer refreshTokenValidity;
	@TableField("additional_information")
	private String additionalInformation;
	private String autoapprove;

	@Override
	protected Serializable pkVal() {
		return this.clientId;
	}

}
