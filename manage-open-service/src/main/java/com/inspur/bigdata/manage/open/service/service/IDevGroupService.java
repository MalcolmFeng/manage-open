package com.inspur.bigdata.manage.open.service.service;

import com.inspur.bigdata.manage.open.service.data.DevGroup;

import java.util.List;
import java.util.Map;

public interface IDevGroupService {

	int insert(DevGroup group);

	void deletebyId(String id);
	
	void update(DevGroup group);

	DevGroup getById(String id);

	List<DevGroup> getGroupList(Map<String, Object> param);

	boolean isRegisted(String userId,String name);
	boolean isExistedContext(String context);
}
