package com.inspur.bigdata.manage.open.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
        return serviceMonitorService.getApiMonitorList(parameters);
    }

    /**
     * 查看调用记录详情
     *
     * @return
     */
    @RequestMapping("/getInfo/{id}")
    @ResponseBody
    public ModelAndView getInfoById(@PathVariable("id") String monitorId) {
        return serviceMonitorService.getInfoById(monitorId);
    }

    /**
     * 查看调用记录统计
     *
     * @return
     */
    @RequestMapping("/getMonitorInfo")
    @ResponseBody
    public JSONObject getMonitorInfo() {
        return serviceMonitorService.getMonitorInfo();
    }

    /**
     * 查看调用记录Top5Api统计
     *
     * @return
     */
    @RequestMapping("/getTopApiInfo")
    @ResponseBody
    public JSONObject getTopApiInfo() {
        return serviceMonitorService.getTopApiInfo();
    }

    /**
     * 查看调用记录Top5 IP统计
     *
     * @return
     */
    @RequestMapping("/getTopIpInfo")
    @ResponseBody
    public JSONObject getTopIpInfo() {
        return serviceMonitorService.getTopIpInfo();
    }

    /**
     * 跳转调用记录列表主页面
     *
     * @return
     */
    @RequestMapping(value = "/getApiMonitorInfoPage")
    public String getApiMonitorInfoPage() {
        return "service/monitor/api_monitor_view";
    }
}
