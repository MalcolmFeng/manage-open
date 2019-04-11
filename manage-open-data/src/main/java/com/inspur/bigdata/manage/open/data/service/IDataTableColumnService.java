package com.inspur.bigdata.manage.open.data.service;

import com.inspur.bigdata.manage.open.data.data.DataTableColumn;

import java.util.List;
import java.util.Map;

public interface IDataTableColumnService {
     List<DataTableColumn> listTableColumns(Map map);
     List< DataTableColumn> listTableColumnsByDataId(String dataId);
}
