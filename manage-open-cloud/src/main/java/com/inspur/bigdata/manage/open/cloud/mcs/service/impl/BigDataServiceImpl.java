package com.inspur.bigdata.manage.open.cloud.mcs.service.impl;

import com.inspur.bigdata.manage.open.cloud.mcs.dao.BigDataServiceMapper;
import com.inspur.bigdata.manage.open.cloud.mcs.data.BigDataService;
import com.inspur.bigdata.manage.open.cloud.mcs.service.IBigDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bigDataService")
public class BigDataServiceImpl implements IBigDataService {

	@Autowired
	private BigDataServiceMapper bigDataServiceMapper;

	public List<BigDataService> getAllBigDataServices() {
		return bigDataServiceMapper.getAllBigDataServices();
	}

}