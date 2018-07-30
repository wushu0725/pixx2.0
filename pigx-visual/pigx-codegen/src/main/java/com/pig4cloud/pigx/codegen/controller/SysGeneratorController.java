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

package com.pig4cloud.pigx.codegen.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.pig4cloud.pigx.codegen.service.SysGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 代码生成器
 *
 * @author lengleng
 * @date 2018-07-30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/generator" )
public class SysGeneratorController {
	private final SysGeneratorService sysGeneratorService;

	/**
	 * 列表
	 *
	 * @param params 参数集
	 * @return 数据库表
	 */
	@GetMapping("/page" )
	public Page list(@RequestParam Map<String, Object> params) {
		return sysGeneratorService.queryPage(params);
	}

	/**
	 * 生成代码
	 */
	@GetMapping("/code/{tableName}" )
	public void code(@PathVariable String tableName, HttpServletResponse response) throws IOException {
		byte[] data = sysGeneratorService.generatorCode(new String[]{tableName});

		response.reset();
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip", tableName));
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8" );

		IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
	}
}
