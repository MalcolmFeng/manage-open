package com.inspur.bigdata.manage.open.service.dao;

import com.inspur.bigdata.manage.open.service.data.AppInstance;
import com.inspur.bigdata.manage.open.service.data.ServiceDef;
import org.loushang.framework.mybatis.mapper.EntityMapper;

import java.util.Map;
import java.util.List;

/**
 * Created by songlili on 2019/2/12.
 */
public interface ServiceDefMapper extends EntityMapper<ServiceDef> {

    List<ServiceDef> listServiceDefs(Map map);

    ServiceDef getById(String id);

    void audit(ServiceDef serviceDef);

    void deleteById(String id);

    List<ServiceDef> getByApiGroupAndPath(Map map);

    List<ServiceDef> listAPIByProvider(Map map);

    List<ServiceDef> queryByRemoteId(String remoteId);

}
