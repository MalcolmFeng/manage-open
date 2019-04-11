package com.inspur.bigdata.manage.open.data.service.impl;

import com.inspur.bigdata.manage.open.data.dao.DataDefMapper;
import com.inspur.bigdata.manage.open.data.dao.DataTableColumnMapper;
import com.inspur.bigdata.manage.open.data.data.DataDef;
import com.inspur.bigdata.manage.open.data.data.DataTableColumn;
import com.inspur.bigdata.manage.open.data.service.IOpenDataService;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("openDataService")
@Transactional("mybatisTransactionManager")
public class OpenDataServiceImpl implements IOpenDataService {
    @Autowired
    private DataDefMapper dataDefMapper;

    @Autowired
    private DataTableColumnMapper dataTableColumnMapper;
    public List<DataDef> listDataDefs(Map map) {
        return dataDefMapper.listDataDefs(map);
    }
    public void addDataDef(DataDef def) {
        if(def==null) return;
        dataDefMapper.insert(def);
    }
    @Override
    public void updateDataDef(Map map) {
        dataDefMapper.updateStatusByID(map);
    }

    public DataDef getDataDef(String id) {
       return dataDefMapper.get(id);
    }
    public DataDef  getDataDef(Map map){
        return  dataDefMapper.getDataDef(map);
    }
    public DataDef  addDataDefForApi(DataDef def){
         dataDefMapper.insert(def);
         List<DataTableColumn> list=def.getColumnList();
         for(DataTableColumn column:list){
             column.setId(UUIDGenerator.getUUID().toString());
             column.setDtDataId(def.getId());
             dataTableColumnMapper.insert(column);
         }
        return def;
    }

    @Override
    public void update(DataDef info) {
        dataDefMapper.update(info);
    }

    @Override
    public void delete(String id) {
        dataDefMapper.delete(id);
}
}
