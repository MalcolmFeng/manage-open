package com.inspur.bigdata.manage.open.data.controller;


import com.inspur.bigdata.manage.cluster.tenant.api.TenantClusterService;
import com.inspur.bigdata.manage.common.utils.PropertiesUtil;
import com.inspur.bigdata.manage.open.data.data.DataApply;

import com.inspur.bigdata.manage.open.data.data.DataDef;
import com.inspur.bigdata.manage.open.data.data.DataTableColumn;
import com.inspur.bigdata.manage.open.data.service.IDataApplyService;

import com.inspur.bigdata.manage.open.data.service.IOpenDataService;
import com.inspur.bigdata.manage.ranger.util.RangerAPI;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import com.inspur.bigdata.manage.utils.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Repository("dataApplyController")
@Controller
@RequestMapping(value = "/data/apply")
public class DataApplyController {
	private static Logger logger = LoggerFactory.getLogger(DataApplyController.class);
	private static List<String> userList = new ArrayList<String>();
	@Autowired
	IDataApplyService dataApplyService;
	@Autowired
	private IOpenDataService openDataService;

	@RequestMapping({ "/getPage" })
	public String toApplyListIndex()
	{
		return "data/apply/list";
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getDataApplyList(@RequestBody Map<String, String> parameters) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("limit",parameters.get("limit"));
		param.put("start",parameters.get("start"));
		String userId= OpenDataConstants.getUserId();
		if(StringUtil.isNotEmpty(parameters.get("tableName")))
		{
			param.put("name",parameters.get("tableName"));
		}else {
			param.put("name","");
		}
		param.put("auditStatus",parameters.get("authStatus"));
		/*if(!OpenDataConstants.isSuperAdmin(OpenDataConstants.getRealm())){
			param.put("userId",userId);
		}*/
		param.put("applicant",userId);
		List<DataApply> list = dataApplyService.getDataApplyList(param);
		System.out.println("dataApplyList:"+list);
		int total = PageUtil.getTotalCount();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("total", total);

		return result;
	}

	@RequestMapping(value = "/dataauth", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getDataAuthList(@RequestBody Map<String, String> parameters) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("limit",parameters.get("limit"));
		param.put("start",parameters.get("start"));
		String userId= OpenDataConstants.getUserId();
		if(StringUtil.isNotEmpty(parameters.get("tableName")))
		{
			param.put("name",parameters.get("tableName"));
		}else {
			param.put("name","");
		}
		param.put("auditStatus",parameters.get("authStatus"));
		param.put("provider",userId);
		List<DataApply> list = dataApplyService.getDataApplyList(param);
		System.out.println("dataApplyList:"+list);
		int total = PageUtil.getTotalCount();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("total", total);

		return result;
	}
	@RequestMapping(value = "/listhistory", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getListhistory(@RequestBody Map<String, String> parameters) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("limit",parameters.get("limit"));
		param.put("start",parameters.get("start"));
		String userId= OpenDataConstants.getUserId();
		if(StringUtil.isNotEmpty(parameters.get("tableName")))
		{
			param.put("name",parameters.get("tableName"));
		}else {
			param.put("name","");
		}
		param.put("auditStatus",OpenDataConstants.auth_status_pass);
		/*if(!OpenDataConstants.isSuperAdmin(OpenDataConstants.getRealm())){*/
			param.put("applicant",userId);
		//}
		List<DataApply> list = dataApplyService.getDataApplyList(param);
		param.put("auditStatus",OpenDataConstants.auth_status_reject);
		List<DataApply> listReject = dataApplyService.getDataApplyList(param);
		list.addAll(listReject);
		int total = PageUtil.getTotalCount();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", list);
		result.put("total", total);
		return result;
	}
	@RequestMapping(value = "/getAuthPage", method = RequestMethod.GET)
	public String getAuthPage() {
		return "data/auth/list";
	}

