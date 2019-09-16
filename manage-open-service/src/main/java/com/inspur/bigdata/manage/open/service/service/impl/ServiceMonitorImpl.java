package com.inspur.bigdata.manage.open.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.inspur.bigdata.manage.open.service.dao.ServiceMonitorMapper;
import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.data.ServiceInput;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;
import com.inspur.bigdata.manage.open.service.util.OpenServiceConstants;
import com.inspur.bigdata.manage.ranger.util.DateUtil;
import com.inspur.bigdata.manage.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * api服务监控表
 *
 * @author zhuzilian
 * @date 2019/08/05
 */
@Service("serviceMonitorService")
public class ServiceMonitorImpl implements IServiceMonitorService {

    private static final Log log = LogFactory.getLog(ServiceMonitorImpl.class);

    @Autowired
    private ServiceMonitorMapper serviceMonitorMapper;

    @Autowired
    private IServiceMonitorService serviceMonitorService;

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

    @Override
    public ModelAndView getInfoById(String monitorId) {
        boolean canViewBackEnd = false;
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("id", monitorId);
        List<ApiServiceMonitor> apiServiceMonitors = serviceMonitorService.query(queryParam);
        ApiServiceMonitor apiServiceMonitor = null;
        List<ServiceInput> inputParam = new ArrayList<>();
        List<ServiceInput> serviceInputsParam = new ArrayList<>();
        if (apiServiceMonitors != null || apiServiceMonitors.size() == 1) {
            apiServiceMonitor = apiServiceMonitors.get(0);
            String openServiceInput = apiServiceMonitor.getOpenServiceInput();
            inputParam = jsonToServiceInputList(openServiceInput);
            String ServiceInput = apiServiceMonitor.getServiceInput();
            serviceInputsParam = jsonToServiceInputList(ServiceInput);
            if (OpenServiceConstants.getUserId().equals(apiServiceMonitor.getCallerUserId()) || OpenServiceConstants.isSuperAdmin(OpenServiceConstants.getRealm())) {
                canViewBackEnd = true;
            }
        } else {
            apiServiceMonitor = new ApiServiceMonitor();
        }

//        canViewBackEnd=true;
        model.put("canViewBackEnd", canViewBackEnd);
        model.put("apiServiceMonitor", apiServiceMonitor);
        model.put("inputParam", inputParam);
        model.put("serviceInputsParam", serviceInputsParam);
        return new ModelAndView("service/monitor/api_monitor_info", model);
    }

    @Override
    public Map<String, Object> getApiMonitorList(Map<String, String> parameters) {
        Map<String, Object> mpMap = new HashMap<String, Object>();
//        parameters.put("provider", OpenServiceConstants.getUserId());
        List<ApiServiceMonitor> ApiServiceMonitors = serviceMonitorService.query(parameters);
        if (StringUtil.isEmpty(ApiServiceMonitors)) {
            mpMap.put("total", 0);
            mpMap.put("data", new ArrayList<ApiServiceMonitor>());
            return mpMap;
        }
        int total = PageUtil.getTotalCount();
        mpMap.put("total", total != -1 ? total : ApiServiceMonitors.size());
        mpMap.put("data", ApiServiceMonitors);

        return mpMap;
    }

    private List<ServiceInput> jsonToServiceInputList(String jsonString) {
        List<ServiceInput> inputParam = new ArrayList<>();
        if (StringUtil.isNotEmpty(jsonString)) {
            try {
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                for (Map.Entry entry : jsonObject.entrySet()) {
                    String key = (String) entry.getKey();
                    ServiceInput input = new ServiceInput();
                    String value = jsonObject.getString(key);
                    input.setName(key);
                    input.setValue(value);
                    inputParam.add(input);
                }
            } catch (Exception e) {
                log.info("jsonToServiceInputList error:" + e.getMessage());
            }
        }
        return inputParam;
    }

