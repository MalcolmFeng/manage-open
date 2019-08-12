package com.inspur.bigdata.manage.open.service.controller;

import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.data.ServiceInput;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;
import com.inspur.bigdata.manage.open.service.util.OpenServiceConstants;
import com.inspur.bigdata.manage.utils.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loushang.framework.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * api服务监控表
 *
 * @author zhuzilian
 * @date 2019/08/05
 */
@Controller
@RequestMapping("/dev/monitor")
public class ServiceMonitorController {
    private static final Log log = LogFactory.getLog(ServiceMonitorController.class);

    @Autowired
    private IServiceMonitorService serviceMonitorService;

    /**
     * [新增]
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/insert")
    public int insert(ApiServiceMonitor apiServiceMonitor) {
        return serviceMonitorService.insert(apiServiceMonitor);
    }

    /**
     * [刪除]
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/delete")
    public int delete(String id) {
        return serviceMonitorService.delete(id);
    }

    /**
     * [更新]
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/update")
    public int update(ApiServiceMonitor apiServiceMonitor) {
        return serviceMonitorService.update(apiServiceMonitor);
    }

    /**
     * [查询] 根据主键 id 查询
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/load")
    public ApiServiceMonitor load(@RequestBody String id) {
        return serviceMonitorService.load(id);
    }

    /**
     * [查询] 根据参数查询
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/query")
    public List<ApiServiceMonitor> query(@RequestBody Map map) {
        return serviceMonitorService.query(map);
    }

    /**
     * 跳转调用记录列表主页面
     *
     * @return
     */
    @RequestMapping(value = "/getApiMonitorPage")
    public String getApiMonitorPage() {
        return "service/monitor/api_monitor_list";
    }

    /**
     * 查询调用记录列表
     *
     * @param parameters
     * @return
     */
    @RequestMapping(value = "/getApiMonitorList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getApiMonitorList(@RequestBody Map<String, String> parameters) {
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

    /**
     * 查看Api详情
     *
     * @return
     */
    @RequestMapping("/getInfo/{id}")
    @ResponseBody
    public ModelAndView getInfoById(@PathVariable("id") String monitorId) {
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

    private List<ServiceInput> jsonToServiceInputList(String jsonString) {
        List<ServiceInput> inputParam = new ArrayList<>();
        if (StringUtil.isNotEmpty(jsonString)) {
            try {
                JSONObject jsonObject = JSONObject.fromObject(jsonString);
                Iterator<String> it = jsonObject.keys();
                while (it.hasNext()) {
                    String key = it.next();
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
}
