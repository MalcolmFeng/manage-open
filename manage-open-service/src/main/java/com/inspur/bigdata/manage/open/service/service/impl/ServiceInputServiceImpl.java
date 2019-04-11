package com.inspur.bigdata.manage.open.service.service.impl;

import com.inspur.bigdata.manage.open.service.dao.ServiceInputMapper;
import com.inspur.bigdata.manage.open.service.data.ServiceInput;
import com.inspur.bigdata.manage.open.service.service.IServiceInputService;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("serviceInputService")
public class ServiceInputServiceImpl implements IServiceInputService {

	@Autowired
	private ServiceInputMapper serviceInputMapper;

	@Override
	public List<ServiceInput> listByServiceId(String serviceId) {
		Map<String,Object> map=new HashMap<>();
		map.put("apiServiceId",serviceId);
		return serviceInputMapper.listServiceInputs(map);
	}
}
