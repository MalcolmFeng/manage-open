package com.inspur.bigdata.manage.open.service.dao;

import com.inspur.bigdata.manage.open.service.data.ServiceInput;
import org.loushang.framework.mybatis.mapper.EntityMapper;

import java.util.List;
import java.util.Map;

public interface ServiceInputMapper  extends EntityMapper<ServiceInput> {
	List<ServiceInput> listServiceInputs(Map map);
	void deleteByApiId(String id);
}
