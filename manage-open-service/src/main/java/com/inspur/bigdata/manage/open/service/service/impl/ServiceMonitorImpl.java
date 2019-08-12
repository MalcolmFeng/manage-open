package com.inspur.bigdata.manage.open.service.service.impl;

import com.inspur.bigdata.manage.open.service.dao.ServiceMonitorMapper;
import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * api服务监控表
 *
 * @author zhuzilian
 * @date 2019/08/05
 */
@Service("serviceMonitorService")
public class ServiceMonitorImpl implements IServiceMonitorService {
    @Autowired
    private ServiceMonitorMapper serviceMonitorMapper;

    @Override
    public int insert(ApiServiceMonitor apiServiceMonitor) {
        String groupId = UUIDGenerator.getUUID();
        apiServiceMonitor.setId(groupId);
        return serviceMonitorMapper.insert(apiServiceMonitor);
    }

    @Override
    public int delete(String id) {
        return serviceMonitorMapper.deleteById(id);
    }

    @Override
    public int update(ApiServiceMonitor apiServiceMonitor) {
        return serviceMonitorMapper.update(apiServiceMonitor);
    }

    @Override
    public ApiServiceMonitor load(String id) {
        return serviceMonitorMapper.load(id);
    }

    @Override
    public List<ApiServiceMonitor> query(Map param) {
        return serviceMonitorMapper.query(param);
    }
}
