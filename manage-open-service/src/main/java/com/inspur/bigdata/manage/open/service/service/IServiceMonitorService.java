package com.inspur.bigdata.manage.open.service.service;

import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;

import java.util.List;
import java.util.Map;

public interface IServiceMonitorService {

    int insert(ApiServiceMonitor apiServiceMonitor);

    int delete(String id);

    int update(ApiServiceMonitor apiServiceMonitor);

    ApiServiceMonitor load(String id);

    List<ApiServiceMonitor> query(Map param);

}
