package com.inspur.bigdata.manage.open.cloud.mcs.controller;

import com.inspur.bigdata.manage.cluster.hadoop.api.HadoopClusterService;
import com.inspur.bigdata.manage.open.cloud.mcs.data.MaxComputeServiceInstance;
import com.inspur.bigdata.manage.open.cloud.mcs.service.IMaxComputeServiceInstance;
import com.inspur.bigdata.manage.open.cloud.utils.Constants;
import com.inspur.bigdata.manage.open.cloud.utils.HttpRequestUtil;
import com.inspur.bigdata.manage.open.cloud.utils.PropertiesUtil;
import com.inspur.bigdata.manage.open.cloud.utils.Result;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/mcs")
public class MaxComputeServiceManageController {

    @Autowired
    IMaxComputeServiceInstance maxComputeServiceInstance;

    // 返回管理列表页面视图
    @RequestMapping("/manage")
    public String getPage() {
        return "cloud/mcs/MaxComputeServiceManageList";
    }

    // 初始化实例列表
    @RequestMapping("/manage/getInstances")
    @ResponseBody
    public Map<String, Object> getInstances() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, String>> instanceList = new ArrayList<Map<String, String>>();

        List<MaxComputeServiceInstance> instances = maxComputeServiceInstance
                .getMaxComputeServiceInstancesOrderByApplyTimeDesc();

        for (MaxComputeServiceInstance instance : instances) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("instance_id", instance.getInstanceId());
            map.put("instance_name", instance.getInstanceName());
            map.put("create_time", instance.getCreateTime());
            map.put("service_version", instance.getServiceVersion());
            map.put("status", instance.getApplyStatus() + "/" + instance.getRunStatus());
            instanceList.add(map);
        }

        result.put("data", instanceList);
        result.put("total", instances.size());

        return result;
    }

    // 根据实例id查询信息
    @RequestMapping("/getDetailsByInstanceId")
    @ResponseBody
    public MaxComputeServiceInstance getDetailsByInstanceId(@RequestBody Map<String, Object> params) {
        String instanceId = params.get("instance_id").toString();
        return maxComputeServiceInstance.getMaxComputeServiceInstanceByInstanceId(instanceId);
    }

    // 删除实例
    @RequestMapping("deleteInstance")
    @ResponseBody
    public String deleteInstance(@RequestBody Map<String, Object> params) {
        Result result = new Result();

        Map<String, String> resultMap = maxComputeServiceInstance.deleteInstance(params);

        result.setStatus(resultMap.get("status"));
        result.setMessage(resultMap.get("message"));

        return result.toString();
    }

    // 批量删除实例
    @RequestMapping("/deleteInstances")
    @ResponseBody
    public String deleteInstances(@RequestBody Map<String, Object> params) {
        Result result = new Result();

        Map<String, String> resultMap = maxComputeServiceInstance.deleteInstances(params);

        result.setStatus(resultMap.get("status"));
        result.setMessage(resultMap.get("message"));

        return result.toString();
    }
    // 获取hdfs存储容量
    @RequestMapping("/getStorageVolume")
    @ResponseBody
    public Map<String, Object> getStorageVolume(@RequestParam String instanceId, @RequestParam String userId) {
        return maxComputeServiceInstance.getStorageVolume(instanceId, userId);
    }
    // 获取hive url
    @RequestMapping("/getHiveUrl")
    @ResponseBody
    public Map<String, Object> getHiveUrl(@RequestParam String userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            String clusterResponse = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/manage-cluster/service/indata/cluster/clusterInfo/"
                    + userId.split("-")[1], new HashMap<>(), "UTF-8");
            String clusterId = new JSONObject(clusterResponse).getString("clusterId");

            String hiveConnUrl =HadoopClusterService.getHiveConnUrl(clusterId);
            String realm="@"+HadoopClusterService.getKerberosRealmName(clusterId);
            String domainName = hiveConnUrl.substring(13, hiveConnUrl.length()-6);
            String userInfo = "hive/" + domainName + realm;
            String connection =hiveConnUrl + "/default;principal="+userInfo;
            result.put("hiveJdbcConnection",connection);

            String hive2ConnUrl = HadoopClusterService.getHive2ConnUrl(clusterId);
            String realm2="@"+HadoopClusterService.getKerberosRealmName(clusterId);
            String domainName2 = hive2ConnUrl.substring(13, hive2ConnUrl.length()-6);
            String userInfo2 = "hive/" + domainName2 + realm2;
            String connection2 =hive2ConnUrl + "/default;principal="+userInfo2;
            result.put("hive2JdbcConnection",connection2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    // 获取租户空间
    @RequestMapping("/queryTenant")
    @ResponseBody
    public Map<String, Object> getTenantResource(@RequestParam String instanceId, @RequestParam String userId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> tenantList = new ArrayList<>();

        try {
            String clusterResponse = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/manage-cluster/service/indata/cluster/clusterInfo/"
                    + userId.split("-")[1], new HashMap<>(), "UTF-8");
            String clusterId = new JSONObject(clusterResponse).getString("clusterId");

            Map<String, String> params = new HashMap<>();
            params.put("userName", userId);
            params.put("clusterId", clusterId);
            String resultStr = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/indata-manage-portal/service/security/rest/getTenantRes",
                    params, "UTF-8");
            JSONArray tenantResArray = new JSONArray(resultStr);

            String tenantName = userId.split("-")[0] + "_" + instanceId.substring(0, 4);
            for (Object object : tenantResArray) {
                JSONObject tenantObject = new JSONObject(object.toString());
                if (tenantObject.getString("tenant").equals(tenantName)) {
                    JSONArray resArray = tenantObject.getJSONArray("resource");
                    for (Object object2 : resArray) {
                        JSONObject resObject = new JSONObject(object2.toString());
                        Map<String, Object> resMap = new HashMap<>();
                        // 当资源为HDFS时，需要额外获取存储空间使用情况
                        if (resObject.getString("service").equals("HDFS")) {
                            resMap = getStorageVolume(instanceId, userId);
                        }
                        resMap.put("service", resObject.getString("service"));
                        resMap.put("resPath", resObject.getString("resPath"));
                        if (resObject.get("resQuota1").toString().equals("null")) {
                            resMap.put("resQuota1", "");
                        } else {
                            resMap.put("resQuota1", resObject.getString("resQuota1"));
                        }
                        if (resObject.get("resQuota2").toString().equals("null")) {
                            resMap.put("resQuota2", "");
                        } else {
                            resMap.put("resQuota2", resObject.getString("resQuota2"));
                        }
                        tenantList.add(resMap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resultMap.put("data", tenantList);
        resultMap.put("size", tenantList.size());
        return resultMap;
    }

}
