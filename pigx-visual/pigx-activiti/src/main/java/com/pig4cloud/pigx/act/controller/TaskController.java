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

import com.pig4cloud.pigx.act.dto.DataTable;
import com.pig4cloud.pigx.act.dto.StartTaskForm;
import com.pig4cloud.pigx.act.service.ActTaskService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lengleng
 * @date 2018/9/25
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
	ActTaskService taskService;

    @GetMapping(value = "/todo")
    public String list(Model model, HttpServletRequest request) {
        model.addAttribute("url", request.getContextPath()+"/task/todo/");
        return "task/todoList";
    }

    @PostMapping("/todo/list")
    @ResponseBody
    public DataTable todoList(@RequestBody DataTable dt) {
        return taskService.getTodoTasks(dt);
    }

    @PostMapping("/todo/start")
    public R<Boolean> start(@RequestBody StartTaskForm form, BindingResult result) {
        return new R<>(Boolean.TRUE);
    }
}
