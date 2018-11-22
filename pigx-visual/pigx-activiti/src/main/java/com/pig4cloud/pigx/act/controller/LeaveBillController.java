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

package com.pig4cloud.pigx.act.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.act.entity.LeaveBill;
import com.pig4cloud.pigx.act.service.LeaveBillService;
import com.pig4cloud.pigx.act.service.ProcessService;
import com.pig4cloud.pigx.common.core.constant.enums.EnumTaskStatus;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 请假流程
 *
 * @author 冷冷
 * @date 2018-09-27 15:20:44
 */
@RestController
@AllArgsConstructor
@RequestMapping("/leavebill")
public class LeaveBillController {
	private final LeaveBillService leaveBillService;
	private final ProcessService processService;

	/**
	 * 请假审批单简单分页查询
	 * @param page 分页对象
	 * @param leaveBill 请假审批单
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<LeaveBill>> getLeaveBillPage(Page<LeaveBill> page, LeaveBill leaveBill) {
		return  new R<>(leaveBillService.getLeaveBillPage(page,leaveBill));
	}


	/**
	 * 信息
	 *
	 * @param leaveId
	 * @return R
	 */
	@GetMapping("/{leaveId}")
	public R info(@PathVariable("leaveId") Integer leaveId) {
		LeaveBill leaveBill = leaveBillService.getById(leaveId);
		return new R<>(leaveBill);
	}

	/**
	 * 保存
	 *
	 * @param leaveBill
	 * @return R
	 */
	@PostMapping
	public R save(@RequestBody LeaveBill leaveBill) {
		leaveBill.setUsername(SecurityUtils.getUsername());
		leaveBill.setState(EnumTaskStatus.UNSUBMIT.getStatus());
		return new R<>(leaveBillService.save(leaveBill));
	}

	/**
	 * 修改
	 *
	 * @param leaveBill
	 * @return R
	 */
	@PutMapping
	public R update(@RequestBody LeaveBill leaveBill) {
		leaveBillService.updateById(leaveBill);
		return new R<>(Boolean.TRUE);
	}

	/**
	 * 删除
	 *
	 * @param leaveId
	 * @return R
	 */
	@DeleteMapping("/{leaveId}")
	public R delete(@PathVariable Integer leaveId) {
		return new R<>(leaveBillService.removeById(leaveId));
	}

	/**
	 * 提交请假流程
	 *
	 * @param leaveId
	 * @return R
	 */
	@GetMapping("/submit/{leaveId}")
	public R submit(@PathVariable("leaveId") Integer leaveId) {
		return new R<>(processService.saveStartProcess(leaveId));
	}
}
