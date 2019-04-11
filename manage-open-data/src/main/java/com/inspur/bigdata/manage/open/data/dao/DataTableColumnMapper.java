package com.inspur.bigdata.manage.open.data.dao;

import com.inspur.bigdata.manage.open.data.data.DataTableColumn;
import org.loushang.framework.mybatis.mapper.EntityMapper;

import java.util.List;
import java.util.Map;

public interface DataTableColumnMapper extends EntityMapper<DataTableColumn>{
	List< DataTableColumn> listTableColumns(Map map);
	List< DataTableColumn> listTableColumnsByDataId(String dataId);
}
