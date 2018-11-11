/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost
 Source Database       : pigxx

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : utf-8

 Date: 11/11/2018 20:38:26 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `JOB_EXECUTION_LOG`
-- ----------------------------
DROP TABLE IF EXISTS `JOB_EXECUTION_LOG`;
CREATE TABLE `JOB_EXECUTION_LOG` (
  `id` varchar(40) NOT NULL,
  `job_name` varchar(100) NOT NULL,
  `task_id` varchar(255) NOT NULL,
  `hostname` varchar(255) NOT NULL,
  `ip` varchar(50) NOT NULL,
  `sharding_item` int(11) NOT NULL,
  `execution_source` varchar(20) NOT NULL,
  `failure_cause` varchar(4000) DEFAULT NULL,
  `is_success` int(11) NOT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `complete_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `JOB_STATUS_TRACE_LOG`
-- ----------------------------
DROP TABLE IF EXISTS `JOB_STATUS_TRACE_LOG`;
CREATE TABLE `JOB_STATUS_TRACE_LOG` (
  `id` varchar(40) NOT NULL,
  `job_name` varchar(100) NOT NULL,
  `original_task_id` varchar(255) NOT NULL,
  `task_id` varchar(255) NOT NULL,
  `slave_id` varchar(50) NOT NULL,
  `source` varchar(50) NOT NULL,
  `execution_type` varchar(20) NOT NULL,
  `sharding_item` varchar(100) NOT NULL,
  `state` varchar(20) NOT NULL,
  `message` varchar(4000) DEFAULT NULL,
  `creation_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TASK_ID_STATE_INDEX` (`task_id`,`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `parent_id` int(11) DEFAULT NULL,
  `tenant_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门管理';

-- ----------------------------
--  Records of `sys_dept`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES ('1', '山东农信', null, '2018-01-22 19:00:23', '2018-09-13 01:46:29', '0', '0', '1'), ('2', '沙县国际', null, '2018-01-22 19:00:38', '2018-09-13 01:46:30', '0', '0', '1'), ('3', '潍坊农信', null, '2018-01-22 19:00:44', '2018-09-13 01:46:31', '0', '1', '1'), ('4', '高新农信', null, '2018-01-22 19:00:52', '2018-10-06 10:41:52', '0', '3', '1'), ('5', '院校农信', null, '2018-01-22 19:00:57', '2018-10-06 10:42:51', '0', '4', '1'), ('6', '潍坊学院农信', null, '2018-01-22 19:01:06', '2018-09-13 03:29:10', '1', '5', '1'), ('7', '山东沙县', null, '2018-01-22 19:01:57', '2018-09-13 01:46:42', '0', '2', '1'), ('8', '潍坊沙县', null, '2018-01-22 19:02:03', '2018-09-13 01:46:43', '0', '7', '1'), ('9', '高新沙县', null, '2018-01-22 19:02:14', '2018-09-13 01:46:44', '1', '8', '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_dept_relation`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation` (
  `ancestor` int(11) NOT NULL COMMENT '祖先节点',
  `descendant` int(11) NOT NULL COMMENT '后代节点',
  PRIMARY KEY (`ancestor`,`descendant`),
  KEY `idx1` (`ancestor`),
  KEY `idx2` (`descendant`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Records of `sys_dept_relation`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept_relation` VALUES ('1', '1'), ('1', '3'), ('1', '4'), ('1', '5'), ('2', '2'), ('2', '7'), ('2', '8'), ('3', '3'), ('3', '4'), ('3', '5'), ('4', '4'), ('4', '5'), ('5', '5'), ('7', '7'), ('7', '8'), ('8', '8');
COMMIT;

-- ----------------------------
--  Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `value` varchar(100) NOT NULL COMMENT '数据值',
  `label` varchar(100) NOT NULL COMMENT '标签名',
  `type` varchar(100) NOT NULL COMMENT '类型',
  `description` varchar(100) NOT NULL COMMENT '描述',
  `sort` decimal(10,0) NOT NULL COMMENT '排序（升序）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`label`),
  KEY `sys_dict_del_flag` (`del_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典表';

-- ----------------------------
--  Records of `sys_dict`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES ('2', '9', '异常', 'log_type', '日志异常', '1', '2018-07-09 06:16:14', '2018-09-13 08:10:58', '日志异常', '0', '1'), ('3', '0', '正常', 'log_type', '正常', '0', '2018-07-09 06:15:40', '2018-07-09 06:15:40', '正常', '0', '0'), ('4', '123', '123', '1234', '123', '123', '2018-08-09 07:16:47', '2018-08-16 14:01:03', '123', '1', '0'), ('5', 'WX', '微信', 'social_type', '微信登录', '0', '2018-08-16 14:01:45', '2018-08-16 14:01:45', '微信登录', '0', '0'), ('6', 'QQ', 'QQ', 'social_type', 'QQ登录', '1', '2018-07-09 06:15:40', '2018-07-09 06:15:40', 'QQ登录', '0', '0'), ('7', '1', '正常', 'log_type', '日志类型', '0', '2018-09-30 02:33:53', '2018-09-30 02:33:53', '日志正常', '0', '1'), ('8', '0', '未提交', 'leave_status', '请假状态', '0', '2018-09-30 02:34:45', '2018-09-30 02:34:45', '请假状态', '0', '1'), ('9', '1', '审批中', 'leave_status', '请假状态', '1', '2018-09-30 02:35:16', '2018-09-30 02:35:16', '请假状态', '0', '1'), ('10', '2', '完成', 'leave_status', '请假状态', '2', '2018-09-30 02:35:58', '2018-09-30 02:35:58', '请假状态', '0', '1'), ('11', '9', '驳回', 'leave_status', '请假状态', '9', '2018-09-30 02:36:31', '2018-09-30 02:36:31', '请假状态', '0', '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` char(1) DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) DEFAULT '' COMMENT '日志标题',
  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `time` mediumtext COMMENT '执行时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  `exception` text COMMENT '异常信息',
  `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='日志表';

-- ----------------------------
--  Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `name` varchar(32) NOT NULL COMMENT '菜单名称',
  `permission` varchar(32) DEFAULT NULL COMMENT '菜单权限标识',
  `path` varchar(128) DEFAULT NULL COMMENT '前端URL',
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `component` varchar(64) DEFAULT NULL COMMENT 'VUE页面',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `type` char(1) DEFAULT NULL COMMENT '菜单类型 （0菜单 1按钮）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '0--正常 1--删除',
  `tenant_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

-- ----------------------------
--  Records of `sys_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES ('1000', '权限管理', null, '/upms', '-1', 'icon-quanxianguanli', 'Layout', '0', '0', '2018-09-28 08:29:53', '2018-09-28 08:53:01', '0', '1'), ('1100', '用户管理', null, 'user', '1000', 'icon-yonghuguanli', 'views/admin/user/index', '1', '0', '2017-11-02 22:24:37', '2018-09-28 09:00:41', '0', '1'), ('1101', '用户新增', 'sys_user_add', null, '1100', null, null, null, '1', '2017-11-08 09:52:09', '2018-09-28 09:06:34', '0', '1'), ('1102', '用户修改', 'sys_user_edit', null, '1100', null, null, null, '1', '2017-11-08 09:52:48', '2018-09-28 09:06:37', '0', '1'), ('1103', '用户删除', 'sys_user_del', null, '1100', null, null, null, '1', '2017-11-08 09:54:01', '2018-09-28 09:06:42', '0', '1'), ('1200', '菜单管理', null, 'menu', '1000', 'icon-caidanguanli', 'views/admin/menu/index', '2', '0', '2017-11-08 09:57:27', '2018-09-28 09:00:45', '0', '1'), ('1201', '菜单新增', 'sys_menu_add', null, '1200', null, null, null, '1', '2017-11-08 10:15:53', '2018-09-28 09:07:16', '0', '1'), ('1202', '菜单修改', 'sys_menu_edit', null, '1200', null, null, null, '1', '2017-11-08 10:16:23', '2018-09-28 09:07:18', '0', '1'), ('1203', '菜单删除', 'sys_menu_del', null, '1200', null, null, null, '1', '2017-11-08 10:16:43', '2018-09-28 09:07:22', '0', '1'), ('1300', '角色管理', null, 'role', '1000', 'icon-jiaoseguanli', 'views/admin/role/index', '3', '0', '2017-11-08 10:13:37', '2018-09-28 09:00:48', '0', '1'), ('1301', '角色新增', 'sys_role_add', null, '1300', null, null, null, '1', '2017-11-08 10:14:18', '2018-09-28 09:07:46', '0', '1'), ('1302', '角色修改', 'sys_role_edit', null, '1300', null, null, null, '1', '2017-11-08 10:14:41', '2018-09-28 09:07:49', '0', '1'), ('1303', '角色删除', 'sys_role_del', null, '1300', null, null, null, '1', '2017-11-08 10:14:59', '2018-09-28 09:07:53', '0', '1'), ('1304', '分配权限', 'sys_role_perm', null, '1300', null, null, null, '1', '2018-04-20 07:22:55', '2018-09-28 09:13:23', '0', '1'), ('1400', '部门管理', null, 'dept', '1000', 'icon-web-icon-', 'views/admin/dept/index', '4', '0', '2018-01-20 13:17:19', '2018-09-28 09:00:52', '0', '1'), ('1401', '部门新增', 'sys_dept_add', null, '1400', null, null, null, '1', '2018-01-20 14:56:16', '2018-09-28 09:08:13', '0', '1'), ('1402', '部门修改', 'sys_dept_edit', null, '1400', null, null, null, '1', '2018-01-20 14:56:59', '2018-09-28 09:08:16', '0', '1'), ('1403', '部门删除', 'sys_dept_del', null, '1400', null, null, null, '1', '2018-01-20 14:57:28', '2018-09-28 09:08:18', '0', '1'), ('2000', '系统管理', null, '/admin', '-1', 'icon-xitongguanli', 'Layout', '1', '0', '2017-11-07 20:56:00', '2018-09-28 08:53:18', '0', '1'), ('2100', '日志管理', null, 'log', '2000', 'icon-rizhiguanli', 'views/admin/log/index', '5', '0', '2017-11-20 14:06:22', '2018-09-28 09:01:52', '0', '1'), ('2101', '日志删除', 'sys_log_del', null, '2100', null, null, null, '1', '2017-11-20 20:37:37', '2018-09-28 09:08:44', '0', '1'), ('2200', '字典管理', null, 'dict', '2000', 'icon-navicon-zdgl', 'views/admin/dict/index', '6', '0', '2017-11-29 11:30:52', '2018-09-28 09:01:47', '0', '1'), ('2201', '字典删除', 'sys_dict_del', null, '2200', null, null, null, '1', '2017-11-29 11:30:11', '2018-09-28 09:09:10', '0', '1'), ('2202', '字典新增', 'sys_dict_add', null, '2200', null, null, null, '1', '2018-05-11 22:34:55', '2018-09-28 09:09:12', '0', '1'), ('2203', '字典修改', 'sys_dict_edit', null, '2200', null, null, null, '1', '2018-05-11 22:36:03', '2018-09-28 09:09:16', '0', '1'), ('2300', '代码生成', '', 'gen', '2000', 'icon-weibiaoti46', 'views/admin/gen/index', '8', '0', '2018-01-20 13:17:19', '2018-09-28 09:02:54', '0', '1'), ('2400', '终端管理', '', 'client', '2000', 'icon-shouji', 'views/admin/client/index', '9', '0', '2018-01-20 13:17:19', '2018-09-28 09:01:43', '0', '1'), ('2401', '客户端新增', 'sys_client_add', null, '2400', '1', null, null, '1', '2018-05-15 21:35:18', '2018-09-28 09:10:25', '0', '1'), ('2402', '客户端修改', 'sys_client_edit', null, '2400', null, null, null, '1', '2018-05-15 21:37:06', '2018-09-28 09:10:27', '0', '1'), ('2403', '客户端删除', 'sys_client_del', null, '2400', null, null, null, '1', '2018-05-15 21:39:16', '2018-09-28 09:10:30', '0', '1'), ('2500', '密钥管理', '', 'social', '2000', 'icon-miyue', 'views/admin/social/index', '10', '0', '2018-01-20 13:17:19', '2018-09-28 09:01:41', '0', '1'), ('2501', '密钥新增', 'generator_syssocialdetails_add', null, '2500', '1', null, '0', '1', '2018-05-15 21:35:18', '2018-09-28 09:11:02', '0', '1'), ('2502', '密钥修改', 'generator_syssocialdetails_edit', null, '2500', '1', null, '1', '1', '2018-05-15 21:35:18', '2018-09-28 09:11:04', '0', '1'), ('2503', '密钥删除', 'generator_syssocialdetails_del', null, '2500', '1', null, '2', '1', '2018-05-15 21:35:18', '2018-09-28 09:11:06', '0', '1'), ('2600', '令牌管理', null, 'token', '2000', 'icon-denglvlingpai', 'views/admin/token/index', '11', '0', '2018-09-04 05:58:41', '2018-09-28 09:01:38', '0', '1'), ('2601', '令牌删除', 'sys_token_del', null, '2600', null, null, '1', '1', '2018-09-04 05:59:50', '2018-09-28 09:11:24', '0', '1'), ('2700', '动态路由', null, 'route', '2000', 'icon-luyou', 'views/admin/route/index', '12', '0', '2018-09-04 05:58:41', '2018-09-28 09:01:38', '0', '1'), ('3000', '系统监控', null, '/daemon', '-1', 'icon-msnui-supervise', 'Layout', '2', '0', '2018-07-27 01:13:21', '2018-09-28 08:53:24', '0', '1'), ('3100', '服务监控', null, 'http://139.224.200.249:15001', '3000', 'icon-server', null, '0', '0', '2018-06-26 10:50:32', '2018-09-28 09:03:40', '0', '1'), ('3200', '接口文档', null, 'http://139.224.200.249:19999/swagger-ui.html', '3000', 'icon-wendang', null, '1', '0', '2018-06-26 10:50:32', '2018-09-28 09:05:16', '0', '1'), ('3300', '事务监控', null, 'tx', '3000', 'icon-gtsquanjushiwufuwuGTS', 'views/admin/tx/index', '5', '0', '2018-08-19 11:02:39', '2018-09-28 09:03:36', '0', '1'), ('3400', '在线事务', null, 'model', '3000', 'icon-online', 'views/admin/tx/model', '6', '0', '2018-08-19 11:32:04', '2018-09-28 09:03:58', '0', '1'), ('3500', '任务监控', null, 'http://139.224.200.249:8899', '3000', 'icon-msnui-supervise', null, '7', '0', '2018-06-26 10:50:32', '2018-09-28 09:03:33', '0', '1'), ('3600', '任务轨迹', '', 'statustracelog', '3000', 'icon-guiji', 'views/daemon/statustracelog/index', '8', '0', '2018-01-20 13:17:19', '2018-09-28 09:04:15', '0', '1'), ('3601', '删除', 'daemon_statustracelog_del', null, '3600', '1', null, '2', '1', '2018-05-15 21:35:18', '2018-09-28 09:13:45', '0', '1'), ('3700', '调用拓扑', null, 'http://139.224.200.249:8081', '3000', 'icon-line', null, '10', '0', '2018-01-25 11:08:52', '2018-09-28 09:04:29', '0', '1'), ('3800', '缓存状态', null, 'http://139.224.200.249:8585', '3000', 'icon-qingchuhuancun', null, '12', '0', '2018-01-23 10:56:11', '2018-09-28 09:13:56', '0', '1'), ('4000', '协同管理', null, '/activti', '-1', 'icon-kuaisugongzuoliu_o', 'Layout', '3', '0', '2018-09-26 01:38:13', '2018-09-28 08:58:24', '0', '1'), ('4100', '模型管理', null, 'activiti', '4000', 'icon-weibiaoti13', 'views/admin/activiti/index', '1', '0', '2018-09-26 01:39:07', '2018-09-28 09:00:57', '0', '1'), ('4200', '流程管理', null, 'process', '4000', 'icon-liucheng', 'views/admin/activiti/process', '2', '0', '2018-09-26 06:41:05', '2018-09-28 09:01:02', '0', '1'), ('4300', '请假管理', '', 'leavebill', '4000', 'icon-qingjia', 'views/admin/activiti/leave', '3', '0', '2018-01-20 13:17:19', '2018-09-28 09:01:07', '0', '1'), ('4301', '请假新增', 'act_leavebill_add', null, '4300', '1', null, '0', '1', '2018-05-15 21:35:18', '2018-09-28 09:12:07', '0', '1'), ('4302', '请假修改', 'act_leavebill_edit', null, '4300', '1', null, '1', '1', '2018-05-15 21:35:18', '2018-09-28 09:12:09', '0', '1'), ('4303', '请假删除', 'act_leavebill_del', null, '4300', '1', null, '2', '1', '2018-05-15 21:35:18', '2018-09-28 09:12:14', '0', '1'), ('4400', '代办任务', null, 'task', '4000', 'icon-renwu', 'views/admin/activiti/task', '4', '0', '2018-09-27 09:52:31', '2018-09-28 09:04:19', '0', '1'), ('5000', '多级菜单', null, '/crud', '-1', 'icon-caidanguanli', 'Layout', '4', '0', '2018-08-28 01:50:22', '2018-09-28 08:58:20', '0', '1'), ('5001', '一级菜单', null, 'index', '5000', 'icon-caidanguanli', 'Layout', '1', '0', '2018-08-28 01:50:48', '2018-09-28 09:12:54', '0', '1'), ('5002', '二级菜单', null, 'index', '5000', 'icon-caidanguanli', 'views/crud/index', '1', '0', '2018-08-28 01:51:23', '2018-09-28 09:13:08', '0', '1'), ('6000', '系统官网', null, 'https://pig4cloud.com#', '-1', 'icon-guanwang', '', '5', '0', '2018-09-27 02:26:36', '2018-09-28 08:58:18', '0', '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_oauth_client_details`
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
  `client_id` varchar(32) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Records of `sys_oauth_client_details`
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` VALUES ('app', null, 'app', 'server', 'password,refresh_token', null, null, null, null, null, 'true', '0'), ('daemon', null, 'daemon', 'server', 'password,refresh_token', null, null, null, null, null, 'true', '0'), ('gen', null, 'gen', 'server', 'password,refresh_token', null, null, null, null, null, 'true', '0'), ('pig', null, 'pig', 'server', 'password,refresh_token', null, null, null, null, null, 'true', '0'), ('test', null, 'test', 'server', 'password,refresh_token', null, null, null, null, null, 'true', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `role_code` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `role_desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
  `tenant_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_idx1_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1', 'admin', 'ROLE_ADMIN', '超级管理员', '2017-10-29 15:45:51', '2018-09-13 01:46:01', '0', '1'), ('2', 'ROLE_CQQ', 'ROLE_CQQ', 'ROLE_CQQ', '2018-11-11 19:42:26', '2018-11-11 20:26:01', '0', '2');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

-- ----------------------------
--  Records of `sys_role_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES ('1', '1000'), ('1', '1100'), ('1', '1101'), ('1', '1102'), ('1', '1103'), ('1', '1200'), ('1', '1201'), ('1', '1202'), ('1', '1203'), ('1', '1300'), ('1', '1301'), ('1', '1302'), ('1', '1303'), ('1', '1304'), ('1', '1400'), ('1', '1401'), ('1', '1402'), ('1', '1403'), ('1', '2000'), ('1', '2100'), ('1', '2101'), ('1', '2200'), ('1', '2201'), ('1', '2202'), ('1', '2203'), ('1', '2300'), ('1', '2400'), ('1', '2401'), ('1', '2402'), ('1', '2403'), ('1', '2500'), ('1', '2501'), ('1', '2502'), ('1', '2503'), ('1', '2600'), ('1', '2601'), ('1', '2700'), ('1', '3000'), ('1', '3100'), ('1', '3200'), ('1', '3300'), ('1', '3400'), ('1', '3500'), ('1', '3600'), ('1', '3601'), ('1', '3700'), ('1', '3800'), ('1', '4000'), ('1', '4100'), ('1', '4200'), ('1', '4300'), ('1', '4301'), ('1', '4302'), ('1', '4303'), ('1', '4400'), ('1', '5000'), ('1', '5001'), ('1', '5002'), ('1', '6000'), ('2', '1000'), ('2', '1100'), ('2', '1101'), ('2', '1102'), ('2', '1103'), ('2', '1200'), ('2', '1201'), ('2', '1202'), ('2', '1203'), ('2', '1300'), ('2', '1301'), ('2', '1302'), ('2', '1303'), ('2', '1304'), ('2', '1400'), ('2', '1401'), ('2', '1402'), ('2', '1403');
COMMIT;

-- ----------------------------
--  Table structure for `sys_route_conf`
-- ----------------------------
DROP TABLE IF EXISTS `sys_route_conf`;
CREATE TABLE `sys_route_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `route_id` varchar(30) NOT NULL DEFAULT '' COMMENT '路由ID',
  `predicates` text COMMENT '断言',
  `filters` text COMMENT '过滤器',
  `uri` varchar(50) DEFAULT NULL,
  `order` int(2) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COMMENT='路由配置表';

-- ----------------------------
--  Records of `sys_route_conf`
-- ----------------------------
BEGIN;
INSERT INTO `sys_route_conf` VALUES ('1', 'pigx-activiti', '[{\"args\":{\"_genkey_0\":\"/act/**\"},\"name\":\"Path\"}]', '[]', 'lb://pigx-activiti', '0', '2018-11-07 20:18:16', '2018-11-07 20:23:04', '0'), ('2', 'pigx-auth', '[{\"args\":{\"_genkey_0\":\"/auth/**\"},\"name\":\"Path\"}]', '[{\"args\":{},\"name\":\"ValidateCodeGatewayFilter\"},{\"args\":{},\"name\":\"PasswordDecoderFilter\"}]', 'lb://pigx-auth', '0', '2018-11-07 20:18:16', '2018-11-07 20:23:13', '0'), ('3', 'pigx-codegen', '[{\"args\":{\"_genkey_0\":\"/gen/**\"},\"name\":\"Path\"}]', '[]', 'lb://pigx-codegen', '0', '2018-11-07 20:18:16', '2018-11-07 20:23:16', '0'), ('4', 'pigx-daemon', '[{\"args\":{\"_genkey_0\":\"/daemon/**\"},\"name\":\"Path\"}]', '[]', 'lb://pigx-daemon', '0', '2018-11-07 20:18:16', '2018-11-07 20:23:18', '0'), ('5', 'pigx-tx-manager', '[{\"args\":{\"_genkey_0\":\"/tx/**\"},\"name\":\"Path\"}]', '[]', 'lb://pigx-tx-manager', '0', '2018-11-07 20:18:16', '2018-11-07 20:23:21', '0'), ('6', 'pigx-upms', '[{\"args\":{\"_genkey_0\":\"/admin/**\"},\"name\":\"Path\"}]', '[{\"args\":{\"key-resolver\":\"#{@remoteAddrKeyResolver}\",\"redis-rate-limiter.replenishRate\":\"10\",\"redis-rate-limiter.burstCapacity\":\"20\"},\"name\":\"RequestRateLimiter\"},{\"args\":{\"name\":\"default\",\"fallbackUri\":\"forward:/fallback\"},\"name\":\"Hystrix\"}]', 'lb://pigx-upms', '0', '2018-11-07 20:18:16', '2018-11-07 20:23:24', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_social_details`
-- ----------------------------
DROP TABLE IF EXISTS `sys_social_details`;
CREATE TABLE `sys_social_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主鍵',
  `type` varchar(16) NOT NULL COMMENT '类型',
  `remark` varchar(64) DEFAULT NULL COMMENT '描述',
  `app_id` varchar(64) NOT NULL COMMENT 'appid',
  `app_secret` varchar(64) NOT NULL COMMENT 'app_secret',
  `redirect_url` varchar(128) DEFAULT NULL COMMENT '回调地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(50) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统社交登录账号表';

-- ----------------------------
--  Records of `sys_social_details`
-- ----------------------------
BEGIN;
INSERT INTO `sys_social_details` VALUES ('1', 'WX', '微信互联参数', 'wxd1678d3f83b1d83a', '6ddb043f94da5d2172926abe8533504f', 'daoweicloud.com', '2018-08-16 14:24:25', '2018-08-16 14:24:25', '0', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `salt` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '随机盐',
  `phone` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '简介',
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，1-删除',
  `wx_openid` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信openid',
  `qq_openid` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'QQ openid',
  `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`user_id`),
  KEY `user_wx_openid` (`wx_openid`),
  KEY `user_qq_openid` (`qq_openid`),
  KEY `user_idx1_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$RpFJjxYiXdEsAGnWp/8fsOetMuOON96Ntk/Ym2M/RKRyU0GZseaDC', null, '17034642888', null, '1', '2018-04-20 07:15:18', '2018-09-27 14:49:41', '0', 'o_0FT0uyg_H1vVy2H0JpSwlVGhWQ', null, '1'), ('2', 'admin', '$2a$10$RpFJjxYiXdEsAGnWp/8fsOetMuOON96Ntk/Ym2M/RKRyU0GZseaDC', null, '17034642888', null, '1', '2018-04-20 07:15:18', '2018-11-11 20:25:54', '0', 'o_0FT0uyg_H1vVy2H0JpSwlVGhWQ', null, '2');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
--  Records of `sys_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES ('1', '1'), ('2', '2');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
