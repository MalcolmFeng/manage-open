package com.inspur.bigdata.manage.open.data.dao;

import java.util.List;
import java.util.Map;

import com.inspur.bigdata.manage.open.data.data.DataGroup;

public interface DataGroupMapper {
	
	void insert(DataGroup group);

	void delete(String groupId);

	void update(DataGroup group);

	DataGroup getById(String groupId);

	List<DataGroup> getGroupList(Map<String, Object> param);

	DataGroup isRegisted(String parentId, String name);
}
