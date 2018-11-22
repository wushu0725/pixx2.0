package com.pig4cloud.pigx.daemon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.daemon.entity.ExecutionLog;

/**
 * 任务日志处理
 *
 * @author lishangbu
 * @date 2018-10-27 10:55:42
 */
public interface ExecutionLogService extends IService<ExecutionLog> {

  /**
   * 任务日志处理简单分页查询
   * @param executionLog 任务日志处理
   * @return
   */
  IPage<ExecutionLog> getExecutionLogPage(Page<ExecutionLog> page, ExecutionLog executionLog);


}
