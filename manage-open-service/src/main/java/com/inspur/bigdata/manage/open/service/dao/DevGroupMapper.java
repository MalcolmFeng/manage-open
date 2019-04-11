package com.inspur.bigdata.manage.open.service.dao;

import com.inspur.bigdata.manage.open.service.data.DevGroup;
import org.loushang.framework.mybatis.mapper.EntityMapper;

import java.util.List;
import java.util.Map;

public interface DevGroupMapper extends EntityMapper<DevGroup> {

	 int insert(DevGroup group);

	 void deletebyId(String groupId);

	 int update(DevGroup group);

	 DevGroup getById(String groupId);

	 List<DevGroup> getGroupList(Map<String, Object> param);

	 DevGroup isRegisted(String userId,String name);
	 DevGroup isExistedContext(String context);
}
