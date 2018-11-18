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

package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.entity.SysUserRole;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.constant.CommonConstant;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.Query;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2017/10/28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Api(value = "user", description = "用户管理模块")
public class UserController {
	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
	private final SysUserService userService;

	/**
	 * 获取当前用户信息（角色、权限）
	 * 并且异步初始化用户部门信息
	 *
	 * @param from     请求标志，该接口会被 auth、 前端调用
	 * @param username 用户名
	 * @return 用户名
	 */
	@GetMapping(value = {"/info", "/info/{username}"})
	public R<UserInfo> user(@PathVariable(required = false) String username,
							@RequestHeader(required = false) String from) {
		// 查询用户不为空时判断是不是内部请求
		if (StrUtil.isNotBlank(username) && !StrUtil.equals(SecurityConstants.FROM_IN, from)) {
			return new R<>(null, "error");
		}
		//为空时查询当前用户
		if (StrUtil.isBlank(username)) {
			username = SecurityUtils.getUser().getUsername();
		}

		SysUser condition = new SysUser();
		condition.setUsername(username);
		SysUser sysUser = userService.selectOne(new EntityWrapper<>(condition));
		if (sysUser == null) {
			return new R<>();
		}
		return new R<>(userService.findUserInfo(sysUser));
	}

	/**
	 * 通过ID查询当前用户信息
	 *
	 * @param id ID
	 * @return 用户信息
	 */
	@GetMapping("/{id}")
	public R<UserVO> user(@PathVariable Integer id) {
		return new R<>(userService.selectUserVoById(id));
	}

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param username 用户名
	 * @return
	 */
	@GetMapping("/details/{username}")
	public R<SysUser> user(@PathVariable String username) {
		SysUser condition = new SysUser();
		condition.setUsername(username);
		return new R<>(userService.selectOne(new EntityWrapper<>(condition)));
	}

	/**
	 * 删除用户信息
	 *
	 * @param id ID
	 * @return R
	 */
	@SysLog("删除用户信息")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_user_del')")
	@ApiOperation(value = "删除用户", notes = "根据ID删除用户")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
	public R<Boolean> userDel(@PathVariable Integer id) {
		SysUser sysUser = userService.selectById(id);
		return new R<>(userService.deleteUserById(sysUser));
	}

	/**
	 * 添加用户
	 *
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@SysLog("添加用户")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_user_add')")
	public R<Boolean> user(@RequestBody UserDTO userDto) {
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(userDto, sysUser);
		sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
		sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
		userService.insert(sysUser);
		userDto.getRole().forEach(roleId -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(sysUser.getUserId());
			userRole.setRoleId(roleId);
			userRole.insert();
		});
		return new R<>(Boolean.TRUE);
	}

	/**
	 * 更新用户信息
	 *
	 * @param userDto 用户信息
	 * @return R
	 */
	@SysLog("更新用户信息")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_user_edit')")
	public R<Boolean> userUpdate(@Valid @RequestBody UserDTO userDto) {
		SysUser user = userService.selectById(userDto.getUserId());
		return new R<>(userService.updateUser(userDto, user.getUsername()));
	}

	/**
	 * 分页查询用户
	 *
	 * @param params 参数集
	 * @return 用户集合
	 */
	@GetMapping("/page")
	public R<Page> userPage(@RequestParam Map<String, Object> params) {
		return new R<>(userService.selectWithRolePage(new Query(params)));
	}

	/**
	 * 修改个人信息
	 *
	 * @param userDto userDto
	 * @return success/false
	 */
	@SysLog("修改个人信息")
	@PutMapping("/edit")
	public R<Boolean> editInfo(@Valid @RequestBody UserDTO userDto) {
		return userService.updateUserInfo(userDto, SecurityUtils.getUser().getUsername());
	}

	/**
	 * @param username 用户名称
	 * @return
	 */
	@GetMapping("/ancestor/{username}")
	public R<List<SysUser>> ancestorUsers(@PathVariable String username) {
		return new R<>(userService.ancestorUsers(username));
	}
}
