package com.inspur.bigdata.manage.open.service.service.impl;

import com.inspur.bigdata.manage.open.service.dao.ServiceDefMapper;
import com.inspur.bigdata.manage.open.service.dao.ServiceInputMapper;
import com.inspur.bigdata.manage.open.service.dao.ServiceOutputMapper;
import com.inspur.bigdata.manage.open.service.data.ServiceDef;
import com.inspur.bigdata.manage.open.service.data.ServiceInput;
import com.inspur.bigdata.manage.open.service.data.ServiceOutput;
import com.inspur.bigdata.manage.open.service.service.IServiceDefService;
import com.inspur.bigdata.manage.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.loushang.framework.util.DateUtil;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private ServiceOutputMapper serviceOutputMapper;

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
        if (list == null){
            list = new ArrayList<>();
        }
        serviceDefMapper.insert(serviceDef);
        for(ServiceInput input:list){
            input.setId(UUIDGenerator.getUUID());
            input.setApiServiceId(serviceDef.getId());
            serviceInputMapper.insert(input);
        }
        List<ServiceOutput> outputs=serviceDef.getOutputList();
        if (outputs == null){
            outputs = new ArrayList<>();
        }
        for (ServiceOutput output:outputs){
            output.setId(UUIDGenerator.getUUID());
            output.setOpenServiceId(serviceDef.getId());
            serviceOutputMapper.insert(output);
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
        serviceOutputMapper.deleteByApiId(serviceDef.getId());
        //重新添加
        for(ServiceInput input:list){
            input.setId(UUIDGenerator.getUUID());
            input.setApiServiceId(serviceDef.getId());
            serviceInputMapper.insert(input);
        }
        List<ServiceOutput> outputs=serviceDef.getOutputList();
        for (ServiceOutput output:outputs){
            output.setId(UUIDGenerator.getUUID());
            output.setOpenServiceId(serviceDef.getId());
            serviceOutputMapper.insert(output);
        }
    }

    @Override
    public void deleteById(String id) {
        //删除def
        serviceDefMapper.deleteById(id);
        //删除对应的输入参数
        serviceInputMapper.deleteByApiId(id);
        //删除对应的输出参数
        serviceOutputMapper.deleteByApiId(id);
    }

    @Override
    public List<ServiceDef> getByApiGroupAndPath(Map map) {
        return serviceDefMapper.getByApiGroupAndPath(map);
    }

    @Override
    public List<ServiceDef> getByGroupContextAndPath(Map map) {
        return serviceDefMapper.getByGroupContextAndPath(map);
    }

    @Override
    public List<ServiceDef> listAPIByProvider(Map map){
        return serviceDefMapper.listAPIByProvider(map);
    }
    @Override
    public List<ServiceDef> queryByRemoteId(String remoteId){
        if (StringUtils.isEmpty(remoteId)){
            return null;
        }
       return serviceDefMapper.queryByRemoteId(remoteId);
    }


    @Override
    public int updateServiceDefCount(Map<String, String> map) {
        if (StringUtil.isEmpty(map.getOrDefault("updateTime", ""))) {
            map.put("updateTime", DateUtil.getCurrentTime2());
        }
        return serviceDefMapper.updateServiceDefCount(map);
    }

}
