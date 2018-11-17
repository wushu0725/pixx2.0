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
import com.pig4cloud.pigx.daemon.entity.ExecutionLog;
import com.pig4cloud.pigx.daemon.service.ExecutionLogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author lengleng
 * @date 2018-08-03 22:15:56
 */
@RestController
@RequestMapping("/executionlog")
@AllArgsConstructor
public class ExecutionLogController {

	private final ExecutionLogService executionLogService;


	/**
	 * 列表
	 *
	 * @param params
	 * @return
	 */
	@GetMapping("/page")
	public R<Page> page(@RequestParam Map<String, Object> params) {
		return new R<>(executionLogService.selectPage(new Query<>(params), new EntityWrapper<>()));
	}


	/**
	 * 信息
	 *
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<ExecutionLog> info(@PathVariable("id") String id) {
		return new R<>(executionLogService.selectById(id));
	}

	/**
	 * 保存
	 *
	 * @param executionLog
	 * @return R
	 */
	@PostMapping
	public R<Boolean> save(@RequestBody ExecutionLog executionLog) {
		return new R<>(executionLogService.insert(executionLog));
	}

	/**
	 * 修改
	 *
	 * @param executionLog
	 * @return R
	 */
	@PutMapping
	public R<Boolean> update(@RequestBody ExecutionLog executionLog) {
		return new R<>(executionLogService.updateById(executionLog));
	}

	/**
	 * 删除
	 *
	 * @param id
	 * @return R
	 */
	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable String id) {
		return new R<>(executionLogService.deleteById(id));
	}

}
