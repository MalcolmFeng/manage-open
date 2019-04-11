package com.inspur.bigdata.manage.open.cloud.mcs.data;

public class MaxComputeServiceInstance {

	private String instanceId; // 32 位UUID值（36 位去掉4个”-“符号）instance_name
	private String instanceName; // 2-128个字符，以大小写字母或中文开头，可包含数字、“.”、“_”、“:”或“-”
	private String userId;
	private String serviceId; // 32 位UUID值（36 位去掉4个”-“符号），通过外键与bigdata_service表关联，为多对1的关系
	private String serviceVersion; // 大数据计算服务版本，如：MCS 1.5
	private Integer coreNum;
	private Integer memory; // 单位：M
	private Integer storageVolume; // 单位：GB，容量范围：20 GB-500 GB，默认为40GB
	private String serviceAddress; // 大数据计算服务实例访问地址，如：http:// 192.168.1.38/dev/?realm=realm1234
	private String serviceUsername; // 大数据计算服务实例访问用户名，如：tenant1234
	private String servicePasswd; // 大数据计算服务实例访问用户密码，如：tenant1234
	private String applyTime; // 用户申请大数据计算服务时间
	private String applyReason; // 用户申请大数据计算服务理由
	private String replyTime; // 管理员审核大数据计算服务申请时间
	private String auditOpinion; // 管理员审核大数据计算服务申请意见
	private String createTime; // 大数据计算服务实例创建时间
	private String updateTime; // 大数据计算服务实例进行某种操作后的完成时间，暂不使用
	private String applyStatus; // 申请状态：0-待审核，1-审批通过，2-审批驳回
	private String runStatus; // 运行状态：0-创建中，1-创建成功，2-创建失败，3-运行中，4-删除中，5-删除失败

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public Integer getCoreNum() {
		return coreNum;
	}

	public void setCoreNum(Integer coreNum) {
		this.coreNum = coreNum;
	}

	public Integer getMemory() {
		return memory;
	}

	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	public Integer getStorageVolume() {
		return storageVolume;
	}

	public void setStorageVolume(Integer storageVolume) {
		this.storageVolume = storageVolume;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
	}

	public String getServicePasswd() {
		return servicePasswd;
	}

	public void setServicePasswd(String servicePasswd) {
		this.servicePasswd = servicePasswd;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

}