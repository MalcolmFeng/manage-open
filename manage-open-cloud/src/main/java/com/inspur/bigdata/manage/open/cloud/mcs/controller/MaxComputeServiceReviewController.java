package com.inspur.bigdata.manage.open.cloud.mcs.controller;

import com.inspur.bigdata.manage.open.cloud.mcs.data.MaxComputeServiceInstance;
import com.inspur.bigdata.manage.open.cloud.mcs.service.IMaxComputeServiceInstance;
import com.inspur.bigdata.manage.open.cloud.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/mcs")
public class MaxComputeServiceReviewController {

    @Autowired
    IMaxComputeServiceInstance maxComputeServiceInstance;

    // 返回审核列表页面视图
    @RequestMapping("/review")
    public String getPage() {
        return "cloud/mcs/MaxComputeServiceReviewList";
    }

    // 初始化实例列表
    @RequestMapping("/review/getInstances")
    @ResponseBody
    public Map<String, Object> getInstances() {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, String>> instanceList = new ArrayList<Map<String, String>>();

        List<MaxComputeServiceInstance> instances = maxComputeServiceInstance
                .getMaxComputeServiceInstances(params);

        for (MaxComputeServiceInstance instance : instances) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("instance_id", instance.getInstanceId());
            map.put("instance_name", instance.getInstanceName());
            map.put("service_version", instance.getServiceVersion());
            map.put("specification", instance.getCoreNum().toString() + "/" + instance.getMemory() + "/" + instance.getStorageVolume() + "/");
            map.put("status", instance.getApplyStatus() + "/" + instance.getRunStatus());
            map.put("apply_user", instance.getUserId());
            map.put("apply_time", instance.getApplyTime());
            instanceList.add(map);
        }

        result.put("data", instanceList);
        result.put("total", instances.size());

        return result;
    }

    // 通过大数据计算服务实例
    @RequestMapping("/passInstance")
    @ResponseBody
    public String passInstance(@RequestBody Map<String, Object> params) {
        Result result = new Result();

        Map<String, String> resultMap = maxComputeServiceInstance.passInstance(params);

        result.setStatus(resultMap.get("status"));
        result.setMessage(resultMap.get("message"));

        return result.toString();
    }

    // 批量通过大数据计算服务实例
    @RequestMapping("/passInstances")
    @ResponseBody
    public String passInstances(@RequestBody Map<String, Object> params) {
        Result result = new Result();

        Map<String, String> resultMap = maxComputeServiceInstance.passInstances(params);

        result.setStatus(resultMap.get("status"));
        result.setMessage(resultMap.get("message"));

        return result.toString();
    }

    // 驳回大数据计算服务实例
    @RequestMapping("/rejectInstance")
    @ResponseBody
    public String rejectInstance(@RequestBody Map<String, Object> params) {
        Result result = new Result();

        Map<String, String> resultMap = maxComputeServiceInstance.rejectInstance(params);

        result.setStatus(resultMap.get("status"));
        result.setMessage(resultMap.get("message"));

        return result.toString();
    }

    // 批量驳回大数据计算服务实例
    @RequestMapping("/rejectInstances")
    @ResponseBody
    public String rejectInstances(@RequestBody Map<String, Object> params) {
        Result result = new Result();

        Map<String, String> resultMap = maxComputeServiceInstance.rejectInstances(params);

        result.setStatus(resultMap.get("status"));
        result.setMessage(resultMap.get("message"));

        return result.toString();
    }
}
