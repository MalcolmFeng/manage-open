package com.inspur.bigdata.manage.open.service.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.inspur.bigdata.manage.open.service.data.ServiceApply;
import com.inspur.bigdata.manage.open.service.data.ServiceDef;
import com.inspur.bigdata.manage.open.service.service.IAppManage;
import com.inspur.bigdata.manage.open.service.service.IServiceApplyService;
import com.inspur.bigdata.manage.open.service.service.IServiceDefService;
import com.inspur.bigdata.manage.open.service.util.OpenServiceConstants;
import com.inspur.bigdata.manage.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by songlili on 2019/2/13.
 */
@CrossOrigin
@Controller
@RequestMapping("/open/apply")
public class ServiceApplyController {
    private static Log log = LogFactory.getLog(ServiceApplyController.class);

    @Autowired
    private IServiceApplyService serviceApplyService;

    @Autowired
    private IServiceDefService serviceDefService;

    @Autowired
    private IAppManage appManage;

    /**
     * 我的申请列表页
     * @return
     */
    @RequestMapping({ "/getPage" })
    public String toApplyListIndex()
    {
        return "service/apply/list";
    }

    /**
     * API授权列表页
     * @return
     */
    @RequestMapping({"/getAuthPage"})
    public String toApplyAuthIndex(){return "service/auth/list";}

    //获取列表
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getDataAuthList(@RequestBody Map<String, String> parameters) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("limit",parameters.get("limit"));
        param.put("start",parameters.get("start"));
        String userId= OpenServiceConstants.getUserId();
        if(StringUtil.isNotEmpty(parameters.get("serviceName")))
        {
            param.put("name",parameters.get("serviceName"));
        }else {
            param.put("name","");
        }
        param.put("authStatus",parameters.get("authStatus"));
        param.put("userId",userId);
      //  List<ServiceApply> list = serviceApplyService.getServiceList(param);
        List <ServiceApply> list = serviceApplyService.getServiceAuthList(param);
        System.out.println("serviceList:"+list);
        int total = PageUtil.getTotalCount();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        result.put("total", total);