    @Override
    public JSONObject getMonitorInfo() {
        int dayNum = 7;
        List<String> daylist = new ArrayList<>(dayNum);
        List<Integer> dayTotalCountList = new ArrayList<>(dayNum);
        List<Integer> dayTotalSuccessCountList = new ArrayList<>(dayNum);
        Date date = new Date();
        //获取每天日期列表
        for (int i = dayNum - 1; i >= 0; i--) {
            daylist.add(DateUtil.dateToString(getFewDays(date, -i), "yyyy-MM-dd"));
        }
        String startDay = daylist.get(0);
        Map<String, String> dayParameter = new HashMap<>();
        //获取 dayNum 天数中每天的调用量和调用成功量（result=200）
        dayParameter.put("result", "200");
        dayParameter.put("days", startDay);
        List<Map<String, String>> allTotalCounts = serviceMonitorMapper.getAllCount();
        List<Map<String, String>> dayTotalCounts = serviceMonitorMapper.getDayCount(dayParameter);
        JSONObject resultJson = new JSONObject();
        if (allTotalCounts.size() == 1) {
            resultJson.put("allTotalCount", allTotalCounts.get(0).get("totalCount"));
            resultJson.put("allTotalSuccessCount", allTotalCounts.get(0).get("totalSuccessCount"));
            JSONObject dayTotalCountJson = new JSONObject();
            for (Map<String, String> temp : dayTotalCounts) {
                dayTotalCountJson.put(temp.get("day"), temp);
            }
            for (String dayText : daylist) {
                if (dayTotalCountJson.get(dayText) == null || dayTotalCountJson.getJSONObject(dayText).size() == 0) {
                    dayTotalCountList.add(0);
                    dayTotalSuccessCountList.add(0);
                } else {
                    dayTotalCountList.add(dayTotalCountJson.getJSONObject(dayText).getInteger("totalCount"));
                    dayTotalSuccessCountList.add(dayTotalCountJson.getJSONObject(dayText).getInteger("totalSuccessCount"));
                }
            }
            resultJson.put("dayTotalCountList", dayTotalCountList);
            resultJson.put("dayTotalSuccessCountList", dayTotalSuccessCountList);
        }
        resultJson.put("daylist", daylist);
        resultJson.put("today", daylist.get(daylist.size() - 1));
        return resultJson;
    }

    @Override
    public JSONObject getTopApiInfo() {
        int dayNum = 7;
        int pageSize = 5;
        String startDay = DateUtil.dateToString(getFewDays(new Date(), -(dayNum - 1)), "yyyy-MM-dd");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("days", startDay);
//        parameter.put("page", 0);
        parameter.put("pageSize", pageSize);
        List<Map<String, String>> topApiCount = serviceMonitorMapper.getTopApiCount(parameter);
        JSONObject resultJson = new JSONObject();
        List<String> topApiName = new ArrayList<>(topApiCount.size());
        List<String> topApiNum = new ArrayList<>(topApiCount.size());
        for (Map<String, String> temp : topApiCount) {
            topApiName.add(temp.get("api_service_name"));
            topApiNum.add(temp.get("totalCount"));
        }
        Collections.reverse(topApiName);
        Collections.reverse(topApiNum);
        resultJson.put("topApiName", topApiName);
        resultJson.put("topApiNum", topApiNum);
        return resultJson;
    }

    @Override
    public JSONObject getTopIpInfo() {
        int dayNum = 7;
        String startDay = DateUtil.dateToString(getFewDays(new Date(), -(dayNum - 1)), "yyyy-MM-dd");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("days", startDay);
        List<Map<String, String>> topIpCount = serviceMonitorMapper.getTopIpCount(parameter);
        JSONObject resultJson = new JSONObject();
        resultJson.put("topIpCount", topIpCount);
        return resultJson;
    }

    public Date getFewDays(Date date, int dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayNum);
        return calendar.getTime();
    }
}
