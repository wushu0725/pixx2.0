package com.pig4cloud.pigx.daemon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.pig4cloud.pigx.daemon.mapper.StatusTraceLogMapper;
import com.pig4cloud.pigx.daemon.entity.StatusTraceLog;
import com.pig4cloud.pigx.daemon.service.StatusTraceLogService;


@Service("statusTraceLogService")
public class StatusTraceLogServiceImpl extends ServiceImpl<StatusTraceLogMapper, StatusTraceLog> implements StatusTraceLogService {

}
