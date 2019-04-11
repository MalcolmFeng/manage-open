/*
Navicat MySQL Data Transfer

Source Server         : 10.111.24.95
Source Server Version : 50634
Source Host           : 10.111.24.95:3306
Source Database       : indata

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2019-03-04 17:28:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for api_developer_group
-- ----------------------------
DROP TABLE IF EXISTS `api_developer_group`;
CREATE TABLE `api_developer_group` (
  `id` varchar(32) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `context` varchar(100) DEFAULT NULL,
  `userId` varchar(32) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_time` varchar(32) DEFAULT NULL,
  `update_time` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for api_service_apply
-- ----------------------------
DROP TABLE IF EXISTS `api_service_apply`;
CREATE TABLE `api_service_apply` (
  `ID` varchar(255) NOT NULL,
  `APP_ID` varchar(32) DEFAULT NULL,
  `APP_NAME` varchar(100) DEFAULT NULL,
  `API_SERVICE_ID` varchar(32) DEFAULT NULL,
  `API_SERVICE_NAME` varchar(64) DEFAULT NULL,
  `API_PROVIDER` varchar(64) DEFAULT NULL,
  `APPLICANT` varchar(255) DEFAULT NULL,
  `APPLY_TIME` varchar(32) DEFAULT NULL,
  `AUTH_STATUS` char(2) DEFAULT NULL,
  `AUTH_TIME` varchar(32) DEFAULT NULL,
  `AUTH_USER` varchar(30) DEFAULT NULL,
  `APPLY_FLAG` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for api_service_def
-- ----------------------------
DROP TABLE IF EXISTS `api_service_def`;
CREATE TABLE `api_service_def` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(200) DEFAULT NULL,
  `AUDIT_STATUS` varchar(10) DEFAULT NULL,
  `AUDIT_USER` varchar(30) DEFAULT NULL,
  `AUTH_TYPE` char(1) DEFAULT NULL,
  `CREATE_TIME` varchar(30) DEFAULT NULL,
  `ONLINE_TIME` varchar(30) DEFAULT NULL,
  `GROUP_ID` varchar(32) DEFAULT NULL,
  `PROVIDER` varchar(30) DEFAULT NULL,
  `UPDATE_TIME` varchar(30) DEFAULT NULL,
  `REMOTE_ID` varchar(32) DEFAULT NULL,
  `PROTOCOL` varchar(10) DEFAULT NULL,
  `REQ_PATH` varchar(20) DEFAULT NULL,
  `HTTP_METHOD` varchar(10) DEFAULT NULL,
  `sc_protocol` varchar(10) DEFAULT NULL,
  `sc_http_method` varchar(10) DEFAULT NULL,
  `sc_addr` varchar(100) DEFAULT NULL,
  `content_type` varchar(50) DEFAULT NULL,
  `return_sample` varchar(4000) DEFAULT NULL,
  `api_group` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for api_service_group
-- ----------------------------
DROP TABLE IF EXISTS `api_service_group`;
CREATE TABLE `api_service_group` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `PARENT_ID` varchar(32) DEFAULT NULL,
  `SEQ` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for api_service_input
-- ----------------------------
DROP TABLE IF EXISTS `api_service_input`;
CREATE TABLE `api_service_input` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `name` varchar(100) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `required` char(1) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `sc_name` varchar(100) DEFAULT NULL,
  `sc_type` varchar(20) DEFAULT NULL,
  `sc_required` char(1) DEFAULT NULL,
  `sc_description` varchar(200) DEFAULT NULL,
  `sc_seq` char(1) DEFAULT NULL,
  `api_service_id` varchar(32) DEFAULT NULL,
  `sc_param_type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for app_info
-- ----------------------------
DROP TABLE IF EXISTS `app_info`;
CREATE TABLE `app_info` (
  `APP_ID` varchar(32) NOT NULL COMMENT '应用ID',
  `APP_NAME` varchar(50) DEFAULT NULL COMMENT '应用名称',
  `APP_DESCRIPTION` varchar(255) DEFAULT NULL,
  `APP_KEY` varchar(32) DEFAULT NULL COMMENT '应用key',
  `APP_SECRET` varchar(255) DEFAULT NULL COMMENT '应用SECRET',
  `APP_CREATE_TIME` varchar(30) DEFAULT NULL,
  `APP_UPDATE_TIME` varchar(30) DEFAULT NULL,
  `USER_ID` varchar(32) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`APP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用id';

-- ----------------------------
-- Table structure for dt_data_apply
-- ----------------------------
DROP TABLE IF EXISTS `dt_data_apply`;
CREATE TABLE `dt_data_apply` (
  `ID` varchar(32) NOT NULL,
  `DT_DATA_ID` varchar(32) NOT NULL,
  `APPLICANT` varchar(255) DEFAULT NULL,
  `APPLICANT_NAME` varchar(255) DEFAULT NULL,
  `APPLY_TIME` varchar(30) DEFAULT NULL,
  `AUTH_STATUS` char(2) DEFAULT NULL,
  `AUTH_TIME` varchar(30) DEFAULT NULL,
  `AUTH_USER` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dt_data_def
-- ----------------------------
DROP TABLE IF EXISTS `dt_data_def`;
CREATE TABLE `dt_data_def` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `AUDIT_STATUS` varchar(10) DEFAULT NULL,
  `AUDIT_USER` varchar(30) DEFAULT NULL,
  `AUTH_TYPE` char(1) DEFAULT NULL,
  `AUTH_USER` varchar(32) DEFAULT NULL,
  `CREATE_TIME` varchar(30) DEFAULT NULL,
  `ONLINE_TIME` varchar(30) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `GROUP_ID` varchar(32) DEFAULT NULL,
  `PROVIDER` varchar(30) DEFAULT NULL,
  `DATA_EXAMPLE` text,
  `UPDATE_TIME` varchar(30) DEFAULT NULL,
  `PROVIDER_NAME` varchar(30) DEFAULT NULL,
  `NEED_USER_AUTH` char(1) DEFAULT NULL,
  `REALM` varchar(20) DEFAULT NULL,
  `REMOTE_ID` varchar(32) DEFAULT NULL,
  `JDBC_URL` varchar(255) DEFAULT NULL,
  `DATA_SOURCE_ID` varchar(255) DEFAULT NULL,
  `INSTANCE_NAME` varchar(100) DEFAULT NULL,
  `TABLE_NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dt_data_group
-- ----------------------------
DROP TABLE IF EXISTS `dt_data_group`;
CREATE TABLE `dt_data_group` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `PARENT_ID` varchar(32) DEFAULT NULL,
  `SEQ` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dt_table_columns
-- ----------------------------
DROP TABLE IF EXISTS `dt_table_columns`;
CREATE TABLE `dt_table_columns` (
  `ID` varchar(32) DEFAULT NULL,
  `DT_DATA_ID` varchar(32) DEFAULT NULL,
  `COLUMN_NAME` varchar(30) DEFAULT NULL,
  `COLUMN_TYPE` varchar(20) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `IS_NULL` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bigdata_service
-- ----------------------------
DROP TABLE IF EXISTS `bigdata_service`;
CREATE TABLE `bigdata_service` (
  `service_id` varchar(32) NOT NULL COMMENT '32位UUID值（36位去掉4个”-“符号）',
  `service_name` varchar(255) DEFAULT NULL COMMENT '大数据服务产品名称，如：MCS-大数据计算服务',
  `service_version` varchar(5) DEFAULT NULL COMMENT '大数据服务产品版本，如：MCS 1.5-大数据计算服务V1.5版',
  `service_desc` varchar(255) DEFAULT NULL COMMENT '大数据服务产品描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='大数据计算服务产品表';

-- ----------------------------
-- Records of bigdata_service
-- ----------------------------
INSERT INTO `bigdata_service` VALUES ('3cff13392c1c41afaf63b593e539509b', 'MCS', '1.5', '大数据计算服务V1.5版');
INSERT INTO `bigdata_service` VALUES ('d38dee61595e4fffb9d741f61a4e89a1', 'MCS', '1.3', '大数据计算服务V1.3版');

-- ----------------------------
-- Table structure for bigdata_service_component
-- ----------------------------
DROP TABLE IF EXISTS `bigdata_service_component`;
CREATE TABLE `bigdata_service_component` (
  `service_id` varchar(32) NOT NULL COMMENT '32位UUID值（36位去掉4个”-“符号），通过外键与bigdata_service表关联，为多对1的关系',
  `component_name` varchar(255) DEFAULT NULL COMMENT '大数据组件名称，如：Hadoop',
  `component_version` varchar(5) DEFAULT NULL COMMENT '大数据组件版本，如：Hadoop V2.7.3',
  `component_desc` varchar(255) DEFAULT NULL COMMENT '大数据组件描述，如：Haoop-针对大数据集的分布式数据处理框架'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='大数据服务组件表';

-- ----------------------------
-- Records of bigdata_service_component
-- ----------------------------
INSERT INTO `bigdata_service_component` VALUES ('d38dee61595e4fffb9d741f61a4e89a1', 'Hadoop123', '3.0.0', '针对大数据集的分布式数据处理框架');
INSERT INTO `bigdata_service_component` VALUES ('d38dee61595e4fffb9d741f61a4e89a1', 'Hive', '3.0.0', '提供数据汇聚和即席查询的数据仓库');
INSERT INTO `bigdata_service_component` VALUES ('3cff13392c1c41afaf63b593e539509b', 'Hadoop', '2.7.3', '针对大数据集的分布式数据处理框架');
INSERT INTO `bigdata_service_component` VALUES ('3cff13392c1c41afaf63b593e539509b', 'Hive', '1.2.1', '提供数据汇聚和即席查询的数据仓库');
INSERT INTO `bigdata_service_component` VALUES ('d38dee61595e4fffb9d741f61a4e89a1', 'Spark', '3.0.0', '快速、通用的大数据处理引擎');
INSERT INTO `bigdata_service_component` VALUES ('3cff13392c1c41afaf63b593e539509b', 'Spark', '2.3.0', '快速、通用的大数据处理引擎');
INSERT INTO `bigdata_service_component` VALUES ('d38dee61595e4fffb9d741f61a4e89a1', '数据开发套件', '2.0.0', '易于使用、功能强大而且可靠的数据处理和分发系统');
INSERT INTO `bigdata_service_component` VALUES ('3cff13392c1c41afaf63b593e539509b', '数据开发套件', '2.0.0', '易于使用、功能强大而且可靠的数据处理和分发系统');
-- ----------------------------
-- Table structure for mcs_instance
-- ----------------------------
DROP TABLE IF EXISTS `mcs_instance`;
CREATE TABLE `mcs_instance` (
  `instance_id` varchar(32) NOT NULL COMMENT '32位UUID值（36位去掉4个”-“符号）',
  `instance_name` varchar(255) DEFAULT NULL COMMENT '2-128个字符，以大小写字母或中文开头，可包含数字、“.”、“_”、“:”或“-”',
  `user_id` varchar(30) DEFAULT NULL,
  `service_id` varchar(32) NOT NULL COMMENT '32位UUID值（36位去掉4个”-“符号），通过外键与bigdata_service表关联，为多对1的关系',
  `service_version` varchar(10) DEFAULT NULL COMMENT '大数据计算服务版本，如：MCS 1.5',
  `core_num` int(2) DEFAULT NULL,
  `memory` int(5) DEFAULT NULL COMMENT '单位：M',
  `storage_volume` int(3) DEFAULT NULL COMMENT '单位：GB，容量范围：20GB-500GB，默认为40GB',
  `service_address` varchar(255) DEFAULT NULL COMMENT '大数据计算服务实例访问地址，如：http://192.168.1.38/dev/?realm=realm1234',
  `service_username` varchar(255) DEFAULT NULL COMMENT '大数据计算服务实例访问用户名，如：tenant1234',
  `service_passwd` varchar(255) DEFAULT NULL COMMENT '大数据计算服务实例访问用户密码，如：tenant1234',
  `apply_time` char(19) DEFAULT NULL COMMENT '用户申请云主机时间',
  `apply_reason` varchar(255) DEFAULT NULL COMMENT '用户申请云主机理由',
  `reply_time` char(19) DEFAULT NULL COMMENT '管理员审核云主机申请时间',
  `audit_opinion` varchar(255) DEFAULT NULL COMMENT '管理员审核云主机申请意见',
  `create_time` char(19) DEFAULT NULL COMMENT '大数据计算服务实例创建时间',
  `update_time` char(19) DEFAULT NULL COMMENT '大数据计算服务实例进行某种操作后的完成时间，暂不使用',
  `apply_status` char(1) DEFAULT NULL COMMENT '申请状态：0-待审核，1-审批通过，2-审批驳回',
  `run_status` char(1) DEFAULT NULL COMMENT '运行状态：0-创建中，1-创建成功，2-创建失败，3-运行中，4-删除中，5-删除失败'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='大数据计算服务实例表';