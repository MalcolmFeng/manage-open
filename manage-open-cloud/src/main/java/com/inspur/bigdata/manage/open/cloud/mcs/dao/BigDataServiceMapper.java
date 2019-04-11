package com.inspur.bigdata.manage.open.cloud.mcs.dao;

import com.inspur.bigdata.manage.open.cloud.mcs.data.BigDataService;

import java.util.List;

public interface BigDataServiceMapper {

	// 查询所有大数据计算服务产品
	List<BigDataService> getAllBigDataServices();

}