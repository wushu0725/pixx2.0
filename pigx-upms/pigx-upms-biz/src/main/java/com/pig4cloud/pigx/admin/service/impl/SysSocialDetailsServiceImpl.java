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

package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.handler.LoginHandler;
import com.pig4cloud.pigx.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pigx.admin.mapper.SysUserMapper;
import com.pig4cloud.pigx.admin.service.SysSocialDetailsService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.EnumLoginType;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018年08月16日
 */
@Slf4j
@AllArgsConstructor
@Service("sysSocialDetailsService")
public class SysSocialDetailsServiceImpl extends ServiceImpl<SysSocialDetailsMapper, SysSocialDetails> implements SysSocialDetailsService {
	private final Map<String, LoginHandler> loginHandlerMap;
	private final CacheManager cacheManager;
	private final RestTemplate restTemplate;
	private final SysUserMapper sysUserMapper;


	/**
	 * 社交登录简单分页查询
	 *
	 * @param sysSocialDetails 社交登录
	 * @return
	 */
	@Override
	public IPage<SysSocialDetails> getSysSocialDetailsPage(Page<SysSocialDetails> page, SysSocialDetails sysSocialDetails) {
		return baseMapper.getSysSocialDetailsPage(page, sysSocialDetails);
	}

	/**
	 * 绑定社交账号
	 *
	 * @param appId appId
	 * @param code  code
	 * @return
	 */
	@Override
	public Boolean bindSocial(String appId, String code) {
		Map<String, String> result = getOpenId(appId, code);

		SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getUser().getId());
		sysUser.setWxOpenid(result.get("openId"));

		sysUserMapper.updateById(sysUser);
		//更新緩存
		cacheManager.getCache("user_details").evict(result.get("openId"));
		return Boolean.TRUE;
	}

	/**
	 * 根据入参查询用户信息
	 *
	 * @param inStr TYPE@code
	 * @return
	 */
	@Override
	public UserInfo findUserInfo(String inStr) {
		String[] inStrs = inStr.split("@");
		String type = inStrs[0];
		String loginStr = inStrs[1];
		return loginHandlerMap.get(type).handle(loginStr);
	}

	/**
	 * 通过appid 、code 获得openID
	 *
	 * @param appId
	 * @param code
	 * @return
	 */
	private Map<String, String> getOpenId(String appId, String code) {
		SysSocialDetails condition = new SysSocialDetails();
		condition.setAppId(appId);
		SysSocialDetails socialDetails = this.baseMapper.selectOne(new QueryWrapper<>(condition));

		String openId = null;
		//微信登录
		if (EnumLoginType.WECHAT.getType().equals(socialDetails.getType())) {
			String url = String.format(SecurityConstants.WX_AUTHORIZATION_CODE_URL
				, socialDetails.getAppId(), socialDetails.getAppSecret(), code);
			String result = restTemplate.getForObject(url, String.class);
			log.debug("微信响应报文:{}", result);

			Object obj = JSONUtil.parseObj(result).get("openid");
			if (obj == null) {
				return null;
			}
			openId = obj.toString();
		}

		Map<String, String> result = new HashMap<>(2);
		result.put("type", socialDetails.getType());
		result.put("openId", openId);
		return result;
	}

}
