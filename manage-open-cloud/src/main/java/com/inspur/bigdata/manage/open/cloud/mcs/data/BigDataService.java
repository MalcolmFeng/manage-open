package com.inspur.bigdata.manage.open.cloud.mcs.data;

public class BigDataService {

	private String serviceId; // 32位UUID值（36位去掉4个”-“符号）
	private String serviceName; // 大数据服务产品名称，如：MCS-大数据计算服务
	private String serviceVersion; // 大数据服务产品版本，如：MCS 1.5-大数据计算服务V1.5版
	private String serviceDesc; // 大数据服务产品描述

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

}