	@RequestMapping("/pass/{id}")
	@ResponseBody
	public Map<String, Object> authPassApply(@PathVariable("id") String id) {
		Map<String, Object> result=new HashMap<>();
		try {
			DataApply dataApply = dataApplyService.getById(id);
			DataDef dataDef = openDataService.getDataDef(dataApply.getDtDataId());
			if (dataDef == null) {
				result.put("result", false);
				result.put("message", "申请数据不存在,授权不成功");
				return result;
			}
			if (dataDef.getAuditStatus() == null || !dataDef.getAuditStatus().equals(OpenDataConstants.data_audit_pass)) {
				result.put("result", false);
				result.put("message", "数据审核不通过，不允许授权");
				return result;
			}

			List<String> resAuth = new ArrayList<>();
			resAuth.add(OpenDataConstants.select);
			String applicant = dataApply.getApplicant();
			Map<String, String> cluster = TenantClusterService.getTenantClusterInfo();
			String clusterId = cluster.get("clusterId");
			String clusterName = cluster.get("clusterName");
			//	String clusterId = "cluster4741";
			//	String clusterName ="cluster4741";

			String dburl = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "od.domain") + "/service/rest/source/getSourceByUser?userId=" + OpenDataConstants.getUserId();
			String resultStr = HttpRequestUtils.get(dburl);
			List<JSONObject> list = OpenDataConstants.getJsonParse(resultStr);
			String resPath = null;
			for (JSONObject jsonObject : list) {
				if (StringUtil.isNotEmpty(dataDef.getDataSourceId()) && dataDef.getDataSourceId().equalsIgnoreCase(jsonObject.getString("dataSourceId"))) {
					resPath = jsonObject.getString("instanceName");
				}
			}
			/**
			 * 数据集开放 table-name为多个表逗号分隔 循环授权
			 */
			if (StringUtils.isNotEmpty(dataDef.getTableName())) {
				for (String tableName : dataDef.getTableName().split(",", -1)) {
					String result_string = null;
					try {
						String url = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "mcs.domain") + "/manage/service/security/rest/saveHiveAuth?clusterId="
								+ clusterId + "&clusterName=" + clusterName + "&authUser=" + applicant + "&table=" + tableName + "&column=*" + "&resPath=" + resPath + "&resAuth=" + URLEncoder.encode(resAuth.toString(), "UTF-8");
						logger.error("<======================授权url:" + url);
						result_string = HttpRequestUtils.get(url);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					dataApply.setAuthTime(OpenDataConstants.sf.format(new Date()));
					dataApply.setAuthStatus(OpenDataConstants.auth_status_pass);
					dataApply.setAuthUser(OpenDataConstants.getUserId());
					if ("success".equalsIgnoreCase(result_string)) {
						dataApplyService.updateDataApply(dataApply);
						result.put("result", true);
					} else {
						result.put("result", false);
					}
				}
			}
		} catch (Exception e) {
			logger.error("数据授权出错.", e);
			result.put("result", false);
			result.put("message", e.getMessage());
		}
		return result;
	}
	@RequestMapping("/reject/{id}")
	@ResponseBody
	public Map<String, Object> authRejectApply(@PathVariable("id") String id) {
		Map<String, Object> result=new HashMap<>();
		try {
			DataApply dataApply=dataApplyService.getById(id);
			DataDef dataDef = openDataService.getDataDef(dataApply.getDtDataId());
			if(dataDef==null){
				result.put("result",false);
				result.put("message","申请数据不存在,授权不成功");
				return result;
			}
			if(dataDef.getAuditStatus()==null||!dataDef.getAuditStatus().equals(OpenDataConstants.data_audit_pass)){
				result.put("result",false);
				result.put("message","数据审核不通过，不允许授权");
				return result;
			}
			//判断是否是管理员
		/*	if(!OpenDataConstants.masert_realm.equals(OpenDataConstants.getRealm())){
				result.put("result",false);
				result.put("message","无权授权数据");
				return result;
			}*/
			String userId=OpenDataConstants.getUserId();
			dataApply.setAuthTime(OpenDataConstants.sf.format(new Date()));
			dataApply.setAuthStatus(OpenDataConstants.auth_status_reject);
			dataApply.setAuthUser(userId);
			dataApplyService.updateDataApply(dataApply);
			result.put("result",true);
		} catch (Exception e) {
			logger.error("数据授权出错.", e);
			result.put("result",false);
			result.put("message",e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/getAuthHistoryPage", method = RequestMethod.GET)
	public String getAuthHistoryPage() {
		return "data/auth/historyList";
	}

	@RequestMapping("/delete/{id}")
	@ResponseBody
	public boolean deleteServiceGroupById(@PathVariable("id") String id) {
		try {
			dataApplyService.delete(id);
			return true;
		} catch (Exception e) {
			logger.error("删除数据申请出错.", e);
			return false;
		}
	}

	/**
	 * 构建HDFS策略json
	 * @param clusterName
	 * @param policyName
	 * @param paths
	 * @param users
	 * @param accesses
	 * @return
	 */
	private static String formatHdfsPolicy(String clusterName, String policyName, List<String> paths, List<String> users, List<String> accesses){
		Map<String, Object> dataMap = new HashMap<String, Object>();

		//构建resourcesMap
		if(paths != null){
			Map<String, Object> resourcesMap = new HashMap<String, Object>();
			Map<String, Object> pathMap = new HashMap<String, Object>();
			pathMap.put("values", paths);
			pathMap.put("isRecursive", true);
			resourcesMap.put("path", pathMap);
			dataMap.put("resources", resourcesMap);
		}

		//构建polocyItemsList
		List<Object> policyItemsList = new ArrayList<Object>();
		boolean read = false;
		boolean write =false;
		boolean execute = false;

		//用户权限：读、写、执行
		Map<String, Object> policyItemsMap = new HashMap<String, Object>();
		if(accesses != null){
			for(String str:accesses){
				if("all".equals(str)){
					read = true;
					write = true;
					execute = true;
					break;
				}else if("read".equals(str)){
					read = true;
				}else if("write".equals(str)){
					write = true;
				}else if("execute".equals(str)){
					execute = true;
				}
			}

			List<Map<String,Object>> accessesList = new ArrayList<Map<String,Object>>();
			if(read){
				Map<String, Object> readMap = new HashMap<String, Object>();
				readMap.put("type", "read");
				readMap.put("isAllowed", true);
				accessesList.add(readMap);
			}
			if(write){
				Map<String, Object> writeMap = new HashMap<String, Object>();
				writeMap.put("type", "write");
				writeMap.put("isAllowed", true);
				accessesList.add(writeMap);
			}
			if(execute){
				Map<String, Object> executeMap = new HashMap<String, Object>();
				executeMap.put("type", "execute");
				executeMap.put("isAllowed", true);
				accessesList.add(executeMap);
			}
			policyItemsMap.put("accesses", accessesList);
		}

		//用户名
		if(users != null){
			policyItemsMap.put("users", users);
		}
		policyItemsMap.put("delegateAdmin", true);
		policyItemsList.add(policyItemsMap);

		//构建策略json
		dataMap.put("service", clusterName + "_hadoop");
		dataMap.put("name", policyName);
		dataMap.put("isEnabled", true);
		dataMap.put("isAuditEnabled", true);
		dataMap.put("description", "");
		dataMap.put("policyItems", policyItemsList);
		JSONObject jsonObject = JSONObject.fromObject(dataMap);
		return jsonObject.toString();
	}

	/**
	 * 新增Hive策略
	 * @param clusterName
	 * @param policyName
	 * @param paths
	 * @param tables
	 * @param columns
	 * @param users
	 * @param accesses
	 * @return
	 */
	private static String formatHivePolicy(String clusterName, String policyName, List<String> paths, List<String> tables, List<String> columns, List<String> users, List<String> accesses){
		Map<String, Object> dataMap = new HashMap<String, Object>();

		//构建resourcesMap
		Map<String, Object> resourcesMap = new HashMap<String, Object>();
		if(paths != null){
			Map<String, Object> dbMap = new HashMap<String, Object>();
			dbMap.put("values", paths);
			resourcesMap.put("database", dbMap);
		}

		if(tables != null){
			Map<String, Object> tableMap = new HashMap<String, Object>();
			tableMap.put("values", tables);
			resourcesMap.put("table", tableMap);
		}

		if(columns != null){
			Map<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("values", columns);
			resourcesMap.put("column", columnMap);
		}
		dataMap.put("resources", resourcesMap);

		//构建polocyItemsList
		List<Object> policyItemsList = new ArrayList<Object>();
		boolean select = false;


		//用户权限：查询(select)
		Map<String, Object> policyItemsMap = new HashMap<String, Object>();
		if(accesses != null){
			for(String str:accesses){
				if("select".equals(str)){
					select = true;
				}
			}

			List<Map<String,Object>> accessesList = new ArrayList<Map<String,Object>>();
			if(select){
				Map<String, Object> accessMap = new HashMap<String, Object>();
				accessMap.put("type", "select");
				accessMap.put("isAllowed", true);
				accessesList.add(accessMap);
			}

			policyItemsMap.put("accesses", accessesList);
		}

		//用户名
		if(users != null){
			policyItemsMap.put("users", users);
		}

		policyItemsMap.put("delegateAdmin", false);
		policyItemsList.add(policyItemsMap);

		//构建策略json
		dataMap.put("service", clusterName + "_hive");
		dataMap.put("name", policyName);
		dataMap.put("isEnabled", true);
		dataMap.put("isAuditEnabled", true);
		dataMap.put("description", "");
		dataMap.put("policyItems", policyItemsList);
		JSONObject jsonObject = JSONObject.fromObject(dataMap);
		return jsonObject.toString();
	}

}
