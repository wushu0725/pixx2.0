package com.pig4cloud.pigx.act.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.act.entity.LeaveBill;

/**
 * 请假流程
 *
 * @author 冷冷
 * @date 2018-09-27 15:20:44
 */
public interface LeaveBillService extends IService<LeaveBill> {
	/**
	 * 请假审批单简单分页查询
	 *
	 * @param leaveBill 请假审批单
	 * @return
	 */
	IPage<LeaveBill> getLeaveBillPage(Page<LeaveBill> page, LeaveBill leaveBill);
}

