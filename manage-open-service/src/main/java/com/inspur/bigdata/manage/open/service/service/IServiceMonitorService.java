package com.inspur.bigdata.manage.open.service.service;

import com.alibaba.fastjson.JSONObject;
import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

public interface IServiceMonitorService {

    int insert(ApiServiceMonitor apiServiceMonitor);

    int delete(String id);

    int update(ApiServiceMonitor apiServiceMonitor);

    ApiServiceMonitor load(String id);

    List<ApiServiceMonitor> query(Map param);

    ModelAndView getInfoById(String monitorId);

    Map<String, Object> getApiMonitorList(Map<String, String> parameters);

    JSONObject getMonitorInfo();

    JSONObject getTopApiInfo();

    JSONObject getTopIpInfo();

}
