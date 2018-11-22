package com.pig4cloud.pigx.daemon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.daemon.entity.StatusTraceLog;

/**
 * 任务轨迹处理
 *
 * @author lishangbu
 * @date 2018-10-28 07:18:05
 */
public interface StatusTraceLogService extends IService<StatusTraceLog> {

  /**
   * 任务轨迹处理简单分页查询
   * @param statusTraceLog 任务轨迹处理
   * @return
   */
  IPage<StatusTraceLog> getStatusTraceLogPage(Page<StatusTraceLog> page, StatusTraceLog statusTraceLog);


}
