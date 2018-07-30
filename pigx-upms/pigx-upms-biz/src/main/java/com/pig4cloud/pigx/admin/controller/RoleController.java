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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pig4cloud.pigx.admin.api.dto.Query;
import com.pig4cloud.pigx.admin.api.dto.RoleDTO;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.service.SysRoleMenuService;
import com.pig4cloud.pigx.admin.service.SysRoleService;
import com.pig4cloud.pigx.common.core.constant.CommonConstant;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2017/11/5
 */
@RestController
@RequestMapping("/role")
public class RoleController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 通过ID查询角色信息
	 *
	 * @param id ID
	 * @return 角色信息
	 */
	@GetMapping("/{id}")
	public SysRole role(@PathVariable Integer id) {
		return sysRoleService.selectById(id);
	}

	/**
	 * 添加角色
	 *
	 * @param roleDto 角色信息
	 * @return success、false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_role_add')")
	public R<Boolean> role(@RequestBody RoleDTO roleDto) {
		return new R<>(sysRoleService.insertRole(roleDto));
	}

	/**
	 * 修改角色
	 *
	 * @param roleDto 角色信息
	 * @return success/false
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_role_edit')")
	public R<Boolean> roleUpdate(@RequestBody RoleDTO roleDto) {
		return new R<>(sysRoleService.updateRoleById(roleDto));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_role_del')")
	public R<Boolean> roleDel(@PathVariable Integer id) {
		SysRole sysRole = sysRoleService.selectById(id);
		sysRole.setDelFlag(CommonConstant.STATUS_DEL);
		return new R<>(sysRoleService.updateById(sysRole));
	}

	/**
	 * 获取角色列表
	 *
	 * @param deptId 部门ID
	 * @return 角色列表
	 */
	@GetMapping("/roleList/{deptId}")
	public List<SysRole> roleList(@PathVariable Integer deptId) {
		return sysRoleService.selectListByDeptId(deptId);

	}

	/**
	 * 分页查询角色信息
	 *
	 * @param params 分页对象
	 * @return 分页对象
	 */
	@GetMapping("/rolePage")
	public Page rolePage(@RequestParam Map<String, Object> params) {
		params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
		return sysRoleService.selectwithDeptPage(new Query<>(params), new EntityWrapper<>());
	}

	/**
	 * 更新角色菜单
	 *
	 * @param roleId  角色ID
	 * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
	 * @return success、false
	 */
	@PutMapping("/roleMenuUpd")
	@PreAuthorize("@pms.hasPermission('sys_role_perm')")
	public R<Boolean> roleMenuUpd(Integer roleId, @RequestParam(value = "menuIds", required = false) String menuIds) {
		SysRole sysRole = sysRoleService.selectById(roleId);
		return new R<>(sysRoleMenuService.insertRoleMenus(sysRole.getRoleCode(), roleId, menuIds));
	}
}
