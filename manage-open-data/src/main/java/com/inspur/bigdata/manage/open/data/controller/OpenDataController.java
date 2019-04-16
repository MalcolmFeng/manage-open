package com.inspur.bigdata.manage.open.data.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.inspur.bigdata.manage.cluster.tenant.api.TenantClusterService;
import com.inspur.bigdata.manage.common.utils.PropertiesUtil;
import com.inspur.bigdata.manage.open.data.data.DataApply;
import com.inspur.bigdata.manage.open.data.data.DataDef;
import com.inspur.bigdata.manage.open.data.data.DataTableColumn;
import com.inspur.bigdata.manage.open.data.data.DataGroup;
import com.inspur.bigdata.manage.open.data.service.IDataApplyService;
import com.inspur.bigdata.manage.open.data.service.IDataTableColumnService;
import com.inspur.bigdata.manage.open.data.service.IDataGroupService;
import com.inspur.bigdata.manage.open.data.service.IOpenDataService;
import com.inspur.bigdata.manage.utils.HttpUtil;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import com.inspur.bigdata.manage.utils.StringUtil;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.HttpRequestUtils;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sdk.security.authc.AuthenticationProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Repository("opendataController")
@Controller
@RequestMapping(value = "/open/data")
public class OpenDataController {
	private static Log log = LogFactory.getLog(OpenDataController.class);

	@Autowired
	private IOpenDataService openDataService;
	@Autowired
	private IDataGroupService groupService;
	@Autowired
	private IDataTableColumnService dataTableColumnService;
	@Autowired
	private IDataApplyService dataApplyService;

