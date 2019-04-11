package com.inspur.bigdata.manage.open.data.dao;

import com.inspur.bigdata.manage.open.data.data.DataDef;
import org.loushang.framework.mybatis.mapper.EntityMapper;

import java.util.List;
import java.util.Map;

public interface DataDefMapper extends EntityMapper<DataDef>{
	List<DataDef> listDataDefs(Map map);
	DataDef getDataDef(Map map);
	void updateStatusByID(Map map);
}
