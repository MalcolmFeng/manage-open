package com.inspur.bigdata.manage.open.service.controller;

import com.inspur.bigdata.manage.open.service.data.AppInstance;
import com.inspur.bigdata.manage.open.service.data.DevGroup;
import com.inspur.bigdata.manage.open.service.data.ServiceApply;
import com.inspur.bigdata.manage.open.service.data.ServiceDef;
import com.inspur.bigdata.manage.open.service.service.IAppManage;
import com.inspur.bigdata.manage.open.service.service.IDevGroupService;
import com.inspur.bigdata.manage.open.service.service.IServiceApplyService;
import com.inspur.bigdata.manage.open.service.service.IServiceDefService;
import com.inspur.bigdata.manage.open.service.util.MD5Util;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/service/app")
public class AppManageController {

    private static final Log log = LogFactory.getLog(AppManageController.class);

    @Autowired
    private IAppManage appManage;
    @Autowired
    IServiceApplyService serviceApplyService;

    @Autowired
    IServiceDefService serviceDefService;

    @Autowired
    IDevGroupService devGroupService;

    @RequestMapping({ "/getPage" })
    public String toApplyListIndex()
    {
        return "app/list";
    }
    @RequestMapping("/list/getInstances")
    @ResponseBody
    public Map<String, Object> getAppList(@RequestBody Map<String, Object> parameters)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("limit",parameters.get("limit"));
        param.put("start",parameters.get("start"));
       String userId= OpenDataConstants.getUserId();
        param.put("userId", userId);
        if(parameters.get("tableName")==null||parameters.get("tableName").toString().length()==0)
        {
            param.put("appName","");
        }else{
            param.put("appName",parameters.get("tableName"));
        }

        List<AppInstance> list = appManage.getAppList(param);
        int total = PageUtil.getTotalCount();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        result.put("total", total);

        return result;
    }

    @RequestMapping("/list/data")
    @ResponseBody
    public Map<String, Object> getAppListData() {
        Map<String, Object> param = new HashMap<String, Object>();
        List<AppInstance> list = appManage.getAppList(param);
        int total = PageUtil.getTotalCount();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        result.put("total", total);

        return result;
    }

    @RequestMapping("/get/{id}")
    @ResponseBody
    public AppInstance getAppById(@PathVariable("id") String appId) {
        AppInstance app = appManage.getById(appId);
        return app;
    }

    @RequestMapping("/reset/{id}")
    @ResponseBody
    public AppInstance resetAppsecretById(@PathVariable("id") String appId) {
        AppInstance app = appManage.getById(appId);
        int appKey=(int)((Math.random()*9+1)*100000);
        String appSecret= MD5Util.md5(String.valueOf(appKey));
        app.setAppSecret(appSecret);
        appManage.update(app);
        return app;

    }
    @RequestMapping("/getInfo/{id}")
    @ResponseBody
    public ModelAndView getInfoById(@PathVariable("id") String appId) {
        AppInstance app = appManage.getById(appId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("app", app);
        return new ModelAndView("app/info",model);
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView editAppById(@PathVariable("id") String appId) {
        AppInstance app = appManage.getById(appId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("app", app);
        return new ModelAndView("/app/edit", model);
    }

    @RequestMapping("/save")
    @ResponseBody
    public Map<String, String> saveApp(@RequestParam Map<String, String> param) {
        Map<String,String> result=new HashMap<String,String>();
        String msg="保存失败";
        String appCreateTime = DateUtil.getCurrentTime2();
        String appUpdateTime = DateUtil.getCurrentTime2();
        AppInstance app = new AppInstance();
        app.setAppName(param.get("appName"));
        app.setAppDescription(param.get("appDescription"));
        String userId= OpenDataConstants.getUserId();
//        String userId = "uuuuuuuuser";
        //String userId= "test11-realm1234";
        app.setUserId(userId);
        boolean isRegisted = appManage.isRegisted(app.getAppName(),userId);


        try {
            if(!isRegisted) {//不存在同名应用
                if (param.get("appId") == null || "".equals(param.get("appId"))) {//第一次添加
                    int appKey=appManage.returnAppkey();
                    String appSecret= MD5Util.md5(String.valueOf(appKey));
                    app.setAppKey(String.valueOf(appKey));
                    app.setAppSecret(appSecret);
                    app.setAppCreateTime(appCreateTime);
                    appManage.insert(app);
                    result.put("msg","保存成功");
                } else {//编辑
                    AppInstance singleapp=appManage.getById(param.get("appId"));
                    singleapp.setAppName(param.get("appName"));
                    singleapp.setAppDescription(param.get("appDescription"));
                    singleapp.setAppUpdateTime(appUpdateTime);
                    appManage.update(singleapp);
                    result.put("msg","更新成功");
                }
                result.put("flag","true");
                return result;
            } else {//存在同名应用
                //创建新应用
                if (param.get("appId") == null || "".equals(param.get("appId"))){
                    msg="已存在相同应用!";
                    result.put("msg",msg);
                    result.put("flag","false");
                    return result;
                }

                //编辑
                AppInstance singleapp=appManage.getById(param.get("appId"));
                if(singleapp.getAppName().equals(param.get("appName")))
                {//更新操作
                    singleapp.setAppDescription(param.get("appDescription"));
                    singleapp.setAppUpdateTime(appUpdateTime);
                    appManage.update(singleapp);
                    result.put("msg","更新成功");
                    result.put("flag","true");
                    return result;
                }else{
                    msg="已存在此应用名，请重新编辑";
                    result.put("msg",msg);
                    result.put("flag","false");
                    return result;
                }


            }

        } catch (Exception e) {
            log.error("保存数据出错.", e);
            return result;
        }
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public boolean deleteAppById(@PathVariable("id") String appId) {
        try {
            appManage.delete(appId);
            return true;
        } catch (Exception e) {
            log.error("删除数据出错.", e);
            return false;
        }
    }
    //获取已授权Api列表
    @RequestMapping("/list/getAuthorizedApi/{id}")
    @ResponseBody
    public Map<String, Object> getAuthorizedApi(@PathVariable("id") String appId)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, String>> apiList = new ArrayList<Map<String, String>>();
        List<ServiceApply> lists = serviceApplyService.getAuthorizedApiListById(appId);
        for (ServiceApply list:lists){
            Map<String, String> map = new HashMap<String, String>();
            try{
                String service_id=list.getApi_service_id();
                ServiceDef sd= serviceDefService.getServiceDef(service_id);
                String groupId=sd.getApiGroup();
                DevGroup dg = devGroupService.getById(groupId);
                map.put("groupName",dg.getName());
            }catch (Exception e){
                log.error("Api未分组.", e);
                map.put("groupName","");
            }
            String eironment="";
            if (list.getAudit_status().equals("2")){
                eironment="上线";
            }else{
                eironment="未上线";
            }
            map.put("apiId", list.getApi_service_id());
            map.put("apiName", list.getName());
            map.put("authUser", list.getAuth_user());
            map.put("authTime",list.getAuth_time());
            map.put("eironment",eironment);
            apiList.add(map);
        }
        result.put("data", apiList);
        result.put("total", lists.size());

        return result;
    }

}
