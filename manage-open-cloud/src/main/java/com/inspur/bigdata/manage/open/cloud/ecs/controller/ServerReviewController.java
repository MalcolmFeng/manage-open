package com.inspur.bigdata.manage.open.cloud.ecs.controller;

import com.inspur.bigdata.manage.open.cloud.ecs.service.IEcsInstance;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/ecs")
public class ServerReviewController {

	//private String str = PropertiesUtil.getValue("conf.properties", "alert.domain");

	@Autowired
	private IEcsInstance ecsInstance;
	
	@RequestMapping("/review")
	public String getPage() {
		return "cloud/ecs/ServerReviewList";
	}

	//初始化实例列表
	@RequestMapping("/review/getInstances")
	@ResponseBody
	public Map<String, Object> getInstances(){
//    	RestTemplate restTemplate = new RestTemplate();
//      String url = "http://"+str+"/alert/getalerts";

		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> instanceList = new ArrayList<Map<String, String>>();

		try {
			JSONArray instances = new JSONObject(ecsInstance.getEcsInstanceByAuditor()).getJSONArray("response");

			for (int i = 0; i < instances.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject instance = instances.getJSONObject(i);

				map.put("instance_name", instance.getString("vm_name"));
				map.put("ip", "");
				map.put("specification_iso", instance.getString("vm_cpu") + "vCPU " + instance.getString("vm_memory")
						+ "GB内存 " +  instance.getString("vm_system"));
				map.put("status", instance.getString("audit_status") + "/" + instance.getString("vm_status"));
				map.put("apply_user", instance.getString("user_id"));
				map.put("apply_time", instance.getString("apply_time"));
				instanceList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		result.put("data", instanceList);
		result.put("total", instanceList.size());

		return result;
	}


	@RequestMapping("/review/getDetails")
	@ResponseBody
	public Map getDetails(@RequestBody Map<String, String> parameters) {
		Map<String, String> result = new HashMap<String, String>();
		String instanceName = parameters.get("instance_name");

		JSONArray instances =new JSONObject(ecsInstance.getEcsInstanceByAuditor()).getJSONArray("response");

		for (int i = 0; i < instances.length(); i++) {
			JSONObject instance = instances.getJSONObject(i);
			if (instanceName.equals(instance.getString("vm_name"))) {
				result.put("instanceName", instance.getString("vm_name"));
				result.put("userId", instance.getString("user_id"));
				result.put("vmNumber", instance.getString("vm_number"));
				result.put("applyTime", instance.getString("apply_time"));
				result.put("applyReason", instance.getString("apply_reason"));
				result.put("coreNum", instance.getString("vm_cpu"));
				result.put("memory", instance.getString("vm_memory"));
				result.put("ecsOs", instance.getString("vm_system"));
				result.put("osDiskSize", instance.getString("vm_os_disk_size"));
				result.put("dataDiskSize", instance.getString("vm_data_disk_size"));
				result.put("dataDiskNum", instance.getString("vm_data_disk_number"));
				result.put("applyStatus", instance.getString("audit_status"));
				if (instance.get("audit_reply").toString().equals("null")) {
					result.put("auditReply", "");
				} else {
					result.put("auditReply", instance.getString("audit_reply"));
				}
				if (instance.has("vm_cpu_new")) {
					result.put("vmCpuNew", instance.getString("vm_cpu_new"));
				}
				if (instance.has("vm_memory_new")) {
					result.put("vmMemoryNew", instance.getString("vm_memory_new"));
				}
			}
		}

		return result;
	}
  	
  	//通过单个实例
  	@RequestMapping("/passInstance")
  	@ResponseBody
  	public String passInstance(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsInstance.passInstance(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
  	}

    //驳回单个实例
  	@RequestMapping("/rejectInstance")
  	@ResponseBody
  	public String rejectInstance(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsInstance.rejectInstance(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
	}

	//批量通过实例
	@RequestMapping("/passinstances")
	@ResponseBody
	public Map passInstances(@RequestBody Map<String, String> params) {
		Map json = new HashMap();
		json.put("result", "success");
		return json;
	}
  	
    //批量驳回实例
  	@RequestMapping("/rejectinstances")
  	@ResponseBody
  	public Map rejectInstances(@RequestBody Map<String, String> params) {
  		Map json = new HashMap();
  		json.put("result", "success");
  		return json;
  	}

	//批量删除实例
	@RequestMapping("/deleteinstances")
	@ResponseBody
	public Map deleteInstances(@RequestBody Map<String, String> params) {
		Map json = new HashMap();
		json.put("result", "success");
		return json;
	}

	@RequestMapping("/auditChangeFormat")
	@ResponseBody
	public String auditChangeFormat(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsInstance.auditChangeFormat(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
	}
	@RequestMapping("/getPlatformType")
	@ResponseBody
	public String getPlatformType() {
		JSONObject result = new JSONObject();
		JSONArray resultArray = new JSONArray();

		try {
			String response = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES,
					"ecs.domain") + "/resource/getplatforminfo", new HashMap<>(), "UTF-8");

			JSONObject responseJson = new JSONObject(response);
			if (responseJson.has("vsphere")) {
				resultArray.put("vsphere");
			}
			if (responseJson.has("openstack")) {
				resultArray.put("openstack");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		result.put("platformType", resultArray);
		return result.toString();
	}

	@RequestMapping("/platformSave")
	@ResponseBody
	public String platformSave(@RequestBody Map<String, Object> params) {
		Result result = new Result();
		result.setStatus("false");
		result.setMessage("保存云主机配置失败！");

		Map<String, String> params1 = new HashMap<>();
		params1.put("data", new JSONObject(params).toString());

		try {
			String response1 = HttpRequestUtil.doPostUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/check",
					params1, "UTF-8");
			if (new JSONObject(response1).getBoolean("result")) {
				String response2 = HttpRequestUtil.doPostUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/platformsave",
						params1, "UTF-8");
				if (new JSONObject(response2).getBoolean("result")) {
					result.setStatus("true");
					result.setMessage("保存云主机配置成功！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result.toString();
	}

}
