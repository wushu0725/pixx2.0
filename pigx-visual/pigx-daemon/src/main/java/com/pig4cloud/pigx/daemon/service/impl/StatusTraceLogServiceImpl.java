package com.pig4cloud.pigx.daemon.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.pig4cloud.pigx.daemon.entity.StatusTraceLog;
import com.pig4cloud.pigx.daemon.mapper.StatusTraceLogMapper;
import com.pig4cloud.pigx.daemon.service.StatusTraceLogService;
import org.springframework.stereotype.Service;

/**
 * @author lengleng
 * @date 2018年08月16日
 * elastic-job 任务轨迹处理
 */
@Service("statusTraceLogService")
public class StatusTraceLogServiceImpl extends ServiceImpl<StatusTraceLogMapper, StatusTraceLog> implements StatusTraceLogService {

}
