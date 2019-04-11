package com.inspur.bigdata.manage.open.cloud.mcs.service;

import com.inspur.bigdata.manage.open.cloud.mcs.data.BigDataServiceComponent;

import java.util.List;

public interface IBigDataServiceComponent {

	// 查询某个大数据计算服务产品包含的大数据服务
	List<BigDataServiceComponent> getBigDataServiceComponentsByServiceId(String serviceId);

}