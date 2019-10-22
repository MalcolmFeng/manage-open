package com.inspur.bigdata.manage.open.service.service.impl;

import com.inspur.bigdata.manage.open.service.dao.ServiceApplyMapper;
import com.inspur.bigdata.manage.open.service.data.ServiceApply;
import com.inspur.bigdata.manage.open.service.service.IServiceApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by songlili on 2019/2/13.
 */
@Service("serviceApplyService")
@Transactional("mybatisTransactionManager")
public class ServiceApplyImpl implements IServiceApplyService{
    @Autowired
    private ServiceApplyMapper serviceApplyMapper;

    @Override
   public List<ServiceApply> getServiceAuthList(Map<String, Object> parameters){
        return  serviceApplyMapper.getServiceAuthList(parameters);
    }
    @Override
    public List<ServiceApply> getServiceList(Map<String, Object> param){
        return  serviceApplyMapper.getServiceList(param);
    }
    @Override
    public List<ServiceApply> getApplyList(Map<String, Object> param){
        return  serviceApplyMapper.getApplyList(param);
    }

    @Override
    public String insert(ServiceApply serviceApply) {
        try{
            serviceApplyMapper.insert(serviceApply);
            return  "true";
        }catch (Exception e)
        {
            e.printStackTrace();
            return "false";
        }

    }
    @Override
    public ServiceApply getById(String id)
    {
        return serviceApplyMapper.get(id);
    }

    @Override
    public List<Map<String,Object>> getByBatchId(String id)
    {
        return serviceApplyMapper.getByBatchId(id);
    }

    @Override
    public void updateServiceApply(ServiceApply serviceApply){
        serviceApplyMapper.update(serviceApply);
    }

    @Override
    public List<ServiceApply> getByServiceId(String apiServiceId) {
        return serviceApplyMapper.getByServiceId(apiServiceId);
    }

    @Override
    public  List<ServiceApply> getListById(Map<String, Object> param) {
        return serviceApplyMapper.getListById(param);
    }

    public  List<ServiceApply> getAuthorizedApiListById(String id){
        return serviceApplyMapper.getAuthorizedApiListById(id);
    }

    public List<ServiceApply> getList(Map<String, Object> param){
        return serviceApplyMapper.getList(param);
    }

    @Override
    public List<ServiceApply> isApplyAuthToUser(Map<String, Object> map) {
        return serviceApplyMapper.isApplyAuthToUser(map);
    }

    public int updateById(ServiceApply serviceApply){
        return serviceApplyMapper.updateById(serviceApply);
    }

    @Override
    public List<ServiceApply> getAPIAuthList(Map<String, Object> parameters){
        return  serviceApplyMapper.getAPIAuthList(parameters);
    }
    @Override
    public void deleteApplyById(String id){
        serviceApplyMapper.deleteApplyById(id);
    }

}
