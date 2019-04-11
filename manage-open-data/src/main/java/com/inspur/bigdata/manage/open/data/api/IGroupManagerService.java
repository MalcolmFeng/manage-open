package com.inspur.bigdata.manage.open.data.api;

import java.util.List;
import java.util.Map;

public interface IGroupManagerService {


	void insert(Map<String, String> group);

	void delete(String groupId);

	void update(Map<String, String> group);

	Map<String, String> getById(String groupId);

	List<Map<String, String>> getGroupList(Map<String, Object> param);

	Map<String, Object> getGroupPage(Map<String, Object> param);
}
