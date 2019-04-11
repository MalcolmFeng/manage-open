package com.inspur.bigdata.manage.open.data.service.impl;

import com.inspur.bigdata.manage.open.data.dao.DataDefMapper;
import com.inspur.bigdata.manage.open.data.dao.DataTableColumnMapper;
import com.inspur.bigdata.manage.open.data.data.DataDef;
import com.inspur.bigdata.manage.open.data.data.DataTableColumn;
import com.inspur.bigdata.manage.open.data.service.IDataTableColumnService;
import com.inspur.bigdata.manage.open.data.service.IOpenDataService;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("dataTableColumnService")
@Transactional("mybatisTransactionManager")
public class DataTableColumnServiceImpl implements IDataTableColumnService {
    @Autowired
    private DataTableColumnMapper dataTableColumnMapper;

    @Override
    public List<DataTableColumn> listTableColumns(Map map) {
        return dataTableColumnMapper.listTableColumns(map);
    }

    @Override
    public List<DataTableColumn> listTableColumnsByDataId(String dataId) {
        return dataTableColumnMapper.listTableColumnsByDataId(dataId);
    }
}
