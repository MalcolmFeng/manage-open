package com.inspur.bigdata.manage.open.service.controller;

import com.inspur.bigdata.manage.open.service.data.ServiceGroup;
import com.inspur.bigdata.manage.open.service.service.IServiceGroupService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loushang.framework.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/service/group")
public class ServiceGroupController {

	private static final Log log = LogFactory.getLog(ServiceGroupController.class);

	@Autowired
	private IServiceGroupService groupService;

	@RequestMapping({ "/getPage" })
	public String toApplyListIndex()
	{
		return "service/group/list";
	}

	@RequestMapping("/list/{pid}")
	@ResponseBody
	public Map<String, Object> getServiceGroupList(@PathVariable("pid") String pid,
			@RequestBody Map<String, Object> param) {
		param.put("parentId", pid);
		List<ServiceGroup> list = groupService.getGroupList(param);
		int total = PageUtil.getTotalCount();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("total", total);

		return result;
	}
	
	@RequestMapping("/list/data")
	@ResponseBody
	public Map<String, Object> getServiceGroupListData() {
		Map<String, Object> param = new HashMap<String, Object>();
		List<ServiceGroup> list = groupService.getGroupList(param);
		int total = PageUtil.getTotalCount();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("total", total);

		return result;
	}

	@RequestMapping("/get/{id}")
	@ResponseBody
	public ServiceGroup getServiceGroupById(@PathVariable("id") String groupId) {
		ServiceGroup group = groupService.getById(groupId);
		return group;
	}

	@RequestMapping("/edit/{id}")
	public ModelAndView editServiceGroupById(@PathVariable("id") String groupId) {
		ServiceGroup group = groupService.getById(groupId);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("group", group);
		return new ModelAndView("/service/group/edit", model);
	}

	@RequestMapping("/save")
	@ResponseBody
	public Map<String, String> saveServiceGroup(@RequestParam Map<String, String> param) {
		Map<String,String> result=new HashMap<String,String>();
		String msg="保存失败";
		ServiceGroup group = new ServiceGroup();
		group.setId(param.get("id"));
		group.setName(param.get("name"));
		group.setParentId(param.get("parentId"));
		group.setSeq(Integer.valueOf(param.get("seq")));
		
		boolean isRegisted = groupService.isRegisted(group.getParentId(),group.getName());

//		try {
//			if(!isRegisted) {
//				if (group.getId() == null || "".equals(group.getId())) {
//					groupService.insert(group);
//					result.put("msg","保存分组成功");
//				} else {
//					groupService.update(group);
//					result.put("msg","更新分组成功");
//				}
//				result.put("flag","true");
//				return result;
//			} else {
//				return false;
//			}
//
//		} catch (Exception e) {
//			log.error("保存服务分组出错.", e);
//			return false;
//		}

		try {
			if(!isRegisted) {
				if (group.getId() == null || "".equals(group.getId())) {
					groupService.insert(group);
					result.put("msg","保存分组成功");
				} else {
					groupService.update(group);
					result.put("msg","更新分组成功");
				}
				result.put("flag","true");
				return result;
			} else {
				//groupService.update(group);
				if (group.getId() == null || "".equals(group.getId())){
					msg="已存在相同分组!";
					result.put("msg",msg);
					result.put("flag","false");
					return result;
				}
				ServiceGroup dg=groupService.getById(param.get("id"));
				if(dg.getName().equals(param.get("name"))&&!dg.getSeq().equals(param.get("seq")))
				{
					groupService.update(group);
					result.put("msg","更新分组成功");
					result.put("flag","true");
					return result;
				}else{
					msg="更新数据分组出错";
					result.put("msg",msg);
					result.put("flag","false");
					return result;
				}


			}

		} catch (Exception e) {
			log.error("保存数据分组出错.", e);
			return result;
		}
	}

	@RequestMapping("/delete/{id}")
	@ResponseBody
	public boolean deleteServiceGroupById(@PathVariable("id") String groupId) {
		try {
			groupService.delete(groupId);
			return true;
		} catch (Exception e) {
			log.error("删除服务分组出错.", e);
			return false;
		}
	}
	
}
