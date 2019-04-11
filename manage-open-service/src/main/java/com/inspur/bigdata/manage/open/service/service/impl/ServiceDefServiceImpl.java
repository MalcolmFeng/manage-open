package com.inspur.bigdata.manage.open.service.service.impl;

import com.inspur.bigdata.manage.open.service.dao.ServiceDefMapper;
import com.inspur.bigdata.manage.open.service.dao.ServiceInputMapper;
import com.inspur.bigdata.manage.open.service.data.ServiceDef;
import com.inspur.bigdata.manage.open.service.data.ServiceInput;
import com.inspur.bigdata.manage.open.service.service.IServiceDefService;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by songlili on 2019/2/12.
 */
@Service("serviceDefService")
@Transactional("mybatisTransactionManager")
public class ServiceDefServiceImpl implements IServiceDefService{
    @Autowired
    private ServiceDefMapper serviceDefMapper;
    @Autowired
    private ServiceInputMapper serviceInputMapper;

    @Override
    public List<ServiceDef> listServiceDefs(Map data){
       return serviceDefMapper.listServiceDefs(data);
    }

    @Override
    public ServiceDef getServiceDef(String id){
        return serviceDefMapper.getById(id);
    }

    @Override
    public void update(ServiceDef serviceDef){
        serviceDefMapper.audit(serviceDef);
    }
    @Override
    /**
     * 页面新增保存
     */
    public void addServiceDef(ServiceDef serviceDef) {
        List<ServiceInput> list=serviceDef.getInputList();
        serviceDefMapper.insert(serviceDef);
        for(ServiceInput input:list){
            input.setId(UUIDGenerator.getUUID().toString());
            input.setApiServiceId(serviceDef.getId());
            serviceInputMapper.insert(input);
        }
    }

    @Override
    /**
     * 页面编辑保存
     */
    public void updateServiceDef(ServiceDef serviceDef) {
        List<ServiceInput> list=serviceDef.getInputList();
        serviceDefMapper.update(serviceDef);
        //删除以前所有
        serviceInputMapper.deleteByApiId(serviceDef.getId());
        //重新添加
        for(ServiceInput input:list){
            input.setId(UUIDGenerator.getUUID().toString());
            input.setApiServiceId(serviceDef.getId());
            serviceInputMapper.insert(input);
        }
    }

    @Override
    public void deleteById(String id) {
        serviceDefMapper.deleteById(id);
    }

    @Override
    public List<ServiceDef> getByApiGroupAndPath(Map map) {
        return serviceDefMapper.getByApiGroupAndPath(map);
    }

    @Override
    public List<ServiceDef> listAPIByProvider(Map map){
        return serviceDefMapper.listAPIByProvider(map);
    }

}
