package com.inspur.bigdata.manage.open.service.data;

import com.inspur.bigdata.manage.common.utils.PropertiesUtil;
import com.inspur.bigdata.manage.utils.OpenDataConstants;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Table(name="api_service_def")
public class ServiceDef implements Serializable{

	@Id
	@Column(name = "ID")
	private String id;
	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String		name;
	/**
	 *描述
	 */
	@Column(name = "DESCRIPTION")
	private String		description;
	/**
	 *审核状态
	 */
	@Column(name = "AUDIT_STATUS")
	private String   auditStatus;
	/**
	 *审核人
	 */
	@Column(name = "AUDIT_USER")
	private String  auditUser;

	/**
	 *授权类型
	 */
	@Column(name="AUTH_TYPE")
	private String authType;



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
	 *分组id
	 */
	@Column(name = "GROUP_ID")
	private String		groupId;
	/**
	 *提供者
	 */
	@Column(name = "PROVIDER")
	private String    provider;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATE_TIME")
	private String  updateTime;

	/**
	 * 数据远程唯一标识
	 */
	@Column(name = "REMOTE_ID")
	private String remoteId;

	/**
	 * 通讯协议
	 */
	@Column(name = "PROTOCOL")
	private String protocol;
	/**
	 * 请求path
	 */
	@Column(name = "REQ_PATH")
	private String reqPath;

	/**
	 * 请求方法 get、post
	 */
	@Column(name = "HTTP_METHOD")
	private String httpMethod;
	/**
	 * 后端请求协议
	 */
	@Column(name = "SC_PROTOCOL")
	private String scProtocol;
	/**
	 * 后端请求方法 get、post
	 */
	@Column(name = "SC_HTTP_METHOD")
	private String scHttpMethod;
	/**
	 * 后端服务全路径
	 */
	@Column(name = "SC_ADDR")
	private String scAddr;
	/**
	 * 返回contentType
	 */
	@Column(name = "CONTENT_TYPE")
	private String contentType;
	/**
	 * 返回示例
	 */
	@Column(name = "RETURN_SAMPLE")
	private String returnSample;
	/**
	 * api 分组 对应DevGroup 类
	 */
	@Column(name = "API_GROUP")
	private String apiGroup;

	/**
	 * 价格
	 */
	@Column(name="price")
	private BigDecimal price;
	/**
	 * 收费类型 默认1
	 */
	@Column(name = "price_type")
	private String priceType;

	@Transient
	private List<ServiceInput> inputList;

	@Transient
	private List<ServiceOutput> outputList;
	/**
	 * 开放地址
	 */
	@Transient
	private String openAddr;

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


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getReqPath() {
		return reqPath;
	}

	public void setReqPath(String reqPath) {
		this.reqPath = reqPath;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public List<ServiceInput> getInputList() {
		return inputList;
	}

	public String getScProtocol() {
		return scProtocol;
	}

	public void setScProtocol(String scProtocol) {
		this.scProtocol = scProtocol;
	}

	public String getScHttpMethod() {
		return scHttpMethod;
	}

	public void setScHttpMethod(String scHttpMethod) {
		this.scHttpMethod = scHttpMethod;
	}

	public String getScAddr() {
		return scAddr;
	}

	public void setScAddr(String scAddr) {
		this.scAddr = scAddr;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getReturnSample() {
		return returnSample;
	}

	public void setReturnSample(String returnSample) {
		this.returnSample = returnSample;
	}

	public String getApiGroup() {
		return apiGroup;
	}

	public void setApiGroup(String apiGroup) {
		this.apiGroup = apiGroup;
	}

	public void setInputList(List<ServiceInput> inputList) {
		this.inputList = inputList;
	}

	public String getOpenAddr() {
		return openAddr;
	}

	public void setOpenAddr(String openAddr) {
		this.openAddr = openAddr;
	}

	public void setOutputList(List<ServiceOutput> outputList) {
		this.outputList = outputList;
	}

	public List<ServiceOutput> getOutputList() {
		return outputList;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
