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

import com.baomidou.mybatisplus.plugins.Page;
import com.pig4cloud.pigx.act.dto.ProcessDefDTO;
import com.pig4cloud.pigx.act.service.ProcessService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/process")
public class ProcessController {
	private final ProcessService processService;

	@GetMapping
	public Page<ProcessDefDTO> list(@RequestParam Map<String, Object> params) {
		return processService.getProcessByPage(params);
	}

	@GetMapping(value = "/resource/{proInsId}/{procDefId}/{resType}")
	public void resourceRead(@PathVariable String procDefId, @PathVariable String proInsId, @PathVariable String resType, HttpServletResponse response) {
		if ("xml".equals(resType)) {
			response.setContentType("application/xml");
		} else {
			response.setContentType("image/png");
		}

		try {
			InputStream resourceAsStream = processService.resourceRead(procDefId, proInsId, resType);
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
		} catch (Exception e) {
		}
	}

	@PutMapping("/status/{procDefId}/{status}")
	public R<Boolean> updateState(@PathVariable String procDefId, @PathVariable String status) {
		return new R<>(processService.updateStatus(status, procDefId));
	}

	@DeleteMapping("/{deploymentId}")
	public R<Boolean> deleteProcIns(@PathVariable String deploymentId) {
		return new R<>(processService.deleteProcIns(deploymentId));
	}
}