        return result;
    }
    //获取申请列表
    @RequestMapping(value = "/applylist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getDataApplyList(@RequestBody Map<String, String> parameters) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("limit",parameters.get("limit"));
        param.put("start",parameters.get("start"));
        String userId= OpenServiceConstants.getUserId();
      if(StringUtil.isNotEmpty(parameters.get("serviceName")))
        {
            param.put("name",parameters.get("serviceName"));
        }else {
            param.put("name","");
        }
        param.put("authStatus",parameters.get("authStatus"));
      /* if(!OpenServiceConstants.isSuperAdmin(OpenServiceConstants.getRealm())){
            param.put("userId",userId);
        }*/
        param.put("userId",userId);
        List<ServiceApply> list = serviceApplyService.getApplyList(param);
        System.out.println("serviceList:"+list);
        int total = PageUtil.getTotalCount();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        result.put("total", total);

        return result;
    }

    //同意操作
    @RequestMapping("/pass/{id}")
    @ResponseBody
    public Map<String, Object> authPassApply(@PathVariable("id") String id) {
        Map<String, Object> result=new HashMap<>();
        try {
            ServiceApply serviceApply=serviceApplyService.getById(id);
            ServiceDef serviceDef = serviceDefService.getServiceDef(serviceApply.getApi_service_id());
            if(serviceDef==null){
                result.put("result",false);
                result.put("message","申请api不存在,授权不成功");
                return result;
            }
            if(serviceDef.getAuditStatus()==null||!serviceDef.getAuditStatus().equals(OpenServiceConstants.api_audit_pass)){
                result.put("result",false);
                result.put("message","api审核不通过，不允许授权");
                return result;
            }
            //判断是否是管理员
           /*if(!OpenServiceConstants.masert_realm.equals(OpenServiceConstants.getRealm())){
                result.put("result",false);
                result.put("message","无权授权api");
                return result;
            }*/


            serviceApply.setAuth_time(OpenServiceConstants.sf.format(new Date()));
            serviceApply.setAuth_status(OpenServiceConstants.auth_status_pass);
            serviceApply.setAuth_user(OpenServiceConstants.getUserId());
            serviceApplyService.updateServiceApply(serviceApply);
            result.put("result",true);
        } catch (Exception e) {
            log.error("API授权出错.", e);
            result.put("result",false);
            result.put("message",e.getMessage());
        }
        return result;
    }

    // 批量授权
    @RequestMapping("/pass/batch/{id}")
    @ResponseBody
    public List<Map<String, Object>> authPassBatchApply(@PathVariable("id") String id) {
        List<Map<String, Object>> resultList = new ArrayList();
        List<Map<String,Object>> serviceApply = serviceApplyService.getByBatchId(id);
        for (Map<String,Object> temp : serviceApply){
            Map<String, Object> result = authPassApply( (String)temp.get("id"));
            resultList.add(result);
        }
        return resultList;
    }

    //驳回操作
    @RequestMapping("/reject/{id}")
    @ResponseBody
    public Map<String, Object> authRejectApply(@PathVariable("id") String id) {
        Map<String, Object> result=new HashMap<>();
        try {
            ServiceApply serviceApply=serviceApplyService.getById(id);
            ServiceDef serviceDef = serviceDefService.getServiceDef(serviceApply.getApi_service_id());
            if(serviceDef==null){
                result.put("result",false);
                result.put("message","申请api不存在,授权不成功");
                return result;
            }
            /***数据未审核状态可以驳回授权申请*/
//           if(serviceDef.getAuditStatus()==null||!serviceDef.getAuditStatus().equals(OpenServiceConstants.api_audit_pass)){
//                result.put("result",false);
//                result.put("message","api审核不通过，不允许授权");
//                return result;
//            }
            //判断是否是管理员
        /*   if(!OpenServiceConstants.masert_realm.equals(OpenServiceConstants.getRealm())){
                result.put("result",false);
                result.put("message","无权授权api");
                return result;
            }*/
            String userId=OpenServiceConstants.getUserId();
            serviceApply.setAuth_time(OpenServiceConstants.sf.format(new Date()));
            serviceApply.setAuth_status(OpenServiceConstants.auth_status_reject);
            serviceApply.setAuth_user(userId);
            serviceApplyService.updateServiceApply(serviceApply);
            result.put("result",true);
        } catch (Exception e) {
            log.error("api授权出错.", e);
            result.put("result",false);
            result.put("message",e.getMessage());
        }
        return result;
    }

    //app申请api市场
    @RequestMapping(value = "/apply",method = RequestMethod.POST)
    @ResponseBody
    public boolean AppServiceApply(@RequestParam Map<String, String> parameters,String batch_apply_id) {
        boolean flag=false;
        SimpleDateFormat df = OpenServiceConstants.sf;
        ServiceApply serviceApply=new ServiceApply();
        serviceApply.setId(UUIDGenerator.getUUID());
        serviceApply.setApp_id(parameters.get("appId"));
        serviceApply.setApp_name(parameters.get("appName"));

        String ids[]=parameters.get("openServiceId").split(",");
        for (int i=0;i<ids.length;i++) {
            String openServiceId = ids[i];
            ServiceDef serviceDef = serviceDefService.getServiceDef(openServiceId);
            if (OpenServiceConstants.auth_type_no.equals(serviceDef.getAuthType())) {
                //无需授权
                serviceApply.setAuth_status(OpenServiceConstants.auth_status_pass);
                serviceApply.setAuth_user(serviceDef.getProvider());
                serviceApply.setAuth_time(df.format(new Date()));
            } else {
                //需要授权
                serviceApply.setAuth_status(OpenServiceConstants.auth_status_submit);
            }
            serviceApply.setApi_service_id(openServiceId);
            serviceApply.setApi_service_name(serviceDef.getName());
            serviceApply.setApi_provider(serviceDef.getProvider());
            serviceApply.setApplicant(parameters.get("userId"));
            serviceApply.setApply_time(df.format(new Date()));
            serviceApply.setApply_flag(parameters.get("applyFlag"));

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("APPLICANT", parameters.get("userId"));
            param.put("appId", parameters.get("appId"));
            param.put("apiServiceId", openServiceId);
            List<ServiceApply> service_app = serviceApplyService.getList(param);
            log.error("====================>:" + service_app);
            if (0 == service_app.size()) {//添加
                serviceApply.setBatch_apply_id(batch_apply_id);
                String insert_flag = serviceApplyService.insert(serviceApply);
                if ("true".equals(insert_flag)) {
                    flag = true;
                }
            } else {//更新
                if ("2".equals(service_app.get(0).getAuth_status())) {
                    String id = service_app.get(0).getId();
                    serviceApply = new ServiceApply();

                    serviceApply.setId(id);
                    serviceApply.setAuth_status(OpenServiceConstants.auth_status_submit);
                    serviceApply.setApply_time(df.format(new Date()));
                    serviceApplyService.updateById(serviceApply);
                    flag = true;
                }
//                serviceApplyService.updateServiceApply(serviceApply);
            }
        }
        return flag;
    }

    //api授权个某个app
    @RequestMapping(value = "/auth",method = RequestMethod.POST)
    @ResponseBody
    public boolean AppServiceAuth(@RequestParam Map<String, String> parameters) {
        boolean flag=false;
        SimpleDateFormat df = OpenServiceConstants.sf;
        ServiceApply serviceApply=new ServiceApply();
        serviceApply.setId(UUIDGenerator.getUUID());
        serviceApply.setApp_id(parameters.get("appId"));
        serviceApply.setApp_name(parameters.get("appName"));
        serviceApply.setApi_service_id(parameters.get("openServiceId"));
        ServiceDef serviceDef=serviceDefService.getServiceDef(parameters.get("openServiceId"));
        serviceApply.setApi_service_name(serviceDef.getName());
        serviceApply.setApi_provider(serviceDef.getProvider());
        //申请相关
        serviceApply.setApplicant(parameters.get("userId"));
        serviceApply.setApply_time(df.format(new Date()));
        serviceApply.setApply_flag(parameters.get("applyFlag"));
        //授权相关
        serviceApply.setAuth_status(OpenServiceConstants.auth_status_pass);
        serviceApply.setAuth_user(OpenServiceConstants.getUserId());
        serviceApply.setAuth_time(df.format(new Date()));
        String insert_flag=serviceApplyService.insert(serviceApply);
        if("true".equals(insert_flag))
        {
            flag=true;
        }
        return flag;
    }

    /**
     * 授权记录
     * @return
     */
    @RequestMapping({ "/getAuthRecord" })
    public String toAuthRecordList()
    {
        return "service/auth/auth_record";
    }

    //获取列表
    @RequestMapping(value = "/authlist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getAPIAuthList(@RequestBody Map<String, String> parameters) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("limit",parameters.get("limit"));
        param.put("start",parameters.get("start"));
        String userId= OpenServiceConstants.getUserId();
        if(StringUtil.isNotEmpty(parameters.get("serviceName")))
        {
            param.put("name",parameters.get("serviceName"));
        }else {
            param.put("name","");
        }
        param.put("authStatus",parameters.get("authStatus"));
        param.put("userId",userId);
        List <ServiceApply> list = serviceApplyService.getAPIAuthList(param);
        int total = PageUtil.getTotalCount();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        result.put("total", total);

        return result;
    }
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public boolean deleteApply(@PathVariable("id") String id){
       boolean result=false;
       if (StringUtils.isNotEmpty(id)){
            serviceApplyService.deleteApplyById(id);
            result=true;
       }
       return result;
    }
}

