package com.pig4cloud.pigx.daemon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.pig4cloud.pigx.daemon.mapper.ExecutionLogMapper;
import com.pig4cloud.pigx.daemon.entity.ExecutionLog;
import com.pig4cloud.pigx.daemon.service.ExecutionLogService;


@Service("executionLogService")
public class ExecutionLogServiceImpl extends ServiceImpl<ExecutionLogMapper, ExecutionLog> implements ExecutionLogService {

}
