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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pig4cloud.pigx.admin.api.dto.MenuTree;
import com.pig4cloud.pigx.admin.api.entity.SysMenu;
import com.pig4cloud.pigx.admin.api.vo.MenuVO;
import com.pig4cloud.pigx.admin.api.vo.TreeUtil;
import com.pig4cloud.pigx.admin.service.SysMenuService;
import com.pig4cloud.pigx.common.core.constant.CommonConstant;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 返回当前用户的树形菜单集合
	 *
	 * @return 当前用户的树形菜单
	 */
	@GetMapping(value = "/userMenu")
	public List<MenuTree> userMenu() {
		// 获取符合条件得菜单
		Set<MenuVO> all = new HashSet<>();
		SecurityUtils.getRoles().forEach(roleId -> all.addAll(sysMenuService.findMenuByRoleId(roleId)));
		List<MenuTree> menuTreeList = new ArrayList<>();
		all.forEach(menuVo -> {
			if (CommonConstant.MENU.equals(menuVo.getType())) {
				menuTreeList.add(new MenuTree(menuVo));
			}
		});
		CollUtil.sort(menuTreeList, Comparator.comparingInt(MenuTree::getSort));
		return TreeUtil.bulid(menuTreeList, -1);
	}

	/**
	 * 返回树形菜单集合
	 *
	 * @return 树形菜单
	 */
	@GetMapping(value = "/allTree")
	public List<MenuTree> getTree() {
		SysMenu condition = new SysMenu();
		condition.setDelFlag(CommonConstant.STATUS_NORMAL);
		return TreeUtil.bulidTree(sysMenuService.selectList(new EntityWrapper<>(condition)), -1);
	}

	/**
	 * 返回角色的菜单集合
	 *
	 * @param roleId 角色ID
	 * @return 属性集合
	 */
	@GetMapping("/roleTree/{roleId}")
	public List<Integer> roleTree(@PathVariable Integer roleId) {
		List<MenuVO> menus = sysMenuService.findMenuByRoleId(roleId);
		List<Integer> menuList = new ArrayList<>();
		for (MenuVO menuVo : menus) {
			menuList.add(menuVo.getMenuId());
		}
		return menuList;
	}

	/**
	 * 通过ID查询菜单的详细信息
	 *
	 * @param id 菜单ID
	 * @return 菜单详细信息
	 */
	@GetMapping("/{id}")
	public SysMenu menu(@PathVariable Integer id) {
		return sysMenuService.selectById(id);
	}

	/**
	 * 新增菜单
	 *
	 * @param sysMenu 菜单信息
	 * @return success/false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_menu_add')")
	public R<Boolean> menu(@Valid @RequestBody SysMenu sysMenu) {
		return new R<>(sysMenuService.insert(sysMenu));
	}

	/**
	 * 删除菜单
	 *
	 * @param id 菜单ID
	 * @return success/false
	 * TODO  级联删除下级节点
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_menu_del')")
	public R<Boolean> menuDel(@PathVariable Integer id) {
		return new R<>(sysMenuService.deleteMenu(id));
	}

	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_menu_edit')")
	public R<Boolean> menuUpdate(@Valid @RequestBody SysMenu sysMenu) {
		return new R<>(sysMenuService.updateMenuById(sysMenu));
	}

}
