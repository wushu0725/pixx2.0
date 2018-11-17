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

package com.pig4cloud.pigx.daemon.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pig4cloud.pigx.common.core.util.Query;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.daemon.entity.StatusTraceLog;
import com.pig4cloud.pigx.daemon.service.StatusTraceLogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author lengleng
 * @date 2018-08-03 22:15:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statustracelog")
public class StatusTraceLogController {
	private final StatusTraceLogService statusTraceLogService;

	/**
	 * 列表
	 *
	 * @param params
	 * @return
	 */
	@GetMapping("/page")
	public R<Page> page(@RequestParam Map<String, Object> params) {
		return new R<>(statusTraceLogService.selectPage(new Query<>(params), new EntityWrapper<>()));
	}


	/**
	 * 信息
	 *
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<StatusTraceLog> info(@PathVariable("id") String id) {
		return new R<>(statusTraceLogService.selectById(id));
	}

	/**
	 * 保存
	 *
	 * @param statusTraceLog
	 * @return R
	 */
	@PostMapping
	public R<Boolean> save(@RequestBody StatusTraceLog statusTraceLog) {
		return new R<>(statusTraceLogService.insert(statusTraceLog));
	}

	/**
	 * 修改
	 *
	 * @param statusTraceLog
	 * @return R
	 */
	@PutMapping
	public R<Boolean> update(@RequestBody StatusTraceLog statusTraceLog) {
		return new R<>(statusTraceLogService.updateById(statusTraceLog));
	}

	/**
	 * 删除
	 *
	 * @param id
	 * @return R
	 */
	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable("id") String id) {
		return new R<>(statusTraceLogService.deleteById(id));
	}

}
