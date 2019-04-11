package com.inspur.bigdata.manage.open.service.service;

import com.inspur.bigdata.manage.open.service.data.AppInstance;
import com.inspur.bigdata.manage.open.service.data.ServiceDef;

import java.util.List;
import java.util.Map;

/**
 * Created by songlili on 2019/2/12.
 */
public interface IServiceDefService {
    List<ServiceDef> listServiceDefs(Map map);

    ServiceDef getServiceDef(String id);

    void update(ServiceDef serviceDef);

    void addServiceDef(ServiceDef serviceDef);

    void updateServiceDef(ServiceDef serviceDef);
    void deleteById(String id);
    List<ServiceDef> getByApiGroupAndPath(Map map);
    List<ServiceDef> listAPIByProvider(Map map);
}
