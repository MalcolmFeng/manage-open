package com.inspur.bigdata.manage.open.service.service.impl;

import com.inspur.bigdata.manage.open.service.dao.ServiceGroupMapper;
import com.inspur.bigdata.manage.open.service.data.ServiceGroup;
import com.inspur.bigdata.manage.open.service.service.IServiceGroupService;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("serviceGroupService")
public class ServiceGroupServiceImpl implements IServiceGroupService {

	@Autowired
	private ServiceGroupMapper serviceGroupMapper;

	@Override
	public String insert(ServiceGroup group) {
		String groupId = UUIDGenerator.getUUID();
		group.setId(groupId);
		serviceGroupMapper.insert(group);
		return groupId;
	}

	@Override
	public void delete(String groupId) {
		serviceGroupMapper.delete(groupId);
	}

	@Override
	public void update(ServiceGroup group) {
		serviceGroupMapper.update(group);
	}

	@Override
	public ServiceGroup getById(String groupId) {
		return serviceGroupMapper.getById(groupId);
	}

	@Override
	public List<ServiceGroup> getGroupList(Map<String, Object> param) {
		return serviceGroupMapper.getGroupList(param);
	}
	
	@Override
	public boolean isRegisted(String parentId, String name) {
		ServiceGroup groupService = serviceGroupMapper.isRegisted(parentId, name);
		return null == groupService ? false : true;
	}

}
