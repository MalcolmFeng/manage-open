package com.inspur.bigdata.manage.open.service.dao;

import com.inspur.bigdata.manage.open.service.data.DevGroup;
import com.inspur.bigdata.manage.open.service.data.ServiceApply;
import org.loushang.framework.mybatis.mapper.EntityMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by songlili on 2019/2/13.
 */
public interface ServiceApplyMapper extends EntityMapper<ServiceApply> {

    List<ServiceApply> getServiceAuthList(Map<String, Object> parameters);

    List<ServiceApply> getServiceList(Map<String, Object> parameters);
    List<ServiceApply> getApplyList(Map<String, Object> parameters);
    int insert(ServiceApply serviceApply);
    List<ServiceApply> getByServiceId(String apiServiceId);
    List<ServiceApply> getListById(Map<String, Object> parameters);
    ServiceApply getById(String id);
    List<ServiceApply> getAuthorizedApiListById(String id);
    List<ServiceApply> getList(Map<String, Object> param);
    List<ServiceApply> isApplyAuthToUser(Map<String, Object> map);
    int updateById(ServiceApply serviceApply);

    List<ServiceApply> getAPIAuthList(Map<String, Object> parameters);

    void deleteApplyById(String id);

    List<Map<String,Object>> getByBatchId(String id);
}
