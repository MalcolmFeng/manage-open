package com.inspur.bigdata.manage.open.cloud.ecs.controller;

import com.inspur.bigdata.manage.open.cloud.ecs.service.IEcsInstance;
import com.inspur.bigdata.manage.open.cloud.utils.OpenUtil;
import com.inspur.bigdata.manage.open.cloud.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/ecs")
public class ServerApplyController {

	@Autowired
	private IEcsInstance ecsInstance;
	
	@RequestMapping("/apply")
	public ModelAndView getPage() {
	    String url = "cloud/ecs/ServerApply";
        Map<String, Object> model = new HashMap<String, Object>();

        String instanceName = OpenUtil.generateInstanceName("ecs");
        model.put("instanceName", instanceName);

	    return new ModelAndView(url, model);
	}

	@RequestMapping("/checkEcsName")
	@ResponseBody
	public String checkEcsName(@RequestBody Map<String, Object> params) {
		return ecsInstance.checkEcsName(params);
	}

	// 保存实例
	@RequestMapping("/saveInstance")
	@ResponseBody
	public String saveInstance(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = ecsInstance.applyInstance(params);

		result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
	}
}
