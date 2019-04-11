package com.inspur.bigdata.manage.open.data.service.impl;

import java.util.List;
import java.util.Map;

import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inspur.bigdata.manage.open.data.dao.DataGroupMapper;
import com.inspur.bigdata.manage.open.data.data.DataGroup;
import com.inspur.bigdata.manage.open.data.service.IDataGroupService;

@Service("groupService")
public class GroupServiceImpl implements IDataGroupService {

	@Autowired
	private DataGroupMapper serviceGroupMapper;

	@Override
	public String insert(DataGroup group) {
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
	public void update(DataGroup group) {
		serviceGroupMapper.update(group);
	}

	@Override
	public DataGroup getById(String groupId) {
		return serviceGroupMapper.getById(groupId);
	}

	@Override
	public List<DataGroup> getGroupList(Map<String, Object> param) {
		return serviceGroupMapper.getGroupList(param);
	}
	
	@Override
	public boolean isRegisted(String parentId, String name) {
		DataGroup groupService = serviceGroupMapper.isRegisted(parentId, name);
		return null == groupService ? false : true;
	}

}
