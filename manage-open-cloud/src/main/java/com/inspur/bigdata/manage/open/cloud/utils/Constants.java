package com.inspur.bigdata.manage.open.cloud.utils;

public interface Constants {

	// 项目配置文件
	String CONF_PROPERTIES = "conf.properties";

	// Keycloak配置文件
	String KEYCLOAK_JSON = "keycloak.json";

	String RESULT_FAILED = "false";

	String RESULT_SUCCEED = "true";

	// 服务审核状态 0待审核 1通过 2驳回
	String APPLY_STATUS_INIT           = "0"; // 待审核
	String APPLY_STATUS_PASS           = "1"; // 审批通过
	String APPLY_STATUS_REJECT         = "2"; // 审批驳回

    // 大数据计算服务运行状态 0-创建中，1-创建成功，2-创建失败，3-运行中，4-删除中，5-删除失败
	String MCS_INSTANCE_DEPLOYING   = "0"; // 创建中
	String MCS_INSTANCE_DEPLOY_SUCCESS = "1"; // 创建成功
	String MCS_INSTANCE_DEPLOY_FAILED  = "2"; // 创建失败
	String MCS_INSTANCE_RUNNING = "3"; // 运行中
	String MCS_INSTANCE_DELETING  = "4"; // 删除中
	String MCS_INSTANCE_DELETE_FAILED  = "5"; // 删除失败

	// 关系数据库服务运行状态 0-创建中，1-创建成功，2-创建失败，3-运行中，4-启动中，5-启动失败，6-停止中，7-停止失败，8-删除中，9-删除失败
	String RDS_INSTANCE_DEPLOYING   = "0"; // 创建中
	String RDS_INSTANCE_DEPLOY_SUCCESS = "1"; // 创建成功
	String RDS_INSTANCE_DEPLOY_FAILED  = "2"; // 创建失败
	String RDS_INSTANCE_RUNNING = "3"; // 运行中
	String RDS_INSTANCE_STARTING = "4"; // 启动中
	String RDS_INSTANCE_START_FAILED = "5"; // 启动失败
	String RDS_INSTANCE_STOPPING = "6"; // 停止中
	String RDS_INSTANCE_STOP_FAILED = "7"; // 停止失败
	String RDS_INSTANCE_DELETING  = "8"; // 删除中
	String RDS_INSTANCE_DELETE_FAILED  = "9"; // 删除失败

	// 分布式消息服务运行状态 0-创建中，1-创建成功，2-创建失败，3-运行中，4-删除中，5-删除失败
	String DMS_INSTANCE_DEPLOYING   = "0"; // 创建中
	String DMS_INSTANCE_DEPLOY_SUCCESS = "1"; // 创建成功
	String DMS_INSTANCE_DEPLOY_FAILED  = "2"; // 创建失败
	String DMS_INSTANCE_RUNNING = "3"; // 运行中
	String DMS_INSTANCE_DELETING  = "4"; // 删除中
	String DMS_INSTANCE_DELETE_FAILED  = "5"; // 删除失败

	// Oracle驱动
	String ORACLE_JDBC_DRIVER   = "oracle.jdbc.driver.OracleDriver";

	// 服务操作
	String METHOD_CREATE    = "passInstance"; // 创建
	String METHOD_DELETE    = "delete"; // 删除

}