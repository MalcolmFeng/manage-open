package com.inspur.bigdata.manage.open.cloud.mcs.data;

public class BigDataServiceComponent {

	private String serviceId; // 32位UUID值（36位去掉4个”-“符号），通过外键与bigdata_service表关联，为多对1的关系
	private String componentName; // 大数据组件名称，如：Hadoop
	private String componentVersion; // 大数据组件版本，如：Hadoop V2.7.3
	private String componentDesc; // 大数据组件描述，如：Haoop-针对大数据集的分布式数据处理框架

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentVersion() {
		return componentVersion;
	}

	public void setComponentVersion(String componentVersion) {
		this.componentVersion = componentVersion;
	}

	public String getComponentDesc() {
		return componentDesc;
	}

	public void setComponentDesc(String componentDesc) {
		this.componentDesc = componentDesc;
	}

}