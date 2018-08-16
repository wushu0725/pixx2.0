package com.pig4cloud.pigx.daemon.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.pig4cloud.pigx.daemon.entity.ExecutionLog;
import com.pig4cloud.pigx.daemon.mapper.ExecutionLogMapper;
import com.pig4cloud.pigx.daemon.service.ExecutionLogService;
import org.springframework.stereotype.Service;

/**
 * @author lengleng
 * @date 2018年08月16日
 * elastic-job 任务日志处理
 */
@Service("executionLogService")
public class ExecutionLogServiceImpl extends ServiceImpl<ExecutionLogMapper, ExecutionLog> implements ExecutionLogService {

}
