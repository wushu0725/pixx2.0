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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pigx.admin.service.SysSocialDetailsService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 系统社交登录账号表
 *
 * @author pigx code generator
 * @date 2018-08-16 21:30:41
 */
@RestController
@RequestMapping("/social")
@AllArgsConstructor
@Api(value = "social", description = "三方账号管理模块")
public class SysSocialDetailsController {
	private final SysSocialDetailsService sysSocialDetailsService;


	/**
	 * 社交登录账户简单分页查询
	 *
	 * @param page             分页对象
	 * @param sysSocialDetails 社交登录
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<SysSocialDetails>> getSysSocialDetailsPage(Page<SysSocialDetails> page, SysSocialDetails sysSocialDetails) {
		return new R<>(sysSocialDetailsService.getSysSocialDetailsPage(page, sysSocialDetails));
	}


	/**
	 * 信息
	 *
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<SysSocialDetails> info(@PathVariable("id") Integer id) {
		return new R<>(sysSocialDetailsService.getById(id));
	}

	/**
	 * 保存
	 *
	 * @param sysSocialDetails
	 * @return R
	 */
	@SysLog("保存三方信息")
	@PostMapping
	public R save(@Valid @RequestBody SysSocialDetails sysSocialDetails) {
		sysSocialDetailsService.save(sysSocialDetails);
		return new R<>(Boolean.TRUE);
	}

	/**
	 * 修改
	 *
	 * @param sysSocialDetails
	 * @return R
	 */
	@SysLog("修改三方信息")
	@PutMapping
	public R update(@Valid @RequestBody SysSocialDetails sysSocialDetails) {
		sysSocialDetailsService.updateById(sysSocialDetails);
		return new R<>(Boolean.TRUE);
	}

	/**
	 * 删除
	 *
	 * @param id
	 * @return R
	 */
	@SysLog("删除三方信息")
	@DeleteMapping("/{id}")
	public R delete(@PathVariable Integer id) {
		return new R<>(sysSocialDetailsService.removeById(id));
	}

	/**
	 * 通过社交账号、手机号查询用户、角色信息
	 *
	 * @param inStr appid@code
	 * @return
	 */
	@GetMapping("/info/{inStr}")
	public R<UserInfo> social(@PathVariable String inStr) {
		return new R<>(sysSocialDetailsService.findUserInfo(inStr));
	}

	/**
	 * 绑定社交账号
	 *
	 * @param state 类型
	 * @param code  code
	 * @return
	 */
	@PostMapping("/bind")
	public R<Boolean> bind(String state, String code) {
		return new R<>(sysSocialDetailsService.bindSocial(state, code));
	}
}
