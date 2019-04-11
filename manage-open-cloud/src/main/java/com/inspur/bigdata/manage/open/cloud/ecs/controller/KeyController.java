package com.inspur.bigdata.manage.open.cloud.ecs.controller;

import com.inspur.bigdata.manage.open.cloud.ecs.service.IEcsKeypair;
import com.inspur.bigdata.manage.open.cloud.utils.Result;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/ecs")
public class KeyController {

	//private String str = PropertiesUtil.getValue("conf.properties", "alert.domain");

	@Autowired
	private IEcsKeypair ecsKeypair;
	
	@RequestMapping("/key")
	public String getPage() {
		return "cloud/ecs/KeyList";
	}

	//创建密钥
	@RequestMapping("/createKey")
	@ResponseBody
	public String createKey(@RequestBody Map<String, String> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsKeypair.createKey(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
	}

	//查询指定用户，指定密钥的详情信息
	@RequestMapping("/getKey")
	@ResponseBody
	public String getKey(@RequestBody Map<String, String> params) {
		return ecsKeypair.getKey(params);
	}

	//初始化密钥对列表
    @RequestMapping("/getKeys")
    @ResponseBody
    public Map getKeys(){
//    	RestTemplate restTemplate = new RestTemplate();
//      String url = "http://"+str+"/alert/getalerts";

		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> instanceList = new ArrayList<Map<String, String>>();

		List<String> keyNames = ecsKeypair.getKeys();

		for (String keyName : keyNames) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("key_name", keyName);
			instanceList.add(map);
		}

		result.put("data", instanceList);
		result.put("total", instanceList.size());

		return result;
    }

    @RequestMapping("/getKeyList")
	@ResponseBody
	public String getKeyList() {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		List<String> keyNames = ecsKeypair.getKeys();

		for (String keyName : keyNames) {
			jsonArray.put(keyName);
		}

		jsonObject.put("keyList", jsonArray);

		return jsonObject.toString();
	}
    
    //删除单个密钥对
  	@RequestMapping("/deleteKey")
  	@ResponseBody
  	public String deleteKey(@RequestBody Map<String, String> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsKeypair.deleteKey(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
  	}
  	
  	//批量删除密钥对
  	@RequestMapping("/deleteKeys")
  	@ResponseBody
  	public String deleteKeys(@RequestBody Map<String, String> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsKeypair.deleteKeys(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
  	}

  	@RequestMapping(value = "/downloadPrivateKey", method = RequestMethod.GET)
	public String downloadPrivateKey(HttpServletRequest request) {
		String keyName = request.getParameter("keyname");
		byte[] body = ecsKeypair.downloadPrivateKey();

		if (body != null) {
			request.setAttribute("body", body);
			request.setAttribute("keyName", keyName);
			return "forward:/service/ecs/download";
		}

		return "forward:/service/ecs/key";
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(HttpServletRequest request) {
		byte[] body = (byte[]) request.getAttribute("body");
		String keyName = (String) request.getAttribute("keyName");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attchement;filename=" + keyName + "_rsa");
		HttpStatus status = HttpStatus.OK;
		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, status);
		return entity;
	}
}
