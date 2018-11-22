package com.pig4cloud.pigx.daemon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.daemon.entity.StatusTraceLog;
import org.apache.ibatis.annotations.Param;

/**
 * 任务轨迹处理
 *
 * @author lishangbu
 * @date 2018/11/22
 */
public interface StatusTraceLogMapper extends BaseMapper<StatusTraceLog> {
  /**
    * 任务轨迹处理简单分页查询
    * @param statusTraceLog 任务轨迹处理
    * @return
    */
  IPage<StatusTraceLog> getStatusTraceLogPage(Page page, @Param("statusTraceLog") StatusTraceLog statusTraceLog);


}