	/**
	 * 数据发布列表页面
	 * @return
	 */
	@RequestMapping(value = "/getPage", method = RequestMethod.GET)
	public ModelAndView getPage() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("isSuperAdmin",OpenDataConstants.isSuperAdmin(OpenDataConstants.getRealm()));
		return new ModelAndView("data/data/list",model);
	}
	@RequestMapping(value = "/getList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(@RequestBody Map<String, String> parameters) {
		Map<String, Object> mpMap = new HashMap<String, Object>();
		if(!OpenDataConstants.isSuperAdmin(OpenDataConstants.getRealm())){//非管理员，查询提供者是当前登录用户
			parameters.put("provider", OpenDataConstants.getUserId());
		}
		List<DataDef> dataDefs = openDataService.listDataDefs(parameters);
		if (StringUtil.isEmpty(dataDefs)) {
			mpMap.put("total", 0);
			mpMap.put("data", new ArrayList<DataDef>());
			return mpMap;
		}
		// 排序
		int total = PageUtil.getTotalCount();
		mpMap.put("total", total != -1 ? total : dataDefs.size());
		mpMap.put("data", dataDefs);
		return mpMap;
	}
	/**
	 * 数据申请页面
	 * @return
	 */
	@RequestMapping(value = "/getApplyPage", method = RequestMethod.GET)
	public ModelAndView getApplyPage() {
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parentId",-1);
		List<DataGroup> groupList=groupService.getGroupList(param);
		model.put("groupList",groupList);
		return new ModelAndView("data/data/applyList",model);
	}

	/**
	 * 数据申请页面
	 * @return
	 */
	@RequestMapping(value = "/getApiList", method = RequestMethod.GET)
	public ModelAndView getApplyPage2() {
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parentId",-1);
		List<DataGroup> groupList=groupService.getGroupList(param);
		model.put("groupList",groupList);
		return new ModelAndView("data/data/apiList",model);
	}
	/**
	 * 查询可申请数据
	 *
	 * @param parameters
	 * @return
	 */
	@RequestMapping(value = "/getApplyList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getApplyList(@RequestParam Map<String, Object> parameters) {
		Map<String, Object> mpMap = new HashMap<String, Object>();
		parameters.put("auditStatus", OpenDataConstants.data_audit_pass);

		String groupId=parameters.get("groupId").toString();
		Map<String, Object> param=new HashMap<String,Object>();
		param.put("parentId", groupId);
		List<String> groupArr=new ArrayList<String>();
		groupArr.add(groupId);

		List<DataGroup> list = groupService.getGroupList(param);
		if(list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				groupArr.add(list.get(i).getId().toString());
			}
			parameters.put("groupArr",groupArr);
			parameters.put("groupId","");
		}else {
			//当前没有子组,最后的组
			parameters.put("groupId",groupId);

		}

		List<DataDef> dataDefs = openDataService.listDataDefs(parameters);
		if (StringUtil.isEmpty(dataDefs)) {
			mpMap.put("total", 0);
			mpMap.put("data", new ArrayList<DataDef>());
			return mpMap;
		}
		// 排序
		int total = PageUtil.getTotalCount();
		mpMap.put("total", total != -1 ? total : dataDefs.size());
		mpMap.put("data", dataDefs);

		return mpMap;
	}
	/**
	 * 数据申请页面
	 * @return
	 */
	@RequestMapping(value = "/remotePage", method = RequestMethod.GET)
	public ModelAndView remotePage() {
		Map<String, Object> model = new HashMap<String, Object>();
		//获取集群列表
//		Map<String,String> clusterParam = TenantClusterService.getTenantClusterInfoByRealm(OpenDataConstants.getRealm());
		Map<String,String> clusterParam=new HashMap<>();
		clusterParam.put("clusterId","1");
		clusterParam.put("clusterName","测试");
		List<Map<String, String>> allCluster = new ArrayList<Map<String, String>>();
		allCluster.add(clusterParam);
		model.put("clusterList",allCluster);
		return new ModelAndView("data/data/remoteList",model);
	}
	@RequestMapping(value = "/remoteList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> remoteList(@RequestBody Map<String, String> parameters) {
		String clusterId=parameters.get("clusterId").toString();
		String clusterName=parameters.get("clusterName").toString();
		//调用接口查询列表数据
		String url="";
		String str= HttpUtil.execGet("",url);
		//解析数据列表
		List<DataDef> remoteList=getRemoteList(str,clusterId,clusterName);
		//查询发布
		Map<String, Object> mpMap = new HashMap<String, Object>();
		mpMap.put("provider",OpenDataConstants.system_user);
		List<DataDef> localList =openDataService.listDataDefs(mpMap);
		//去重分页
		List<DataDef> newList = new ArrayList<>();
		for (DataDef d : remoteList) {
			boolean b = localList.stream().anyMatch(u -> !StringUtil.isEmpty(u.getRemoteId())&&u.getRemoteId().equals(d.getRemoteId()));
			if (!b) {
				newList.add(d);
			}
		}
        int  start=Integer.parseInt(parameters.get("start").toString());
		int  limit=Integer.parseInt(parameters.get("limit").toString());
		int  total=newList.size();
        List<DataDef> dataList = new ArrayList<>();
		int toIndex = start + limit;
		if(toIndex > total){
			toIndex = total;
		}
		if(start<=total){
			for(int i=start;i<toIndex;i++){
				dataList.add(newList.get(i));
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",total);
		result.put("data", dataList);
		return result;
	}
	//获取集群
	@RequestMapping(value = "/getCluster", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> getCluster(HttpServletRequest request) {
		Map<String,String> clusterParam = TenantClusterService.getTenantClusterInfoByRealm(OpenDataConstants.getRealm());
		List<Map<String, String>> allCluster = new ArrayList<Map<String, String>>();
		allCluster.add(clusterParam);
		return allCluster;
	}
	/**
	 * 跳转发布页面
	 * @return
	 */
	@RequestMapping("/release")
	@ResponseBody
	public ModelAndView release(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parentId",-1);
		List<DataGroup> groupList=groupService.getGroupList(param);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("groupList",groupList);
		model.put("edit",false);
		model.put("dataDef",new DataDef());
		return new ModelAndView("data/data/register",param);
	}
	/**
	 * 跳转远程数据发布页面
	 * @return
	 */
	@RequestMapping("/release/remote")
	@ResponseBody
	public ModelAndView releaseRemote(HttpServletRequest request,@RequestBody  DataDef dataDef) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parentId",-1);
		List<DataGroup> groupList=groupService.getGroupList(param);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("groupList",groupList);
		model.put("edit",false);
		model.put("dataDef",dataDef);
		return new ModelAndView("data/data/register",model);
	}
	/**
	 * 发布数据
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> register( final HttpServletRequest httpServletRequest,@RequestBody  DataDef dataDef) {
		Map<String, Object> map=new HashMap<>();
		if(null==dataDef||dataDef.getColumnList()==null){
			map.put("result",false);
			map.put("message","数据不全");
			return map;
		}
		if(StringUtil.isEmpty(dataDef.getName())){
			map.put("result",false);
			map.put("message","名称为空");
			return map;
		}
		if(StringUtil.isEmpty(dataDef.getTableName())){
			map.put("result",false);
			map.put("message","数据库表为空");
			return map;
		}
		if(!StringUtil.isEmpty(dataDef.getGroupId())){
			DataGroup group=groupService.getById(dataDef.getGroupId());
			if(group==null){
				map.put("result",false);
				map.put("message","数据分组不存在");
				return map;
			}
		}else{
			map.put("result",false);
			map.put("message","数据分组不存在");
			return map;
		}
		String dburl = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "od.domain")+ "/service/rest/source/getSourceByUser?userId="+ OpenDataConstants.getUserId();
		String resultStr = HttpRequestUtils.get(dburl);
		List<JSONObject> list = OpenDataConstants.getJsonParse(resultStr);
		String jdbcurl=null;
		for(JSONObject jsonObject:list){
			if(StringUtil.isNotEmpty(dataDef.getDataSourceId())&&dataDef.getDataSourceId().equalsIgnoreCase(jsonObject.getString("dataSourceId"))){
				jdbcurl=jsonObject.getString("ip");
			}
		}
		dataDef.setJdbcUrl(jdbcurl);
		if(OpenDataConstants.isSuperAdmin(OpenDataConstants.getRealm())){
			dataDef.setProvider(OpenDataConstants.system_user);
			dataDef.setAuditUser(OpenDataConstants.system_user);
			dataDef.setAuditStatus(OpenDataConstants.data_audit_pass);
			dataDef.setOnlineTime(OpenDataConstants.sf.format(new Date()));
		}else{
			dataDef.setProvider(OpenDataConstants.getUserId());
			dataDef.setAuditStatus(OpenDataConstants.data_submit_audit);
		}
		dataDef.setUpdateTime(OpenDataConstants.sf.format(new Date()));

		if(StringUtil.isNotEmpty(dataDef.getId())){
			dataDef.setCreateTime(openDataService.getDataDef(dataDef.getId()).getCreateTime());
			openDataService.update(dataDef);
			 map.put("result","editsuccess");
		}else{
			dataDef.setId(UUIDGenerator.getUUID().toString());
			dataDef.setCreateTime(OpenDataConstants.sf.format(new Date()));
			openDataService.addDataDefForApi(dataDef);
			map.put("result",true);
		}
		map.put("auth","success");
		return map;
	}
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Boolean delete(@PathVariable("id") String id){
	    boolean result = false;
        try {
            openDataService.delete(id);
            result = true;
        } catch (Exception e) {
            result = false;
            log.error("删除失败", e);
        }
        return result;
    }
    /**
     * 跳转发布更新页面
     * @return
     */
	@RequestMapping("/toUpdate/{id}")
	@ResponseBody
	public ModelAndView update(@PathVariable("id") String id) {
		DataDef dataDef = openDataService.getDataDef(id);
		List<Map> list = new ArrayList<>();
		String dataexample = dataDef.getDataExample();
		JSONArray jsonArray = JSONArray.fromObject(dataexample);
		for(Object object:jsonArray){
			JSONObject jsonObject = JSONObject.fromObject(object);
			Map<Object,Object> map = (Map) jsonObject;
			list.add(map);
		}
		dataDef.setList(list);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parentId",-1);
		List<DataGroup> groupList=groupService.getGroupList(param);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("groupList",groupList);
		model.put("edit",true);
		model.put("dataDef",dataDef);
		return new ModelAndView("data/data/register",model);
	}

	/**
	 * 查看详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/get/{id}")
	@ResponseBody
	public ModelAndView getInfoById(@PathVariable("id") String id) {
		DataDef dataDef = openDataService.getDataDef(id);
		List<Map> list = new ArrayList<>();
		String dataexample = dataDef.getDataExample();
		JSONArray jsonArray = JSONArray.fromObject(dataexample);
		for(Object object:jsonArray){
			JSONObject jsonObject = JSONObject.fromObject(object);
			Map<Object,Object> map = (Map) jsonObject;
			list.add(map);
		}
		dataDef.setList(list);
		Map<String, Object> param = new HashMap<String, Object>();
		if(null!=dataDef){
			List<DataTableColumn> columns=dataTableColumnService.listTableColumnsByDataId(id);
			dataDef.setColumnList(columns);
		}
		param.put("serviceInfo",dataDef);
		param.put("apply",false);
		return new ModelAndView("data/data/info",param);
	}

	/**
	 * 详情显示申请按钮
	 * @param id
	 * @return
	 */
	@RequestMapping("/get/{id}/apply")
	@ResponseBody
	public ModelAndView getInfoForApplyById(@PathVariable("id") String id) {
		DataDef dataDef = openDataService.getDataDef(id);
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map> list = new ArrayList<>();
		//
		String url = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "od.domain")+"/service/rest/source/getResourceDetail?dataResourceId="+dataDef.getRemoteId();
		String resultStr = HttpRequestUtils.get(url);
		JSONObject json = JSONObject.fromObject(resultStr);
		//////
		JSONArray jsonArray = json.getJSONArray("exampleData");
		for(Object object:jsonArray){
			JSONObject jsonObject = JSONObject.fromObject(object);
			Map<Object,Object> map = (Map) jsonObject;
			list.add(map);
		}
		dataDef.setList(list);
		if(null!=dataDef){
			List<DataTableColumn> columns=dataTableColumnService.listTableColumnsByDataId(id);
			dataDef.setColumnList(columns);
		}
		param.put("serviceInfo",dataDef);
		param.put("apply",true);
		return new ModelAndView("data/data/info",param);
	}

	/**
	 * 数据申请操作
	 * @param id
	 * @return
	 */
	@RequestMapping("/apply/{id}")
	@ResponseBody
	public Map<String, Object> applyDataDef(@PathVariable("id") String id) {
		Map<String, Object> result=new HashMap<>();
		try {
			DataDef dataDef = openDataService.getDataDef(id);
			if(dataDef==null){
				result.put("result",false);
				result.put("message","申请数据不存在");
				return result;
			}
			if(dataDef.getAuditStatus()==null||!dataDef.getAuditStatus().equals(OpenDataConstants.data_audit_pass)){
				result.put("result",false);
				result.put("message","数据状态不允许申请");
				return result;
			}
			//查询是否申请过
			String userId=OpenDataConstants.getUserId();
			Map<String,Object> applyMap=new HashMap<>();
			applyMap.put("applicant",userId);
			applyMap.put("dataDefId",dataDef.getId());
			List<String> statusList=new ArrayList<>();
			statusList.add(OpenDataConstants.auth_status_submit);
			statusList.add(OpenDataConstants.auth_status_pass);
			applyMap.put("authStatusStr",statusList);
			List<DataApply>  list=dataApplyService.getUserApplyList(applyMap);
			if(list!=null&&list.size()>0){
				result.put("result",false);
				result.put("message","数据已申请不允许重复申请");
				return result;
			}
			DataApply dataApply =new DataApply();
			dataApply.setId(UUIDGenerator.getUUID().toString());
			dataApply.setDtDataId(dataDef.getId());
			dataApply.setApplyTime(OpenDataConstants.sf.format(new Date()));
			dataApply.setApplicant(userId);
			if("0".equals(dataDef.getAuthType()))
			{
				dataApply.setAuthTime(OpenDataConstants.sf.format(new Date()));
				dataApply.setAuthStatus(OpenDataConstants.auth_status_pass);
				dataApply.setAuthUser(OpenDataConstants.getUserId());
			}else{
				dataApply.setAuthStatus(OpenDataConstants.auth_status_submit);
			}

			dataApplyService.insertDataApply(dataApply);
			result.put("result",true);
		} catch (Exception e) {
			log.error("数据申请出错.", e);
			result.put("result",false);
			result.put("message",e.getMessage());
		}
		return result;
	}

	private List<DataDef> getRemoteList(String defs,String clusterId,String  clusterName){
		 defs="[{\"name\":\"aa\"," +
				 "\"tableName\":\"t1\"," +
				 "\"dataExample\":\"sdsds\"," +
				 "\"description\":\"ssdsds\"," +
				 "\"dbName\":\"dev\"," +
				 "\"hdfsURL\":\"hdfs/dev/\"," +
				 "\"id\":\"1\"," +
				 "\"list\":[]}]";
		JSONArray jsonArray=JSONArray.fromObject(defs);
		List<DataDef> remoteList=new ArrayList<DataDef>();
		Iterator<Object> it = jsonArray.iterator();
		while (it.hasNext()) {
			JSONObject ob = (JSONObject) it.next();
			DataDef info=new DataDef();
			info.setName(ob.get("name").toString());
			info.setTableName(ob.get("tableName").toString());
			info.setDataExample(ob.get("dataExample").toString());
			info.setDescription(ob.get("description").toString());
			info.setDbName(ob.get("dbName").toString());
			info.setRemoteId(ob.get("id").toString());
			info.setProvider(OpenDataConstants.system_user);

			List<DataTableColumn> columns=new ArrayList<>();
			JSONArray columnArray=ob.getJSONArray("list");
			Iterator<Object> clit = columnArray.iterator();
			while (clit.hasNext()){
				DataTableColumn cl=new DataTableColumn();
				JSONObject cljson = (JSONObject) it.next();
				cl.setColumnName(cljson.getString(""));
				cl.setColumnType(cljson.getString(""));
				cl.setIsNull(cljson.getInt(""));
				cl.setDescription(cljson.getString(""));
				columns.add(cl);
			}
			info.setColumnList(columns);
			remoteList.add(info);
		}
		return remoteList;
	}

	@RequestMapping(value = "/getAuditPage")
	public String getAuditPage(){
		return "data/audit/list";
	}
	@RequestMapping(value = "/listAudit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getDataAuditList(@RequestBody Map<String, String> parameters) {
		Map<String, Object> mpMap = new HashMap<String, Object>();
		parameters.put("auditStatus",OpenDataConstants.data_submit_audit);
		if(StringUtil.isNotEmpty(parameters.get("name"))){
			parameters.put("name",parameters.get("name"));
		}
		List<DataDef> dataDefs = openDataService.listDataDefs(parameters);
		if (StringUtil.isEmpty(dataDefs)) {
			mpMap.put("total", 0);
			mpMap.put("data", new ArrayList<DataDef>());
			return mpMap;
		}
		// 排序
		int total = PageUtil.getTotalCount();
		mpMap.put("total", total != -1 ? total : dataDefs.size());
		mpMap.put("data", dataDefs);
		return mpMap;
	}
	@RequestMapping("/pass/{id}")
	@ResponseBody
	public boolean doOnline(@PathVariable("id") String openServiceId) {
		Map<String,String> map = new HashMap<>();
		map.put("id",openServiceId);
		map.put("auditUser",OpenDataConstants.getUserId());
		map.put("updateTime",OpenDataConstants.sf.format(new Date()));
		map.put("auditStatus", OpenDataConstants.data_audit_pass);
		try {
			openDataService.updateDataDef(map);
		} catch (Exception e) {
				log.error("数据上线失败", e);
			return false;
		}
		return true;
	}

	@RequestMapping("/reject/{id}")
	@ResponseBody
	public boolean doReject(@PathVariable("id") String openServiceId) {
		Map<String,String> map = new HashMap<>();
		map.put("id",openServiceId);
		map.put("auditUser",OpenDataConstants.getUserId());
		map.put("updateTime",OpenDataConstants.sf.format(new Date()));
		map.put("auditStatus", OpenDataConstants.data_audit_reject);
		try {
			openDataService.updateDataDef(map);
		} catch (Exception e) {
				log.error("服务驳回失败", e);
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/getonLineData")
	public String getonLinePage(){
		return "data/data/offlineData";
	}

	@RequestMapping(value = "/getOnLineData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getOnLineData(@RequestBody Map<String, String> parameters) {
		Map<String, Object> mpMap = new HashMap<String, Object>();
		parameters.put("auditStatus",OpenDataConstants.data_audit_pass);
		if(StringUtil.isNotEmpty(parameters.get("name"))){
			parameters.put("name",parameters.get("name"));
		}
		List<DataDef> dataDefs = openDataService.listDataDefs(parameters);
		if (StringUtil.isEmpty(dataDefs)) {
			mpMap.put("total", 0);
			mpMap.put("data", new ArrayList<DataDef>());
			return mpMap;
		}
		// 排序
		int total = PageUtil.getTotalCount();
		mpMap.put("total", total != -1 ? total : dataDefs.size());
		mpMap.put("data", dataDefs);
		return mpMap;
	}

	@RequestMapping("/offline/{id}")
	@ResponseBody
	public boolean doOffline(@PathVariable("id") String openServiceId) {
		Map<String,String> map = new HashMap<>();
		map.put("id",openServiceId);
		map.put("auditUser",OpenDataConstants.getUserId());
		map.put("updateTime",OpenDataConstants.sf.format(new Date()));
		map.put("auditStatus", OpenDataConstants.data_audit_offline);
		try {
			openDataService.updateDataDef(map);
		} catch (Exception e) {
			log.error("数据服务下线失败", e);
			return false;
		}
		return true;
	}
}
