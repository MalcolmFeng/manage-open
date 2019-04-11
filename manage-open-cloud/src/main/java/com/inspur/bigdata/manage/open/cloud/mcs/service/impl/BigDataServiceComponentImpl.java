package com.inspur.bigdata.manage.open.cloud.mcs.service.impl;

import com.inspur.bigdata.manage.open.cloud.mcs.dao.BigDataServiceComponentMapper;
import com.inspur.bigdata.manage.open.cloud.mcs.data.BigDataServiceComponent;
import com.inspur.bigdata.manage.open.cloud.mcs.service.IBigDataServiceComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bigDataServiceComponent")
public class BigDataServiceComponentImpl implements IBigDataServiceComponent {

	@Autowired
	private BigDataServiceComponentMapper bigDataServiceComponentMapper;

	public List<BigDataServiceComponent> getBigDataServiceComponentsByServiceId(String serviceId) {
		return bigDataServiceComponentMapper.getBigDataServiceComponentsByServiceId(serviceId);
	}

}