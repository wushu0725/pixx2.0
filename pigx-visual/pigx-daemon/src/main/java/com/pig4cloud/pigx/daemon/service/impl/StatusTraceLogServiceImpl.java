package com.pig4cloud.pigx.daemon.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.daemon.entity.StatusTraceLog;
import com.pig4cloud.pigx.daemon.mapper.StatusTraceLogMapper;
import com.pig4cloud.pigx.daemon.service.StatusTraceLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 任务轨迹处理
 *
 * @author lishangbu
 * @date 2018/11/22
 */
@Service("statusTraceLogService")
@AllArgsConstructor
public class StatusTraceLogServiceImpl extends ServiceImpl<StatusTraceLogMapper, StatusTraceLog> implements StatusTraceLogService {

  private final StatusTraceLogMapper statusTraceLogMapper;

  /**
   * 任务轨迹处理简单分页查询
   * @param statusTraceLog 任务轨迹处理
   * @return
   */
  @Override
  public IPage<StatusTraceLog> getStatusTraceLogPage(Page<StatusTraceLog> page, StatusTraceLog statusTraceLog){
      return statusTraceLogMapper.getStatusTraceLogPage(page,statusTraceLog);
  }

}
