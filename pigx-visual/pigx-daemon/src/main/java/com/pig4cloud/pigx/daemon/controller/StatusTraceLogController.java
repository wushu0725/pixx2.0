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

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.pig4cloud.pigx.common.core.util.Query;

import com.pig4cloud.pigx.daemon.entity.StatusTraceLog;
import com.pig4cloud.pigx.daemon.service.StatusTraceLogService;
import com.pig4cloud.pigx.common.core.util.R;
import java.util.Map;


/**
 *
 *
 * @author lengleng
 * @date 2018-08-03 22:15:45
 */
@RestController
@RequestMapping("/statustracelog")
public class StatusTraceLogController {
    @Autowired
    private StatusTraceLogService statusTraceLogService;


    /**
    *  列表
    * @param params
    * @return
    */
    @GetMapping("/page")
    public Page page(@RequestParam Map<String, Object> params) {
      return  statusTraceLogService.selectPage(new Query<>(params), new EntityWrapper<>());
    }


    /**
     * 信息
     * @param id
     * @return R
     */
    @GetMapping("/{id}")
    public R info(@PathVariable("id") String id){
			StatusTraceLog statusTraceLog = statusTraceLogService.selectById(id);

			  return new R<>(statusTraceLog);
    }

    /**
     * 保存
     * @param statusTraceLog
     * @return R
     */
    @PostMapping("/save")
    public R save(@RequestBody StatusTraceLog statusTraceLog){
			statusTraceLogService.insert(statusTraceLog);

			  return new R<>(Boolean.TRUE);
    }

    /**
     * 修改
     * @param statusTraceLog
     * @return R
     */
    @PutMapping("/update")
    public R update(@RequestBody StatusTraceLog statusTraceLog){
			statusTraceLogService.updateById(statusTraceLog);

      return new R<>(Boolean.TRUE);
    }

    /**
     * 删除
     * @param ids
     * @return R
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody String[] ids){
			statusTraceLogService.deleteBatchIds(Arrays.asList(ids));

      return new R<>(Boolean.TRUE);
    }

}
