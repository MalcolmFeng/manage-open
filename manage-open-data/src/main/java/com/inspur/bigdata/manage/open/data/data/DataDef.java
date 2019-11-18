package com.inspur.bigdata.manage.open.data.data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Table(name="dt_data_def")
public class DataDef implements Serializable{

	@Id
	@Column(name = "ID")
	private String id;
	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String		name;
	/**
	 * 数据库名
	 */
	@Transient
	private String	dbName;
	/**
	 *表名
	 */
	@Column(name = "TABLE_NAME")
	private String    tableName;
	/**
	 *描述
	 */
	@Column(name = "DESCRIPTION")
	private String		description;
	/**
	 *审核状态
	 */
	@Column(name = "AUDIT_STATUS")
	private String auditStatus;
	/**
	 *审核人
	 */

	@Column(name="AUTH_TYPE")
	private String authType;
	@Column(name = "AUDIT_USER")
	private String  auditUser;
	/**
	 *创建时间
	 */
	@Column(name = "CREATE_TIME")
	private String createTime;
	/**
	 *上线时间
	 */
	@Column(name = "ONLINE_TIME")
	private String	 onlineTime;
	/**
	 *
	 */
	@Column(name = "STATUS")
	private String  status;
	/**
	 *分组id
	 */
	@Column(name = "GROUP_ID")
	private String		groupId;

	@Column(name = "PROVIDER")
	private String    provider;
	/**
	 *数据示例
	 */
	@Column(name = "DATA_EXAMPLE")
	private String		dataExample;
	/**
	 * 更新时间
	 */
	@Column(name = "UPDATE_TIME")
	private String  updateTime;
	/**
	 * 提供者名称
	 */
	@Column(name = "PROVIDER_NAME")
	private String  providerName;
	/**
	 * 是否需要用户授权 0 不需要 1 需要
	 */
	@Column(name = "NEED_USER_AUTH")
	private int needUserAuth;
	/**
	 * 数据所属域名
	 */
	@Column(name = "REALM")
	private String realm;

	/**
	 * 数据远程唯一标识
	 */
	@Column(name = "REMOTE_ID")
	private String remoteId;

	@Column(name="JDBC_URL")
	private String jdbcUrl;

	@Column(name="DATA_SOURCE_ID")
	private String dataSourceId;
	@Column(name="INSTANCE_NAME")
	private String instanceName;
	@Column(name="SERVICE_TYPE")
	private String serviceType;

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * 数据产品分析详情页面url
	 */
	@Column(name = "DATA_DETAIL_URL")
	private String dataDetailUrl;

	@Transient
	List<DataTableColumn> columnList;
	@Transient
	private List<Map> list;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getDataExample() {
		return dataExample;
	}

	public void setDataExample(String dataExample) {
		this.dataExample = dataExample;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public int getNeedUserAuth() {
		return needUserAuth;
	}

	public void setNeedUserAuth(int needUserAuth) {
		this.needUserAuth = needUserAuth;
	}


	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public List<DataTableColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<DataTableColumn> columnList) {
		this.columnList = columnList;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}


	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getDataDetailUrl() {
		return dataDetailUrl;
	}

	public void setDataDetailUrl(String dataDetailUrl) {
		this.dataDetailUrl = dataDetailUrl;
	}
}
