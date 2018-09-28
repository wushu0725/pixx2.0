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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pig4cloud.pigx.act.entity.LeaveBill;
import com.pig4cloud.pigx.act.service.ActTaskService;
import com.pig4cloud.pigx.act.service.LeaveBillService;
import com.pig4cloud.pigx.act.service.ProcessService;
import com.pig4cloud.pigx.common.core.util.Query;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
	private final ActTaskService actTaskService;


	/**
	 * 列表
	 *
	 * @param params
	 * @return
	 */
	@GetMapping("/page")
	public Page page(@RequestParam Map<String, Object> params) {
		return leaveBillService.selectPage(new Query<>(params), new EntityWrapper<>());
	}


	/**
	 * 信息
	 *
	 * @param leaveId
	 * @return R
	 */
	@GetMapping("/{leaveId}")
	public R info(@PathVariable("leaveId") Integer leaveId) {
		LeaveBill leaveBill = leaveBillService.selectById(leaveId);
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
		return new R<>(leaveBillService.insert(leaveBill));
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
		return new R<>(leaveBillService.deleteById(leaveId));
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
