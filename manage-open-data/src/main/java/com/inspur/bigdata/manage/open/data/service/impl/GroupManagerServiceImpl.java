package com.inspur.bigdata.manage.open.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.loushang.framework.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inspur.bigdata.manage.open.data.api.IGroupManagerService;
import com.inspur.bigdata.manage.open.data.data.DataGroup;
import com.inspur.bigdata.manage.open.data.service.IDataGroupService;

@Service("groupManagerService")
public class GroupManagerServiceImpl implements IGroupManagerService {

	@Autowired
	private IDataGroupService groupService;

	@Override
	public void insert(Map<String, String> group) {
		DataGroup serviceGroup = map2bean(group);
		groupService.insert(serviceGroup);
	}

	@Override
	public void delete(String groupId) {
		groupService.delete(groupId);
	}

	@Override
	public void update(Map<String, String> group) {
		DataGroup serviceGroup = map2bean(group);
		groupService.update(serviceGroup);
	}

	@Override
	public Map<String, String> getById(String groupId) {
		DataGroup group = groupService.getById(groupId);
		return bean2map(group);
	}

	@Override
	public List<Map<String, String>> getGroupList(Map<String, Object> param) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		List<DataGroup> list = groupService.getGroupList(param);
		for (DataGroup serviceGroup : list) {
			data.add(bean2map(serviceGroup));
		}
		return data;
	}

	@Override
	public Map<String, Object> getGroupPage(Map<String, Object> param) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		List<DataGroup> list = groupService.getGroupList(param);
		for (DataGroup serviceGroup : list) {
			data.add(bean2map(serviceGroup));
		}

		int total = PageUtil.getTotalCount();
		if (total <= 0) {
			total = data.size();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", data);
		result.put("total", total);
		return result;
	}

	private DataGroup map2bean(Map<String, String> group) {
		DataGroup serviceGroup = new DataGroup();
		serviceGroup.setId(group.get("id"));
		serviceGroup.setName(group.get("name"));
		serviceGroup.setParentId(group.get("parentId"));
		serviceGroup.setSeq(Integer.valueOf(group.get("seq")));

		return serviceGroup;
	}

	private Map<String, String> bean2map(DataGroup group) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("id", group.getId());
		result.put("name", group.getName());
		result.put("parentId", group.getParentId());
		result.put("seq", String.valueOf(group.getSeq()));

		return result;
	}

}
