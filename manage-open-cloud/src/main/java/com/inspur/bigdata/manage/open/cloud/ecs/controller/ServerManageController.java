package com.inspur.bigdata.manage.open.cloud.ecs.controller;

import com.inspur.bigdata.manage.open.cloud.ecs.service.IEcsInstance;
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
public class ServerManageController {

	@Autowired
	private IEcsInstance ecsInstance;

	@RequestMapping("/manage")
	public String getPage() {
		return "cloud/ecs/ServerManageList";
	}

//初始化实例列表
	@RequestMapping("/manage/getInstances")
	@ResponseBody
	public Map<String, Object> getInstances(){
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> instanceList = new ArrayList<Map<String, String>>();

		try {
			JSONArray instances = new JSONObject(ecsInstance.getEcsInstancesByUser()).getJSONArray("response");

			for (int i = 0; i < instances.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject instance = instances.getJSONObject(i);

				map.put("vm_name", instance.getString("vm_name"));
				map.put("instance_name", instance.getString("vm_name_apply"));
				map.put("ip", instance.get("vm_fix_ip").toString());
				map.put("status", instance.getString("audit_status") + "/" + instance.getString("vm_status"));
				map.put("specification_iso", instance.getString("vm_cpu") + "vCPU " + instance.getString("vm_memory")
						+ "GB内存 " +  instance.getString("vm_system"));
				instanceList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		result.put("data", instanceList);
		result.put("total", instanceList.size());

		return result;
	}

    
    //删除单个实例
  	@RequestMapping("/deleteinstance")
  	@ResponseBody
  	public String deleteInstance(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsInstance.deleteInstance(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
  	}
  	
  	//批量删除实例
  	@RequestMapping("/deletemanageinstances")
  	@ResponseBody
  	public Map deleteInstances(@RequestBody Map<String, String> params) {
  		Map json = new HashMap();
  		json.put("result", "success");
  		return json;
  	}
  	
  	//保存信息
  	@RequestMapping("/savebasicinfo")
  	@ResponseBody
  	public Map saveBasicinfo(@RequestBody Map<String, String> params) {
  		Map json = new HashMap();
  		json.put("result", "success");
  		return json;
  	}
  	
  	//重置密码
  	@RequestMapping("/resetpwd")
	@ResponseBody
	public String resetPwd(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsInstance.resetPwd(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
	}

	//修改密钥
	@RequestMapping("/modifyKey")
	@ResponseBody
	public String modifyKey(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsInstance.modifyKey(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
	}

  	//变更规格
	@RequestMapping("/applyChangeFormat")
	@ResponseBody
	public String applyChangeFormat(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsInstance.applyChangeFormat(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
	}
  	
  	//根据实例id查询信息
  	@RequestMapping("/manage/getDetails")
	@ResponseBody
	public Map getDetails(@RequestBody Map<String, String> parameters) {
//		String instance_id = parameters.get("instance_id");
//		Map postData = new HashMap();
//		postData.put("instance_id", instance_id);

		Map<String, String> result = new HashMap<String, String>();
  		String instanceName = parameters.get("instance_name");

		JSONArray instances =new JSONObject(ecsInstance.getEcsInstancesByUser()).getJSONArray("response");

		for (int i = 0; i < instances.length(); i++) {
			JSONObject instance = instances.getJSONObject(i);
			if (instanceName.equals(instance.getString("vm_name_apply"))) {

				result.put("vmName", instance.getString("vm_name"));
				result.put("instanceName", instance.getString("vm_name_apply"));
				result.put("applyStatus", instance.getString("audit_status"));
				if (instance.get("audit_reply").toString().equals("null")) {
					result.put("auditReply", "");
				} else {
					result.put("auditReply", instance.getString("audit_reply"));
				}
				result.put("applyTime", instance.getString("apply_time"));
				result.put("coreNum", instance.getString("vm_cpu"));
				result.put("memory", instance.getString("vm_memory"));
				result.put("ecsOs", instance.getString("vm_system"));
				result.put("runStatus", instance.getString("vm_status"));
				result.put("password", instance.getString("rootpassword"));
				result.put("keyName", instance.getString("key_name"));
				result.put("ipAddress", instance.getString("vm_fix_ip"));
				result.put("osDiskSize", instance.getString("vm_os_disk_size"));
				result.put("dataDiskSize", instance.getString("vm_data_disk_size"));
				result.put("dataDiskNum", instance.getString("vm_data_disk_number"));
				if (instance.get("lock_status").toString().equals("null")) {
					result.put("lockStatus", "");
				} else {
					result.put("lockStatus", instance.getString("lock_status"));
				}
				if (instance.get("audit_time").toString().equals("null")) {
					result.put("createTime", "");
				} else {
					result.put("createTime", instance.getString("audit_time"));
				}
			}
		}

		return result;
	}

	@RequestMapping("/powerOperation")
	@ResponseBody
	public String powerOperation(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsInstance.powerOperation(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
	}
}
