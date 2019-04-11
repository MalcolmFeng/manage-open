package com.inspur.bigdata.manage.open.data.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.inspur.bigdata.manage.open.data.data.DataDef;
import com.inspur.bigdata.manage.open.data.data.DataGroup;
import com.inspur.bigdata.manage.open.data.service.IDataTableColumnService;
import com.inspur.bigdata.manage.open.data.service.IDataGroupService;
import com.inspur.bigdata.manage.open.data.service.IOpenDataService;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import com.inspur.bigdata.manage.utils.StringUtil;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import sdk.security.authc.AuthenticationProvider;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository("opendataApiController")
@Controller
@RequestMapping(value = "/open/data/api")
public class OpenDataApiController {
	private static Log log = LogFactory.getLog(OpenDataApiController.class);

	private static SimpleDateFormat sf=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

	@Autowired
	private IOpenDataService openDataService;
	@Autowired
	private IDataGroupService groupService;

	/**
	 * 添加数据
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/putInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getApplyList( final HttpServletRequest httpServletRequest,@RequestBody  DataDef dataDef) {
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
		dataDef.setId(UUIDGenerator.getUUID().toString());
		dataDef.setProvider(OpenDataConstants.system_user);
		dataDef.setAuditUser(OpenDataConstants.system_user);
		dataDef.setCreateTime(sf.format(new Date()));
		dataDef.setAuditStatus(OpenDataConstants.data_audit_pass);
		dataDef.setOnlineTime(sf.format(new Date()));
		DataDef info=openDataService.addDataDefForApi(dataDef);
		map.put("result",true);
		map.put("data",info);
		return map;
	}
}
