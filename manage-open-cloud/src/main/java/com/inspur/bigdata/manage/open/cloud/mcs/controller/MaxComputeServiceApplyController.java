package com.inspur.bigdata.manage.open.cloud.mcs.controller;

import com.inspur.bigdata.manage.open.cloud.mcs.data.BigDataService;
import com.inspur.bigdata.manage.open.cloud.mcs.data.BigDataServiceComponent;
import com.inspur.bigdata.manage.open.cloud.mcs.data.MaxComputeServiceInstance;
import com.inspur.bigdata.manage.open.cloud.mcs.service.IBigDataService;
import com.inspur.bigdata.manage.open.cloud.mcs.service.IBigDataServiceComponent;
import com.inspur.bigdata.manage.open.cloud.mcs.service.IMaxComputeServiceInstance;
import com.inspur.bigdata.manage.open.cloud.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/mcs")
public class MaxComputeServiceApplyController {

	@Autowired
	IBigDataService bigDataService;

	@Autowired
	IBigDataServiceComponent bigDataServiceComponent;

	@Autowired
	IMaxComputeServiceInstance maxComputeServiceInstance;

	// 返回大数据计算服务实例申请页面视图
	@RequestMapping("/apply")
	public ModelAndView getPage() {
		String url = "cloud/mcs/MaxComputeServiceApply";

		List<BigDataService> bigDataServices = bigDataService.getAllBigDataServices();

		Map<String, Object> model = new HashMap<String, Object>();
		MaxComputeServiceInstance instance = new MaxComputeServiceInstance();
		// 随机生成大数据计算服务实例名称
		instance.setInstanceName(OpenUtil.generateInstanceName("mcs"));

		model.put("mcs", instance);
		model.put("bigDataServices", bigDataServices);

		return new ModelAndView(url, model);
	}

	// 根据服务id查询大数据计算服务组件
	@RequestMapping("/getComponents")
	@ResponseBody
	public Map<String, Object> getComponents(@RequestBody Map<String, Object> params) {
		List<BigDataServiceComponent> bigDataServiceComponents = bigDataServiceComponent
				.getBigDataServiceComponentsByServiceId(params.get("service_id").toString());

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", bigDataServiceComponents);
		result.put("total", bigDataServiceComponents.size());

		return result;
	}

	// 申请大数据计算服务实例
	@RequestMapping("/saveInstance")
	@ResponseBody
	public String saveInstance(@RequestBody Map<String, Object> params) {
		Result result = new Result();

		Map<String, String> resultMap = maxComputeServiceInstance.applyInstance(params);

        result.setStatus(resultMap.get("status"));
		result.setMessage(resultMap.get("message"));

		return result.toString();
	}
}
