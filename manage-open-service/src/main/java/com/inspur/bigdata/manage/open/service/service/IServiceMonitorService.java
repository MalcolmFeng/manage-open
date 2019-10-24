package com.inspur.bigdata.manage.open.service.service;

import com.alibaba.fastjson.JSONObject;
import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IServiceMonitorService {

    int insert(ApiServiceMonitor apiServiceMonitor);

    int delete(String id);

    int update(ApiServiceMonitor apiServiceMonitor);

    ApiServiceMonitor load(String id);

    List<ApiServiceMonitor> query(Map param);

    ModelAndView getInfoById(String monitorId);

    ModelAndView getDetailById(String monitorId);

    Map<String, Object> getApiMonitorList(Map<String, String> parameters);

    JSONObject getMonitorInfo();

    JSONObject getTopApiInfo();

    JSONObject getTopIpInfo();

    void exportExcelById(String monitorId, HttpServletRequest request, HttpServletResponse response);

    List<Map<String, Object>> queryNotSuccessNearby(Map<String, Object> paramsMap);
}
