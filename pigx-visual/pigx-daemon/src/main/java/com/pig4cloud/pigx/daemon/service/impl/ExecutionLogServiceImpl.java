package com.pig4cloud.pigx.daemon.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.daemon.entity.ExecutionLog;
import com.pig4cloud.pigx.daemon.mapper.ExecutionLogMapper;
import com.pig4cloud.pigx.daemon.service.ExecutionLogService;
import org.springframework.stereotype.Service;

/**
 * 任务日志处理
 *
 * @author lishangbu
 * @date 2018/11/22
 */
@Service("executionLogService")
public class ExecutionLogServiceImpl extends ServiceImpl<ExecutionLogMapper, ExecutionLog> implements ExecutionLogService {


	/**
	 * 任务日志处理简单分页查询
	 *
	 * @param executionLog 任务日志处理
	 * @return
	 */
	@Override
	public IPage<ExecutionLog> getExecutionLogPage(Page<ExecutionLog> page, ExecutionLog executionLog) {
		return baseMapper.getExecutionLogPage(page, executionLog);
	}

}
