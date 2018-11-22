package com.pig4cloud.pigx.daemon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.daemon.entity.ExecutionLog;
import org.apache.ibatis.annotations.Param;

/**
 * 任务日志处理
 *
 * @author lishangbu
 * @date 2018/11/22
 */
public interface ExecutionLogMapper extends BaseMapper<ExecutionLog> {
  /**
    * 任务日志处理简单分页查询
    * @param executionLog 任务日志处理
    * @return
    */
  IPage<ExecutionLog> getExecutionLogPage(Page page, @Param("executionLog") ExecutionLog executionLog);


}
