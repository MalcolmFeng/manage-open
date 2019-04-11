package com.inspur.bigdata.manage.open.service.service;

import com.inspur.bigdata.manage.open.service.data.ServiceGroup;

import java.util.List;
import java.util.Map;

public interface IServiceGroupService {

	String insert(ServiceGroup group);

	void delete(String groupId);
	
	void update(ServiceGroup group);

	ServiceGroup getById(String groupId);

	List<ServiceGroup> getGroupList(Map<String, Object> param);

	boolean isRegisted(String parentId, String name);
}
