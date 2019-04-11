package com.inspur.bigdata.manage.open.data.service;

import com.inspur.bigdata.manage.open.data.data.DataDef;

import java.util.List;
import java.util.Map;

public interface IOpenDataService {
     List<DataDef> listDataDefs(Map map);
     void  updateDataDef(Map map);
     DataDef  getDataDef(String id);
     DataDef  getDataDef(Map map);
     DataDef  addDataDefForApi(DataDef def);

    void update(DataDef info);

    void delete(String id);
}
