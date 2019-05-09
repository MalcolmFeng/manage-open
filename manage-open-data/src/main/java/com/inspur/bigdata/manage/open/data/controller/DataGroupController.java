package com.inspur.bigdata.manage.open.data.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.inspur.bigdata.manage.common.utils.PropertiesUtil;
import com.inspur.bigdata.manage.common.utils.RequestUtil;
import com.inspur.bigdata.manage.utils.HttpUtil;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.inspur.bigdata.manage.open.data.data.DataGroup;
import com.inspur.bigdata.manage.open.data.service.IDataGroupService;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;

@Controller
@RequestMapping("/data/group")
public class DataGroupController {

	private static final Log log = LogFactory.getLog(DataGroupController.class);

	@Autowired
	private IDataGroupService groupService;

	@RequestMapping({ "/getPage" })
	public String toGroupListIndex()
	{
		return "data/group/list";
	}

	@RequestMapping("/list/{pid}")
	@ResponseBody
	public Map<String, Object> getServiceGroupList(@PathVariable("pid") String pid,
			@RequestBody Map<String, Object> param) {
		param.put("parentId", pid);
		List<DataGroup> list = groupService.getGroupList(param);
		int total = PageUtil.getTotalCount();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("total", total);

		return result;
	}
	@RequestMapping("/dataorigin/{pid}")
	@ResponseBody
	public Map<String, Object> getDataOriginList(@PathVariable("pid") String pid,
												   @RequestBody Map<String, Object> param) {
		param.put("parentId", pid);
		String userId = OpenDataConstants.getUserId();
		//String userId = "superadmin";
		String url = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "od.domain")+ "/service/rest/source/getSourceByUser?userId="+ userId+"&sourceType=hive,kudu";
		String resultStr = HttpRequestUtils.get(url);
		List<JSONObject> list = OpenDataConstants.getJsonParse(resultStr);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		return result;
	}

	@RequestMapping("/table")
	@ResponseBody
	public Map<String, Object> getTableList(HttpServletRequest request,
											@RequestBody Map<String, Object> param) {
		String sourceId = request.getParameter("sourceId");
		String userId = OpenDataConstants.getUserId();
		//String userId = "superadmin";
		String url =PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "od.domain")+ "/service/rest/source/getResourceByUserAndSource?userId="+userId+"&sourceId="+sourceId;
		String resultStr = HttpRequestUtils.get(url);
		List<JSONObject> list = OpenDataConstants.getJsonParse(resultStr);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		return result;
	}


	@RequestMapping("/tableDetail")
	@ResponseBody
	public Map<String, Object> getTableDetail(HttpServletRequest request,
											@RequestBody Map<String, Object> param) {
		String tableId = request.getParameter("tableId");
		String url = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "od.domain")+"/service/rest/source/getResourceDetail?dataResourceId="+tableId;
		String resultStr = HttpRequestUtils.get(url);
		JSONObject json = JSONObject.fromObject(resultStr);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", json);
		return result;
	}
	@RequestMapping("/list/data")
	@ResponseBody
	public Map<String, Object> getServiceGroupListData() {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DataGroup> list = groupService.getGroupList(param);
		int total = PageUtil.getTotalCount();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("total", total);

		return result;
	}

	@RequestMapping("/get/{id}")
	@ResponseBody
	public DataGroup getServiceGroupById(@PathVariable("id") String groupId) {
		DataGroup group = groupService.getById(groupId);
		return group;
	}

	@RequestMapping("/edit/{id}")
	public ModelAndView editServiceGroupById(@PathVariable("id") String groupId) {
		DataGroup group = groupService.getById(groupId);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("group", group);
		return new ModelAndView("/data/group/edit", model);
	}

	@RequestMapping("/save")
	@ResponseBody
	public Map saveServiceGroup(@RequestParam Map<String, String> param) {
		Map<String,String> result=new HashMap<String,String>();
		String msg="保存失败";
		DataGroup group = new DataGroup();
		group.setId(param.get("id"));
		group.setName(param.get("name"));
		group.setParentId(param.get("parentId"));
		group.setSeq(Integer.valueOf(param.get("seq")));

		boolean isRegisted = groupService.isRegisted(group.getParentId(),group.getName());

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
				DataGroup dg=groupService.getById(param.get("id"));
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
