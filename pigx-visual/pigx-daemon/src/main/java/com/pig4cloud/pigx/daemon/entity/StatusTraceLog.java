package com.pig4cloud.pigx.daemon.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
  import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author lengleng
 * @date 2018-08-03 22:15:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("JOB_STATUS_TRACE_LOG")
public class StatusTraceLog extends Model<StatusTraceLog> {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String id;
	/**
	 * 
	 */
	private String jobName;
	/**
	 * 
	 */
	private String originalTaskId;
	/**
	 * 
	 */
	private String taskId;
	/**
	 * 
	 */
	private String slaveId;
	/**
	 * 
	 */
	private String source;
	/**
	 * 
	 */
	private String executionType;
	/**
	 * 
	 */
	private String shardingItem;
	/**
	 * 
	 */
	private String state;
	/**
	 * 
	 */
	private String message;
	/**
	 * 
	 */
	private LocalDateTime creationTime;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
