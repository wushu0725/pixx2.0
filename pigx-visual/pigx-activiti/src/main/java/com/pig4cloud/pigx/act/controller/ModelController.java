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
import com.pig4cloud.pigx.act.dto.ModelForm;
import com.pig4cloud.pigx.act.service.ModelService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import org.activiti.engine.repository.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/25
 */
@RestController
@RequestMapping("/model")
@AllArgsConstructor
public class ModelController {
	private final ModelService modelService;

	@PostMapping(value = "/insert")
	public R<Boolean> insertForm(@RequestBody @Valid ModelForm form) {
		modelService.create(form.getName(), form.getKey(), form.getDesc(), form.getCategory());
		return new R<>(Boolean.TRUE);
	}

	@PostMapping
	public String createModel(@RequestParam String name, @RequestParam String key,
							  @RequestParam String desc, @RequestParam String category) {
		Model model = modelService.create(name, key, desc, category);
		return model.getId();
	}

	@GetMapping
	public R<IPage<Model>> list(@RequestParam Map<String, Object> params) {
		return new R<> (modelService.selectPage(params));
	}

	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable("id") String id) {
		return new R<>(modelService.delete(id));

	}

	@PostMapping("/deploy/{id}")
	public R<Boolean> deploy(@PathVariable("id") String id) {
		return new R<>(modelService.deploy(id));
	}
}
