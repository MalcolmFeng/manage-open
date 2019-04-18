package com.inspur.bigdata.manage.open.service.service.impl;

import com.inspur.bigdata.manage.open.service.dao.ServiceOutputMapper;
import com.inspur.bigdata.manage.open.service.data.ServiceOutput;
import com.inspur.bigdata.manage.open.service.service.IServiceOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional("mybatisTransactionManager")
public class ServiceOutputServiceImpl implements IServiceOutputService {
    @Autowired
    private ServiceOutputMapper serviceOutputMapper;

    @Override
    public List<ServiceOutput> selectByApiId(String apiId){
        return serviceOutputMapper.selectByApiId(apiId);
    }
}
