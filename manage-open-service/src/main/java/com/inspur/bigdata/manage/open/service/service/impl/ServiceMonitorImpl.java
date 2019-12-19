package com.inspur.bigdata.manage.open.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inspur.bigdata.manage.common.utils.PropertiesUtil;
import com.inspur.bigdata.manage.open.service.dao.ServiceMonitorMapper;
import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.data.ServiceInput;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;
import com.inspur.bigdata.manage.open.service.util.OpenServiceConstants;
import com.inspur.bigdata.manage.ranger.util.DateUtil;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import com.inspur.bigdata.manage.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static com.inspur.bigdata.manage.open.service.controller.ServiceExecuteController.createCloseableHttpClient;

/**
 * api服务监控表
 *
 * @author zhuzilian
 * @date 2019/08/05
 */
@Service("serviceMonitorService")
public class ServiceMonitorImpl implements IServiceMonitorService {

    private static final Log log = LogFactory.getLog(ServiceMonitorImpl.class);

    private static String apiServiceId = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "get_health_model_id");
    private static String getLogDataUrl = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "decrypt_health_model_getLogData_url");
    private static String exportExcelUrl = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "decrypt_health_model_exportExcel_url");

    @Autowired
    private ServiceMonitorMapper serviceMonitorMapper;

    @Autowired
    private IServiceMonitorService serviceMonitorService;

    @Override
    public int insert(ApiServiceMonitor apiServiceMonitor) {
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
        if (apiServiceMonitors != null && apiServiceMonitors.size() == 1) {
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

    /***
     @Override public ModelAndView getDetailById(String monitorId) {
     Map<String, Object> model = new HashMap<String, Object>();
     Map<String, String> queryParam = new HashMap<String, String>();
     queryParam.put("id", monitorId);
     if (StringUtil.isEmpty(apiServiceId)) {
//            apiServiceId = "b983cc8ee7814d2d9d4702fa08f230e7";
     }
//        queryParam.put("apiServiceId", apiServiceId);
     queryParam.put("result", "200");
     List<ApiServiceMonitor> apiServiceMonitors = serviceMonitorService.query(queryParam);
     ApiServiceMonitor apiServiceMonitor = null;
     String isSuccess = "";
     String totalNumber = "0";
     String totalPositivePopulation = "0";
     String totalPositiveRecord = "0";
     String uploadFile = "";
     String resultFile = "";
     if (apiServiceMonitors != null && apiServiceMonitors.size() == 1) {
     apiServiceMonitor = apiServiceMonitors.get(0);
     String callerUserId = apiServiceMonitor.getCallerUserId();
     String callerIp = apiServiceMonitor.getCallerIp();
     String requestTime = apiServiceMonitor.getRequestTime();
     String responseTime = apiServiceMonitor.getResponseTime();
     String openServiceOutput = apiServiceMonitor.getOpenServiceOutput();
     JSONObject openServiceOutputJson = null;
     try {
     openServiceOutputJson = JSON.parseObject(openServiceOutput);
     } catch (Exception e) {
     return new ModelAndView("service/monitor/api_monitor_detail", model);
     }
     if (openServiceOutputJson == null || openServiceOutputJson.size() == 0) {
     return new ModelAndView("service/monitor/api_monitor_detail", model);
     }
     String data = openServiceOutputJson.getString("data");
     uploadFile = openServiceOutputJson.getString("fileSourcePath");
     totalNumber = openServiceOutputJson.getString("authCount");
     JSONObject instream = new JSONObject();
     instream.put("content", data);
     String getLogDataStr = sendPostLogData(getLogDataUrl, instream.toString());
     if (StringUtil.isEmpty(getLogDataStr)) {
     model.put("error", "用户模型解密失败，请联系管理员或稍后重试");
     return new ModelAndView("service/monitor/api_monitor_detail", model);
     }
     JSONObject getLogDataJson = null;
     try {
     getLogDataJson = JSON.parseObject(getLogDataStr);
     } catch (Exception e) {
     model.put("error", "用户模型解密失败，请联系管理员或稍后重试");
     return new ModelAndView("service/monitor/api_monitor_detail", model);
     }
     if (getLogDataJson == null || getLogDataJson.size() == 0) {
     model.put("error", "用户模型解密失败，请联系管理员或稍后重试");
     return new ModelAndView("service/monitor/api_monitor_detail", model);
     }
     totalPositiveRecord = getLogDataJson.getString("dataCount");
     totalPositivePopulation = getLogDataJson.getString("realPeopleCount");
//            resultFile = "../exportExcelById/" + monitorId;
     resultFile = instream.toString();
     isSuccess = "success";

     model.put("isSuccess", isSuccess);
     model.put("callerUserId", callerUserId);
     model.put("callerIp", callerIp);
     model.put("requestTime", requestTime);
     model.put("responseTime", responseTime);
     model.put("totalNumber", totalNumber);
     model.put("totalPositivePopulation", totalPositivePopulation);
     model.put("totalPositiveRecord", totalPositiveRecord);
     model.put("uploadFile", uploadFile);
     model.put("resultFile", resultFile);
     }
     return new ModelAndView("service/monitor/api_monitor_detail", model);
     }
     */
    @Override
    public ModelAndView getDetailById(String monitorId) {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("id", monitorId);
        queryParam.put("result", "200");
        List<ApiServiceMonitor> apiServiceMonitors = serviceMonitorService.query(queryParam);
        ApiServiceMonitor apiServiceMonitor = null;
        String isSuccess = "";
        String totalNumber = "0";
        String totalPositivePopulation = "0";
        String totalPositiveRecord = "0";
        String uploadFile = "";
        String resultFile = "";
        if (apiServiceMonitors != null && apiServiceMonitors.size() == 1) {
            apiServiceMonitor = apiServiceMonitors.get(0);
            String callerUserId = apiServiceMonitor.getCallerUserId();
            String callerIp = apiServiceMonitor.getCallerIp();
            String requestTime = apiServiceMonitor.getRequestTime();
            String responseTime = apiServiceMonitor.getResponseTime();
            String openServiceOutput = apiServiceMonitor.getOpenServiceOutput();
            JSONObject openServiceOutputJson = null;
            try {
                openServiceOutputJson = JSON.parseObject(openServiceOutput);
            } catch (Exception e) {
                return new ModelAndView("service/monitor/api_monitor_detail", model);
            }
            if (openServiceOutputJson == null || openServiceOutputJson.size() == 0) {
                return new ModelAndView("service/monitor/api_monitor_detail", model);
            }
            String data = openServiceOutputJson.getString("data");
            uploadFile = openServiceOutputJson.getString("fileSourcePath");
            totalNumber = openServiceOutputJson.getString("authCount");
            JSONObject instream = new JSONObject();
            instream.put("content", data);
            totalPositiveRecord = openServiceOutputJson.getString("dataCount");
            totalPositivePopulation = openServiceOutputJson.getString("hasDataPeople");
            resultFile = instream.toString();
            isSuccess = "success";
            model.put("isSuccess", isSuccess);
            model.put("callerUserId", callerUserId);
            model.put("callerIp", callerIp);
            model.put("requestTime", requestTime);
            model.put("responseTime", responseTime);
            model.put("totalNumber", totalNumber);
            model.put("totalPositivePopulation", totalPositivePopulation);
            model.put("totalPositiveRecord", totalPositiveRecord);
            model.put("uploadFile", uploadFile);
            model.put("resultFile", resultFile);
        }
        return new ModelAndView("service/monitor/api_monitor_detail", model);
    }


    @Override
    public Map<String, Object> getApiMonitorList(Map<String, String> parameters) {
        Map<String, Object> mpMap = new HashMap<String, Object>();
        String userId= OpenDataConstants.getUserId();
        if ( userId != null ){
            if(userId.equals("odadmin")){
                parameters.put("callerUserId", null);
            }
            else {
                parameters.put("callerUserId", userId);
            }
        }
//        List<ApiServiceMonitor> ApiServiceMonitors = serviceMonitorService.query(parameters);
        List<ApiServiceMonitor> ApiServiceMonitors = serviceMonitorMapper.queryList(parameters);
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

    @Override
    public void exportExcelById(String monitorId, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("id", monitorId);
        queryParam.put("apiServiceId", "b983cc8ee7814d2d9d4702fa08f230e7");
        queryParam.put("result", "200");
        List<ApiServiceMonitor> apiServiceMonitors = serviceMonitorService.query(queryParam);
        ApiServiceMonitor apiServiceMonitor = null;
        if (apiServiceMonitors != null && apiServiceMonitors.size() == 1) {
            apiServiceMonitor = apiServiceMonitors.get(0);
            String openServiceOutput = apiServiceMonitor.getOpenServiceOutput();
            JSONObject openServiceOutputJson = JSON.parseObject(openServiceOutput);
            String data = openServiceOutputJson.getString("data");
            JSONObject instream = new JSONObject();
            instream.put("content", data);
            exportExcel(exportExcelUrl, instream.toString(), request, response);
        } else {
            log.warn("导出Excel失败，记录不存在。monitorId = [" + monitorId + "]");
        }

    }

    @Override
    public List<Map<String, Object>> queryNotSuccessNearby(Map<String, Object> paramsMap) {
        return serviceMonitorMapper.queryNotSuccessNearby(paramsMap);
    }

    public Date getFewDays(Date date, int dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayNum);
        return calendar.getTime();
    }

    public String sendPostLogData(String url, String instream) {
        String str = "";
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", OpenServiceConstants.content_type_json);
        httpPost.setEntity(new StringEntity(instream, "utf-8"));
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse response = httpclient.execute(httpPost)) {
            Header[] dispositionHeaders = response.getHeaders("Content-disposition");
            Header[] typeHeaders = response.getHeaders("Content-Type");
            Header[] transferEncodingHeaders = response.getHeaders("Transfer-Encoding");
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("调用用户模型解密接口错误，URL:" + url, e);
        }
        return str;
    }

    public void exportExcel(String url, String instream, HttpServletRequest request, HttpServletResponse response) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", OpenServiceConstants.content_type_json);
        httpPost.setEntity(new StringEntity(instream, "utf-8"));
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse httpResponse = httpclient.execute(httpPost)) {
            Header contentType = httpResponse.getFirstHeader("Content-Type");
            Header dispositionHeader = httpResponse.getFirstHeader("Content-disposition");
            Header transferEncodingHeader = httpResponse.getFirstHeader("Transfer-Encoding");
            HttpEntity entity = httpResponse.getEntity();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            InputStream inputStream = null;
            if (entity != null) {
                inputStream = entity.getContent();
                response.reset();
                response.setContentType(contentType.getValue());
                response.setHeader(dispositionHeader.getName(), dispositionHeader.getValue());
                response.setHeader(transferEncodingHeader.getName(), transferEncodingHeader.getValue());
            } else {
                inputStream = new ByteArrayInputStream("Export Excel failed ".getBytes());
                response.reset();
                response.setContentType(OpenServiceConstants.content_type_html);
            }
            try {
                ServletOutputStream sout = response.getOutputStream();
                bis = new BufferedInputStream(inputStream);
                bos = new BufferedOutputStream(sout);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                log.error("导出excel出现异常:", e);
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("调用导出Excel接口错误，URL:" + url, e);
        }
    }
}
