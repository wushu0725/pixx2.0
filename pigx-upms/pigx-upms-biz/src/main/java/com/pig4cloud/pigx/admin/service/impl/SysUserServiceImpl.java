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

package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.*;
import com.pig4cloud.pigx.admin.api.vo.MenuVO;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.admin.mapper.SysUserMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.core.datascope.DataScope;
import com.pig4cloud.pigx.common.core.util.Query;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
	private final SysMenuService sysMenuService;
	private final SysRoleService sysRoleService;
	private final SysDeptService sysDeptService;
	private final SysUserRoleService sysUserRoleService;
	private final SysDeptRelationService sysDeptRelationService;

	/**
	 * 通过查用户的全部信息
	 *
	 * @param sysUser 用户
	 * @return
	 */
	@Override
	@Cacheable(value = "user_details", key = "#sysUser.username", unless = "#result == null")
	public UserInfo findUserInfo(SysUser sysUser) {
		if (sysUser == null) {
			return null;
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(sysUser);
		//设置角色列表  （ID）
		List<Integer> roleIds = sysRoleService.findRolesByUserId(sysUser.getUserId())
			.stream()
			.map(SysRole::getRoleId)
			.collect(Collectors.toList());
		userInfo.setRoles(ArrayUtil.toArray(roleIds, Integer.class));

		//设置权限列表（menu.permission）
		Set<String> permissions = new HashSet<>();
		roleIds.forEach(roleId -> {
			List<String> permissionList = sysMenuService.findMenuByRoleId(roleId)
				.stream()
				.filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermission()))
				.map(MenuVO::getPermission)
				.collect(Collectors.toList());
			permissions.addAll(permissionList);
		});
		userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
		return userInfo;
	}

	@Override
	public Page selectWithRolePage(Query query) {
		DataScope dataScope = new DataScope();
		dataScope.setScopeName("deptId");
		dataScope.setIsOnly(true);
		dataScope.setDeptIds(getChildDepts());
		Object username = query.getCondition().get("username");
		query.setRecords(baseMapper.selectUserVoPage(query, username, dataScope));
		return query;
	}


	/**
	 * 通过ID查询用户信息
	 *
	 * @param id 用户ID
	 * @return 用户信息
	 */
	@Override
	public UserVO selectUserVoById(Integer id) {
		return baseMapper.selectUserVoById(id);
	}

	/**
	 * 删除用户
	 *
	 * @param sysUser 用户
	 * @return Boolean
	 */
	@Override
	@CacheEvict(value = "user_details", key = "#sysUser.username")
	public Boolean deleteUserById(SysUser sysUser) {
		sysUserRoleService.deleteByUserId(sysUser.getUserId());
		this.deleteById(sysUser.getUserId());
		return Boolean.TRUE;
	}

	@Override
	@CacheEvict(value = "user_details", key = "#username")
	public R<Boolean> updateUserInfo(UserDTO userDto, String username) {
		UserVO userVO = baseMapper.selectUserVoByUsername(username);
		SysUser sysUser = new SysUser();
		if (StrUtil.isNotBlank(userDto.getPassword())
			&& StrUtil.isNotBlank(userDto.getNewpassword1())) {
			if (ENCODER.matches(userDto.getPassword(), userVO.getPassword())) {
				sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
			} else {
				log.warn("原密码错误，修改密码失败:{}", username);
				return new R<>(Boolean.FALSE, "原密码错误，修改失败");
			}
		}
		sysUser.setPhone(userDto.getPhone());
		sysUser.setUserId(userVO.getUserId());
		sysUser.setAvatar(userDto.getAvatar());
		return new R<>(this.updateById(sysUser));
	}

	@Override
	@CacheEvict(value = "user_details", key = "#username")
	public Boolean updateUser(UserDTO userDto, String username) {
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(userDto, sysUser);
		sysUser.setUpdateTime(LocalDateTime.now());

		if (StrUtil.isNotBlank(userDto.getPassword())) {
			sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
		}
		this.updateById(sysUser);

		SysUserRole condition = new SysUserRole();
		condition.setUserId(userDto.getUserId());
		sysUserRoleService.delete(new EntityWrapper<>(condition));
		userDto.getRole().forEach(roleId -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(sysUser.getUserId());
			userRole.setRoleId(roleId);
			userRole.insert();
		});
		return Boolean.TRUE;
	}

	/**
	 * 查询上级部门的用户信息
	 *
	 * @param username 用户名
	 * @return R
	 */
	@Override
	public List<SysUser> ancestorUsers(String username) {
		SysUser condition = new SysUser();
		condition.setUsername(username);
		SysUser sysUser = this.selectOne(new EntityWrapper<>(condition));

		Integer deptId = sysUser.getDeptId();
		SysDept sysDept = sysDeptService.selectById(deptId);
		if (sysDept == null) {
			return null;
		}

		Integer parentId = sysDept.getParentId();
		SysUser condition2 = new SysUser();
		condition2.setDeptId(parentId);
		return this.selectList(new EntityWrapper<>(condition2));
	}

	/**
	 * 获取当前用户的子部门信息
	 *
	 * @return 子部门列表
	 */
	private List<Integer> getChildDepts() {
		Integer deptId = SecurityUtils.getUser().getDeptId();
		//获取当前部门的子部门
		SysDeptRelation condition = new SysDeptRelation();
		condition.setAncestor(deptId);
		return sysDeptRelationService
			.selectList(new EntityWrapper<>(condition))
			.stream()
			.map(SysDeptRelation::getDescendant)
			.collect(Collectors.toList());
	}

}
