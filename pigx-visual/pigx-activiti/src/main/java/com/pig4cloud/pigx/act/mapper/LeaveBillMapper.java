package com.pig4cloud.pigx.act.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.act.entity.LeaveBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 请假流程
 *
 * @author 冷冷
 * @date 2018-09-27 15:20:44
 */
@Mapper
public interface LeaveBillMapper extends BaseMapper<LeaveBill> {
	/**
	 * 请假审批单简单分页查询
	 * @param leaveBill 请假审批单
	 * @return
	 */
	IPage<LeaveBill> getLeaveBillPage(Page page, @Param("leaveBill") LeaveBill leaveBill);

}
