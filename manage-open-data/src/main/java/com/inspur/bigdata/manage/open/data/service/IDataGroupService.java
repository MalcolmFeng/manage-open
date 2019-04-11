package com.inspur.bigdata.manage.open.data.service;

import java.util.List;
import java.util.Map;

import com.inspur.bigdata.manage.open.data.data.DataGroup;

public interface IDataGroupService {

	String insert(DataGroup group);

	void delete(String groupId);
	
	void update(DataGroup group);

	DataGroup getById(String groupId);

	List<DataGroup> getGroupList(Map<String, Object> param);

	boolean isRegisted(String parentId, String name);
}
