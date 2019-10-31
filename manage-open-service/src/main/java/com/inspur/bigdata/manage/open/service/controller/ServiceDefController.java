package com.inspur.bigdata.manage.open.service.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.inspur.bigdata.manage.common.utils.PropertiesUtil;
import com.inspur.bigdata.manage.open.service.data.*;
import com.inspur.bigdata.manage.open.service.service.*;
import com.inspur.bigdata.manage.open.service.util.OpenServiceConstants;
import com.inspur.bigdata.manage.open.service.util.POIUtil;
import com.inspur.bigdata.manage.utils.HttpUtil;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import com.inspur.bigdata.manage.utils.StringUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import org.loushang.framework.util.HttpRequestUtils;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.inspur.bigdata.manage.open.service.util.OpenServiceConstants.*;

/**
 * Created by songlili on 2019/2/12.
 */
@CrossOrigin
@Controller
@RequestMapping("/open/api")
public class ServiceDefController {
    private static Log log = LogFactory.getLog(ServiceDefController.class);

    @Autowired
    private IServiceDefService serviceDefService;

    @Autowired
    private IServiceApplyService serviceApplyService;


    @Autowired
    private IServiceGroupService serviceGroupService;

    @Autowired
    private IDevGroupService devGroupService;

    @Autowired
    private IAppManage appManage;

    @Autowired
    private IServiceInputService serviceInputService;

    @Autowired
    private IServiceOutputService serviceOutputService;

    @Autowired
    ServiceApplyController serviceApplyController;

    @Autowired
    AppManageController appManageController;

    /**
     * 自动批量生成API文档
     * @param ids
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/generateAPIDocByAPIIds")
    @ResponseBody
    public void generateAPIDocByAPIIds(@RequestBody List<String> ids,HttpServletRequest request, HttpServletResponse response) throws Exception{

        com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(ids));

        // 查询API
        List<ServiceDef> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            ServiceDef serviceDef = serviceDefService.getServiceDef(jsonArray.getString(i));

            List<ServiceInput> inputList = serviceInputService.listByServiceId(serviceDef.getId());
            List<ServiceOutput> outputList = serviceOutputService.selectByApiId(serviceDef.getId());
            serviceDef.setInputList(inputList);
            serviceDef.setOutputList(outputList);
            list.add(serviceDef);
        }

        //构造数据
        Map<String, Object> dataMap = new HashMap<String,Object>();
        dataMap.put("apiList", list);
        dataMap.put("datetime",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dataMap.put("version","V1.0");
        dataMap.put("auth","system");

        // 生成文件到本地
//        Configuration configuration = new Configuration();
//        configuration.setDefaultEncoding("utf-8");
//        configuration.setDirectoryForTemplateLoading(new File("C:/"));
//        File outFile = new File("D:/test.doc");
//        Template t =  configuration.getTemplate("c.ftl","utf-8");
//        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"),10240);
//        t.process(dataMap, out);
//        out.close();

        // 生成下载文件
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setDirectoryForTemplateLoading(new File("C:/"));
        response.setContentType("application/msword");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + new String("c.doc".getBytes("GBK"), "iso8859-1") + "\"");
        response.setCharacterEncoding("utf-8");//此句非常关键,不然word文档全是乱码
        PrintWriter out = response.getWriter();
        Template t =  configuration.getTemplate("c.ftl","utf-8");//以utf-8的编码读取ftl文件
        t.process(dataMap, out);
        out.close();
    }

    /**
     * 批量导入API服务
     * @return
     */
    @PostMapping(value = "/import")
    @ResponseBody
    public Map<String, Object> importAPI(@RequestParam("excelFile") MultipartFile excelFile, HttpServletRequest request) throws IOException {
        List<String[]> excelData = POIUtil.readExcelFile(excelFile, 0);

        // 初始化参数
        ServiceDef serviceDef = new ServiceDef();
        List<ServiceInput> inputList = new ArrayList<>();
        List<ServiceOutput> outputList = new ArrayList<>();
        serviceDef.setInputList(inputList);
        serviceDef.setOutputList(outputList);

        // 开始遍历
        int id_count = 0;
        for (String[] arr : excelData) {
            System.out.println(arr);
            if (StringUtils.equals(arr[0],"") && StringUtils.equals(arr[1],"") && StringUtils.equals(arr[2],"")){
                continue;
            }
            if (StringUtils.equals(arr[0],"id")){
                id_count++;
                continue;
            }
            if (id_count == 1){ // 解析serviceDef
                try{
                    serviceDef.setName(arr[1]);
                    serviceDef.setDescription(arr[2]);
                    serviceDef.setAuditStatus("0");
                    serviceDef.setAuthType("0");
                    serviceDef.setProvider(arr[3]);
                    serviceDef.setProtocol(arr[4]);
                    serviceDef.setReqPath(arr[5]);
                    serviceDef.setHttpMethod(arr[6]);
                    serviceDef.setScProtocol(arr[7]);
                    serviceDef.setScHttpMethod(arr[8]);
                    serviceDef.setScAddr(arr[9]);
                    serviceDef.setSc_ws_function(arr[10]);
                    serviceDef.setScFrame(arr[11]);
                    serviceDef.setNameSpace(arr[12]);
                    serviceDef.setContentType(arr[13]);
                    serviceDef.setReturnSample(arr[14]);
                    serviceDef.setApiGroup(arr[15]);
                    serviceDef.setPrice(new BigDecimal(Double.parseDouble(arr[16])));
                    serviceDef.setPriceType(arr[17]);
                    serviceDef.setLimitCount(Double.parseDouble(arr[18]));
                    serviceDef.setEncryptionType(arr[19]);
                    serviceDef.setTopLimitCount(Integer.parseInt(arr[20]));
                    serviceDef.setTopLimitUnit(arr[21]);
                }catch (Exception e){
                    System.out.println("赋值serviceDef异常：" + e.toString());
                }
            }else if(id_count == 2){ // 解析 Input
                try{
                    ServiceInput serviceInput = new ServiceInput();
                    serviceInput.setType(arr[1]);
                    serviceInput.setRequired(Integer.parseInt(arr[2]));
                    serviceInput.setDescription(arr[3]);
                    serviceInput.setScName(arr[4]);
                    serviceInput.setName(arr[4]);
                    serviceInput.setScType(arr[5]);
                    serviceInput.setScRequired(Integer.parseInt(arr[6]));
                    serviceInput.setScDescription(arr[7]);
                    serviceInput.setScSeq(Integer.parseInt(arr[8]));
                    serviceInput.setScParamType(arr[9]);
                    serviceInput.setFixedValue(arr[10]);
                    serviceInput.setEncryptType(arr[11]);
                    serviceInput.setDecryptType(arr[12]);
                    serviceInput.setEncryptUrl(arr[13]);
                    serviceInput.setDecryptUrl(arr[14]);
                    inputList.add(serviceInput);
                }catch (Exception e){
                    System.out.println("赋值serviceInput异常："+e.toString());
                }
            }else if(id_count == 3){ // 解析 Output
                try{
                    ServiceOutput serviceOutput = new ServiceOutput();
                    serviceOutput.setName(arr[1]);
                    serviceOutput.setType(arr[2]);
                    serviceOutput.setDescription(arr[3]);
                    serviceOutput.setSeq(Integer.parseInt(arr[4]));
                    outputList.add(serviceOutput);
                }catch (Exception e){
                    System.out.println("赋值serviceOutput异常："+e.toString());
                }
            }
        }

        Map<String, Object> result = importAPI(serviceDef);
        System.out.println(JSON.toJSONString(result));
        return result;
    }

    /**
     * 批量申请api （省平台开放平台的定制化功能）
     * @param ids
     * @return
     */
    @RequestMapping(value = "/doApply")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject doApply(String[] ids){
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        // 根据userId查询所有APP
//        String userId = "uuuuuuuuser";
        String userId = OpenDataConstants.getUserId();
        if (userId ==null){
            result.put("result","please login first!");
            return result;
        }
        try{
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", userId);
            List<AppInstance> appInstances = appManage.getAppList(param);
            AppInstance appInstance = null;
            if (appInstances!=null){
                if (appInstances.size() == 0 ){
                    // 无APP，创建一个
                    Map<String,String> param_app = new HashMap<String,String>();
                    param_app.put("appName",userId+"-app");
                    param_app.put("appDescription","auto create by system.");
                    // 直接调用原来的创建app方法
                    appManageController.saveApp(param_app);
                    // 创建后再一次查询APP
                    appInstances = appManage.getAppList(param);
                    appInstance = appInstances.get(0);
                }else{
                    // 有app，直接获取
                    appInstance = appInstances.get(0);
                }
            }
            // 为当前批量申请创建一个批量id
            String batch_apply_id = UUIDGenerator.getUUID();
            // 遍历所有 API id
            for (int i = 0; i < ids.length; i++) {
                System.out.println(ids[i]);
                // 调用申请
                Map<String, String> parameters = new HashMap<>();
                parameters.put("openServiceId",ids[i]);
                parameters.put("applyFlag","0");
                parameters.put("userId",userId);
                parameters.put("appId",appInstance.getAppId());
                parameters.put("appName",appInstance.getAppName());
                // 直接调用原来的申请方法
                boolean flag = serviceApplyController.AppServiceApply(parameters,batch_apply_id);
            }
            result.put("result","success");

        }catch ( Exception e){
            System.out.println(e.toString());
            result.put("result","fail");
        }
        return result;
    }

    /**
     * 批量申请页面
     *
     * @return
     */
    @RequestMapping(value = "/getAPIListPublishedPage", method = RequestMethod.GET)
    public ModelAndView getAPIListPublishedPage() {
        Map<String, Object> model = new HashMap<String, Object>();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("auditStatus", OpenServiceConstants.api_audit_pass);
        List<ServiceDef> serviceDefs = serviceDefService.listServiceDefs(parameters);

        model.put("serviceDefs", serviceDefs);
        return new ModelAndView("service/service/publishedList", model);
    }

    /******************************************************************************************************
     * 市场列表页
     *
     * @return
     */
    @RequestMapping(value = "/getApplyPage", method = RequestMethod.GET)
    public ModelAndView getMarketPage() {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("parentId", -1);
        List<ServiceGroup> groupList = serviceGroupService.getGroupList(param);
        model.put("groupList", groupList);
        return new ModelAndView("service/service/marketList", model);
    }

    /**
     * 首页api列表页
     *
     * @return
     */
    @RequestMapping(value = "/getApiListPage", method = RequestMethod.GET)
    public ModelAndView getApiListPage() {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("parentId", -1);
        List<ServiceGroup> groupList = serviceGroupService.getGroupList(param);
        model.put("groupList", groupList);
        model.put("provider", OpenDataConstants.getUserId());
        return new ModelAndView("service/service/apiList", model);
    }

    /**
     * 查询市场列表数据
     *
     * @param parameters
     * @return
     */
    @RequestMapping(value = "/getApplyList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getMarketList(@RequestParam Map<String, Object> parameters) {
        Map<String, Object> mpMap = new HashMap<String, Object>();
        parameters.put("auditStatus", OpenServiceConstants.api_audit_pass);
        String groupId = parameters.get("groupId").toString();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("parentId", groupId);

        List<String> groupArr = new ArrayList<String>();
        groupArr.add(groupId);
        //获取当前节点的子组节点
        List<ServiceGroup> list = serviceGroupService.getGroupList(param);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                groupArr.add(list.get(i).getId());
            }
            parameters.put("groupArr", groupArr);
            parameters.put("groupId", "");
        } else {
            //当前没有子组,最后的组
            parameters.put("groupId", groupId);

        }

        List<ServiceDef> ServiceDefs = serviceDefService.listServiceDefs(parameters);
        if (StringUtil.isEmpty(ServiceDefs)) {
            mpMap.put("total", 0);
            mpMap.put("data", new ArrayList<ServiceDef>());
            return mpMap;
        }
        // 排序
        int total = PageUtil.getTotalCount();
        mpMap.put("total", total != -1 ? total : ServiceDefs.size());
        mpMap.put("data", ServiceDefs);

        return mpMap;
    }

    @RequestMapping(value = "/getAuditPage")
    public String getAuditPage() {
        return "service/audit/list";
    }

    @RequestMapping(value = "/listAudit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getApiAuditList(@RequestBody Map<String, String> parameters) {
        Map<String, Object> mpMap = new HashMap<String, Object>();
        String realm = OpenServiceConstants.getRealm();
        parameters.put("realm", realm);
        parameters.put("auditStatus", OpenServiceConstants.api_submit_audit);
        if (StringUtil.isNotEmpty(parameters.get("name"))) {
            parameters.put("name", parameters.get("name"));
        }
        List<ServiceDef> serviceDef = serviceDefService.listServiceDefs(parameters);
        if (StringUtil.isEmpty(serviceDef)) {
            mpMap.put("total", 0);
            mpMap.put("data", new ArrayList<ServiceDef>());
            return mpMap;
        }
        // 排序
        int total = PageUtil.getTotalCount();
        mpMap.put("total", total != -1 ? total : serviceDef.size());
        mpMap.put("data", serviceDef);
        return mpMap;
    }

    /**
     * 市场详情页
     *
     * @param id
     * @return
     */
    @RequestMapping("/get/{id}/apply")
    @ResponseBody
    public ModelAndView getInfoForApplyById(@PathVariable("id") String id) {
        String userId = OpenDataConstants.getUserId();
        if (StringUtil.isEmpty(userId)) {
            userId = "";
        }
        ServiceDef serviceDef = serviceDefService.getServiceDef(id);
        DevGroup devGroup = devGroupService.getById(serviceDef.getApiGroup());
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("apiServiceId", id);
        temp.put("userId", userId);
        temp.put("auth_status", OpenDataConstants.auth_status_pass);
        List<ServiceApply> serviceApplies = serviceApplyService.isApplyAuthToUser(temp);
        if (serviceApplies == null || serviceApplies.isEmpty()) {
            serviceDef.setOpenAddr(serviceDef.getReqPath());
        } else {
            serviceDef.setOpenAddr(OpenServiceConstants.getOpenAddr(devGroup.getContext(), serviceDef.getReqPath()));
        }
        serviceDef.setScAddr("");
        Map<String, Object> param = new HashMap<String, Object>();
        List<ServiceInput> inputParam = serviceInputService.listByServiceId(id);
        List<ServiceOutput> outputList = serviceOutputService.selectByApiId(id);
        param.put("serviceInfo", serviceDef);
        param.put("inputParam", inputParam);
        param.put("outputParam", outputList);
        param.put("apply", true);
        return new ModelAndView("service/service/api_market_info", param);
    }

    /**
     * 市场详情页 返回数据
     *
     * @param id
     * @return
     */
    @RequestMapping("/get/{id}/info")
    @ResponseBody
    public Map<String, Object> getInfoForApplyById2(@PathVariable("id") String id) {
        ServiceDef serviceDef = serviceDefService.getServiceDef(id);
        DevGroup devGroup = devGroupService.getById(serviceDef.getApiGroup());
//        serviceDef.setOpenAddr(OpenServiceConstants.getOpenAddr(devGroup.getContext(),serviceDef.getReqPath()));
        serviceDef.setOpenAddr(serviceDef.getReqPath());
        serviceDef.setScAddr("");
        Map<String, Object> param = new HashMap<String, Object>();
        List<ServiceInput> inputParam = serviceInputService.listByServiceId(id);
        List<ServiceOutput> outputList = serviceOutputService.selectByApiId(id);
        param.put("serviceInfo", serviceDef);
        param.put("inputParam", inputParam);
        param.put("outputParam", outputList);
        param.put("apply", true);
        return param;
    }

    /**
     * 查看Api详情
     *
     * @return
     */
    @RequestMapping("/getInfo/{id}")
    @ResponseBody
    public ModelAndView getInfoById(@PathVariable("id") String apiId) {

        Map<String, Object> model = new HashMap<String, Object>();
        ServiceDef serviceDef = serviceDefService.getServiceDef(apiId);
        List<ServiceInput> inputParam = serviceInputService.listByServiceId(apiId);

        try {
            DevGroup group = devGroupService.getById(serviceDef.getApiGroup());
            model.put("groupName", group.getName());
        } catch (Exception e) {
            log.error("Api未分组.", e);
            model.put("groupName", "");
        }
        boolean canViewBackEnd = false;
        if (OpenServiceConstants.getUserId().equals(serviceDef.getProvider()) || OpenServiceConstants.isSuperAdmin(OpenServiceConstants.getRealm())) {
            canViewBackEnd = true;
        }
        model.put("canViewBackEnd", canViewBackEnd);
        model.put("serviceDef", serviceDef);
        model.put("inputParam", inputParam);
        return new ModelAndView("service/service/api_info", model);

    }


    /**
     * 根据用户ID获取APP列表
     *
     * @return
     */
    @RequestMapping(value = "/getAppByUserId", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getAppByUserId(@RequestBody Map<String, String> parameters) {
        Map<String, Object> mpMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        String userId = OpenServiceConstants.getUserId();
        String api_service_id = parameters.get("openServiceId");
        String apply_flag = parameters.get("applyFlag");
        if (StringUtil.isNotEmpty(parameters.get("appName"))) {
            data.put("appName", parameters.get("appName"));
        }
        if ("0".equals(apply_flag)) {//0:市场申请(只查询当前用户的APP),1:授权(可查平台上所有的APP)
            data.put("userId", userId);
        }

        List<AppInstance> appList = appManage.getappListByUserId(data);
        data.put("api_service_id", api_service_id);
        List<AppInstance> appApplyList = appManage.getAppStatusByUserId(data);

        boolean flag = false;
        for (int i = 0; i < appList.size(); i++) {
            AppInstance app = appList.get(i);
            String app_id = app.getAppId();
            flag = getApplyForFlag(appApplyList, app_id);
            app.setApplyFor(flag);

        }
        int total = PageUtil.getTotalCount();
        mpMap.put("total", total != -1 ? total : appList.size());
        mpMap.put("data", appList);
        return mpMap;
    }


    /**
     * 某个用户针对某个APP的申请状态
     *
     * @return
     */
    boolean getApplyForFlag(List<AppInstance> list, String appId) {
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            String app_id = list.get(i).getAppId();
            String auth_status = list.get(i).getAuth_status();
            if (appId.equals(app_id)) {
                flag = !"2".equals(auth_status);

                return flag;
            }
        }
        return flag;
    }

    @RequestMapping("/releaseApi")
    @ResponseBody
    public boolean doRelease(@RequestParam Map<String, String> parameters) {
        ServiceDef serviceDef = serviceDefService.getServiceDef(parameters.get("id"));
//        if (serviceDef.getProvider().equals(OpenServiceConstants.getUserId())) {
            serviceDef.setId(parameters.get("id"));

            if (parameters.get("subgroupId") == null) {
                serviceDef.setGroupId(parameters.get("groupId"));
            } else {
                serviceDef.setGroupId(parameters.get("subgroupId"));
            }

            serviceDef.setAuditStatus(OpenServiceConstants.api_submit_audit);
            serviceDef.setOnlineTime(OpenServiceConstants.sf.format(new Date()));
            serviceDef.setUpdateTime(OpenServiceConstants.sf.format(new Date()));
            try {
                serviceDefService.update(serviceDef);
            } catch (Exception e) {
                log.error("api发布失败", e);
                return false;
            }
            return true;
//        } else {
//            log.error("api发布者和提供者不一致！");
//            return false;
//        }

    }

    //下线
    @RequestMapping("/offline/{id}")
    @ResponseBody
    public boolean doapiOffline(@PathVariable("id") String id) {
        ServiceDef serviceDef = serviceDefService.getServiceDef(id);
        serviceDef.setId(id);
        serviceDef.setAuditStatus(OpenServiceConstants.api_offline);
        serviceDef.setUpdateTime(OpenServiceConstants.sf.format(new Date()));
        try {
            serviceDefService.update(serviceDef);
        } catch (Exception e) {
            log.error("api下线失败", e);
            return false;
        }
        return true;
    }

    /**
     * 发布审核通过
     *
     * @param id
     * @return
     */
    @RequestMapping("/pass/{id}")
    @ResponseBody
    public boolean doOnline(@PathVariable("id") String id) {
        ServiceDef serviceDef = serviceDefService.getServiceDef(id);
        serviceDef.setId(id);
        serviceDef.setAuditStatus(OpenServiceConstants.api_audit_pass);
        String userId = OpenServiceConstants.getUserId();
        serviceDef.setAuditUser(userId);
        serviceDef.setOnlineTime(OpenServiceConstants.sf.format(new Date()));
        serviceDef.setUpdateTime(OpenServiceConstants.sf.format(new Date()));
        try {
            serviceDefService.update(serviceDef);
            //上线服务 调用服务网关
            JSONObject jo = new JSONObject();
            jo.put("group", serviceDef.getApiGroup());
            jo.put("limitCount", serviceDef.getLimitCount());
            jo.put("reqPath", serviceDef.getReqPath());
            jo.put("serviceId", serviceDef.getId());
            jo.put("url", serviceDef.getScAddr());
            List<Object> list = new ArrayList<>();
            list.add(jo);
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", OpenServiceConstants.content_type_json);
            HttpUtil.execPost(PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "service_gateway_register_url"), headers, list.toString());
        } catch (Exception e) {
            log.error("api审核失败", e);
            return false;
        }
        return true;
    }

    /**
     * 发布审核驳回
     *
     * @param id
     * @return
     */
    @RequestMapping("/reject/{id}")
    @ResponseBody
    public boolean doOffline(@PathVariable("id") String id) {
        ServiceDef serviceDef = serviceDefService.getServiceDef(id);
        serviceDef.setId(id);
        serviceDef.setAuditStatus(OpenServiceConstants.api_audit_reject);
        String userId = OpenServiceConstants.getUserId();
        serviceDef.setAuditUser(userId);
        serviceDef.setUpdateTime(OpenServiceConstants.sf.format(new Date()));
        try {
            serviceDefService.update(serviceDef);
        } catch (Exception e) {
            log.error("api驳回失败", e);
            return false;
        }
        return true;
    }

    @RequestMapping("/test")
    public String toTest() {
        return "service/service/servicetest";
    }

    /**
     * 跳转API列表主页面
     *
     * @return
     */
    @RequestMapping(value = "/getApiPage")
    public String getAppPage() {
        return "service/service/list";
    }

    /**
     * 查询api列表
     *
     * @param parameters
     * @return
     */
    @RequestMapping(value = "/getApiList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getApiList(@RequestBody Map<String, String> parameters) {
        Map<String, Object> mpMap = new HashMap<String, Object>();
        parameters.put("provider", OpenServiceConstants.getUserId());
        if (StringUtil.isNotEmpty(parameters.get("name"))) {
            parameters.put("name", parameters.get("name"));
        }
        if (StringUtil.isNotEmpty(parameters.get("auditStatus"))) {
            parameters.put("auditStatus", parameters.get("auditStatus"));
        }
        List<ServiceDef> ServiceDefs = serviceDefService.listServiceDefs(parameters);
        if (StringUtil.isEmpty(ServiceDefs)) {
            mpMap.put("total", 0);
            mpMap.put("data", new ArrayList<ServiceDef>());
            return mpMap;
        }
        // 排序
        int total = PageUtil.getTotalCount();
        mpMap.put("total", total != -1 ? total : ServiceDefs.size());
        mpMap.put("data", ServiceDefs);

        return mpMap;
    }

    /**
     * 跳转创建页面
     *
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public ModelAndView create(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("edit", false);
        model.put("serviceDef", new ServiceDef());
        return new ModelAndView("service/service/register", model);
    }

    /**
     * 跳转编辑页面
     *
     * @return
     */
    @RequestMapping("/toUpdate/{id}")
    @ResponseBody
    public ModelAndView toUpdate(HttpServletRequest request, @PathVariable("id") String id) {
        Map<String, Object> model = new HashMap<String, Object>();
        ServiceDef serviceDef = serviceDefService.getServiceDef(id);
        model.put("edit", true);
        model.put("serviceDef", serviceDef);
        serviceDef.setInputList(serviceInputService.listByServiceId(serviceDef.getId()));
        return new ModelAndView("service/service/register", model);
    }

    /**
     * 保存数据
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(final HttpServletRequest httpServletRequest, @RequestBody ServiceDef serviceDef) {
        Map<String, Object> map = new HashMap<>();
        if (null == serviceDef) {
            map.put("result", false);
            map.put("message", "数据不存在");
            return map;
        }
//        if (StringUtil.isEmpty(serviceDef.getEncryptionType())) {
//            map.put("result", false);
//            map.put("message", "加密方式为空");
//            return map;
//        }
//        if (StringUtil.isEmpty(ENCRYPTION_MAP.get(serviceDef.getEncryptionType()))) {
//            map.put("result", false);
//            map.put("message", "加密方式不正确");
//            return map;
//        }
//        if (serviceDef.getLimitCount() == null) {
//            map.put("result", false);
//            map.put("message", "API限流次数为空");
//            return map;
//        }
//        try {
//            if (new BigDecimal(serviceDef.getLimitCount()).compareTo(BigDecimal.ZERO) < 1) {
//                map.put("result", false);
//                map.put("message", "API限流次数数值不能小于等于0");
//                return map;
//            }
//        } catch (Exception e) {
//            log.warn(e.getMessage());
//            map.put("result", false);
//            map.put("message", "API限流次数数值不正确");
//            return map;
//        }
//        if (StringUtil.isEmpty(TOP_LIMIT_UNIT_MAP.get(serviceDef.getTopLimitUnit()))) {
//            map.put("result", false);
//            map.put("message", "时间粒度不正确");
//            return map;
//        }
//        if (serviceDef.getTopLimitCount() <= 0) {
//            map.put("result", false);
//            map.put("message", "API限流次数数值不能小于等于0");
//            return map;
//        }

        if (StringUtil.isEmpty(serviceDef.getName())) {
            map.put("result", false);
            map.put("message", "名称为空");
            return map;
        }
//        if(StringUtil.isEmpty(serviceDef.getRemoteId())){
//            map.put("result",false);
//            map.put("message","后端服务为空");
//            return map;
//        }
        if (!StringUtil.isEmpty(serviceDef.getApiGroup())) {
            DevGroup group = devGroupService.getById(serviceDef.getApiGroup());
            if (group == null) {
                map.put("result", false);
                map.put("message", "分组不存在");
                return map;
            }
        } else {
            map.put("result", false);
            map.put("message", "数据分组不存在");
            return map;
        }
        if (!StringUtil.isEmpty(serviceDef.getId())) {
            return update(serviceDef);
        }
        serviceDef.setId(UUIDGenerator.getUUID());
        serviceDef.setProvider(OpenServiceConstants.getUserId());
        serviceDef.setAuditStatus(OpenServiceConstants.api_create);
        serviceDef.setCreateTime(OpenServiceConstants.sf.format(new Date()));
        try {
            serviceDefService.addServiceDef(serviceDef);
            map.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    public Map<String, Object> importAPI(ServiceDef serviceDef){
        Map<String, Object> map = new HashMap<>();
        if (!StringUtil.isEmpty(serviceDef.getId())) {
            return update(serviceDef);
        }
        serviceDef.setId(UUIDGenerator.getUUID());
        serviceDef.setCreateTime(OpenServiceConstants.sf.format(new Date()));
        try {
            serviceDefService.addServiceDef(serviceDef);
            map.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 服务更新
     *
     * @param serviceDef
     * @return
     */
    public Map<String, Object> update(ServiceDef serviceDef) {
        Map<String, Object> map = new HashMap<>();
        ServiceDef info = serviceDefService.getServiceDef(serviceDef.getId());
        if (null == info) {
            map.put("result", false);
            map.put("message", "数据不存在");
            return map;
        }
        if (info.getAuditStatus().equals(OpenServiceConstants.api_audit_pass) ||
                info.getAuditStatus().equals(OpenServiceConstants.api_submit_audit)) {
            map.put("result", false);
            map.put("message", "数据状态不允许更新");
            return map;
        }
        List<ServiceInput> list = serviceDef.getInputList();
        String remoteId = serviceDef.getRemoteId();
        BeanUtils.copyProperties(serviceDef, info, getNullPropertyNames(serviceDef));
        //修改之前调用的远程，修改成手动需要清空remoteId
        if (remoteId == null || remoteId.trim().length() == 0) {
            info.setRemoteId(null);
        }
        info.setUpdateTime(OpenServiceConstants.sf.format(new Date()));
        info.setInputList(list);
        try {
            serviceDefService.updateServiceDef(info);
            map.put("result", true);
            try {
                //调用服务网关
                JSONObject jo = new JSONObject();
                jo.put("group", info.getApiGroup());
                jo.put("limitCount", info.getLimitCount());
                jo.put("reqPath", info.getReqPath());
                jo.put("serviceId", info.getId());
                jo.put("url", info.getScAddr());
                List<Object> listTemp = new ArrayList<>();
                listTemp.add(jo);
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", OpenServiceConstants.content_type_json);
                HttpUtil.execPost(PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "service_gateway_update_limit_count_url"), headers, listTemp.toString());
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 调用接口获取api列表
     *
     * @return
     */
    @RequestMapping("/apiList")
    @ResponseBody
    public Map<String, Object> getApiList() {
        String userId = OpenServiceConstants.getUserId();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", OpenServiceConstants.getRemoteApiList(userId));
        return result;
    }

    /**
     * 根据id获取api详情
     *
     * @return
     */
    @RequestMapping("/apiDetail/{id}")
    @ResponseBody
    public JSONObject getApiDetail(@PathVariable("id") String id) {
        return OpenServiceConstants.getRemoteApiDetail(id);
    }

    /**
     * 跳转远程数据服务选择页面
     *
     * @return
     */
    @RequestMapping(value = "/remotePage", method = RequestMethod.GET)
    public ModelAndView remotePage() {
        Map<String, Object> model = new HashMap<String, Object>();
        return new ModelAndView("service/service/remoteList", model);
    }

    /**
     * 远程数据服务选择页面查询列表 过滤掉使用过的远程api
     *
     * @param parameters
     * @return
     */
    @RequestMapping(value = "/remoteList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> remoteList(@RequestBody Map<String, String> parameters) {
        String serviceName = parameters.get("serviceName");
        //解析数据列表
        String userId = OpenServiceConstants.getUserId();
        JSONArray remoteList = OpenServiceConstants.getRemoteApiList(userId);
        //查询发布
        Map<String, Object> mpMap = new HashMap<String, Object>();
        mpMap.put("provider", userId);
        List<ServiceDef> localList = serviceDefService.listServiceDefs(mpMap);
        //去重分页
        List<JSONObject> newList = new ArrayList<>();
        for (int i = 0; i < remoteList.size(); i++) {
            JSONObject job = remoteList.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            boolean b = localList.stream().anyMatch(u ->
                    !StringUtil.isEmpty(u.getRemoteId()) && u.getRemoteId().equals(job.getString("serviceId")
                    )
            );
            if (!b) {
                if (!StringUtil.isEmpty(serviceName)) {
                    if (job.getString("serviceName").toLowerCase().contains(serviceName.toLowerCase())) {
                        newList.add(job);
                    }
                } else {
                    newList.add(job);
                }
            }
        }
        int start = Integer.parseInt(parameters.get("start"));
        int limit = Integer.parseInt(parameters.get("limit"));
        int total = newList.size();
        List<JSONObject> dataList = new ArrayList<>();
        int toIndex = start + limit;
        if (toIndex > total) {
            toIndex = total;
        }
        if (start <= total) {
            for (int i = start; i < toIndex; i++) {
                dataList.add(newList.get(i));
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("data", dataList);
        return result;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 删除api
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public boolean deleteApiServiceById(@PathVariable("id") String id) {
        try {
            serviceDefService.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("删除api出错.", e);
            return false;
        }
    }

    /**
     * 根据id获取入参
     *
     * @return
     */
    @RequestMapping("/getInParam/{id}")
    @ResponseBody
    public Map<String, Object> getInParam(@PathVariable("id") String id) {
        List<ServiceInput> inParam = serviceInputService.listByServiceId(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", inParam);
        result.put("total", inParam.size());

        return result;

    }

    /**
     * 验证api分组中reqPath是否唯一
     *
     * @param apiGroupId
     * @param reqPath
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/{apiGroupId}/{reqPath}")
    @ResponseBody
    public boolean check(@PathVariable("apiGroupId") String apiGroupId, @PathVariable("reqPath") String reqPath,
                         HttpServletRequest request, HttpServletResponse response) {
        //根据apiGroupId和reqPath查询唯一的service
        Map<String, Object> map = new HashMap<>();
        map.put("apiGroup", apiGroupId);
        map.put("reqPath", "/" + reqPath);
        List<ServiceDef> defs = serviceDefService.getByApiGroupAndPath(map);
        return defs == null || defs.size() == 0;
    }

    /**
     * api发布列表页
     *
     * @return
     */
    @RequestMapping(value = "/getOpenApiPage")
    public String getOpenApiPage() {
        return "service/service/api_open_list";
    }

    /**
     * 跳转API发布页面
     *
     * @return
     */
    @RequestMapping("/release")
    @ResponseBody
    public ModelAndView release(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("parentId", -1);
        List<ServiceGroup> groupList = serviceGroupService.getGroupList(param);

        param = new HashMap<String, Object>();
        param.put("provider", OpenServiceConstants.getUserId());
        List<ServiceDef> ServiceDefs = serviceDefService.listServiceDefs(param);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("groupList", groupList);
        model.put("edit", false);
        model.put("serviceDef", ServiceDefs);
        return new ModelAndView("service/service/api_release", param);
    }


    @RequestMapping(value = "/getApiListByUserId")
    @ResponseBody
    public Map<String, Object> getApiListByUserId() {
        Map<String, Object> mpMap = new HashMap<String, Object>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("provider", OpenServiceConstants.getUserId());
        List<String> status = new ArrayList<String>();
        status.add("0");
        status.add("3");
        status.add("4");
        parameters.put("auditStatus", status);
        List<ServiceDef> ServiceDefs = serviceDefService.listAPIByProvider(parameters);
        if (StringUtil.isEmpty(ServiceDefs)) {
            mpMap.put("total", 0);
            mpMap.put("data", new ArrayList<ServiceDef>());
            return mpMap;
        }
        // 排序
        int total = PageUtil.getTotalCount();
        mpMap.put("total", total != -1 ? total : ServiceDefs.size());
        mpMap.put("data", ServiceDefs);

        return mpMap;
    }

    /**
     * 主动授权查询数据
     *
     * @return
     */
    @RequestMapping(value = "/getAppByAuth", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getAppByAuth(@RequestBody Map<String, String> parameters) {
        Map<String, Object> mpMap = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        String limit = parameters.get("limit");
        String start = parameters.get("start");
        String api_service_id = parameters.get("openServiceId");
        String apply_flag = parameters.get("applyFlag");
        String appKey = parameters.get("appKey");
        String user = parameters.get("user");
        String selectType = parameters.get("selectType");
        if (StringUtil.isEmpty(selectType)) {
            mpMap.put("total", 0);
            mpMap.put("data", null);
            return mpMap;
        }
        data.put("limit", limit);
        data.put("start", start);
        if ("0".equals(selectType)) {//0:我的应用，1、根据应用ID查 2、根据平台用户查)
            String userId = OpenServiceConstants.getUserId();
            data.put("userId", userId);
            if (StringUtil.isNotEmpty(parameters.get("appName"))) {
                data.put("appName", parameters.get("appName"));
            }
        } else if ("1".equals(selectType)) {
            if (StringUtil.isEmpty(appKey)) {
                mpMap.put("total", 0);
                mpMap.put("data", null);
                return mpMap;
            }
            data.put("appKey", appKey);
        } else if ("2".equals(selectType)) {
            if (StringUtil.isEmpty(user)) {
                mpMap.put("total", 0);
                mpMap.put("data", null);
                return mpMap;
            }
            data.put("userId", user + "-" + OpenServiceConstants.getRealm());
        } else {
            mpMap.put("total", 0);
            mpMap.put("data", null);
            return mpMap;
        }

        List<AppInstance> appList = appManage.getappListByUserId(data);
        data.put("api_service_id", api_service_id);
        List<AppInstance> appApplyList = appManage.getAppStatusByUserId(data);

        boolean flag = false;
        for (int i = 0; i < appList.size(); i++) {
            AppInstance app = appList.get(i);
            String app_id = app.getAppId();
            flag = getApplyForFlag(appApplyList, app_id);
            app.setApplyFor(flag);
            app.setAppSecret(null);
            app.setAppKey(null);
        }
        int total = PageUtil.getTotalCount();
        mpMap.put("total", total != -1 ? total : appList.size());
        mpMap.put("data", appList);
        return mpMap;
    }

    @RequestMapping(value = "/getAPIOnlinePage")
    public String getAPIOnlinePage() {
        return "service/service/api_manage";
    }

    @RequestMapping(value = "/listOnlineAPI", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getApiOnLineList(@RequestBody Map<String, String> parameters) {
        Map<String, Object> mpMap = new HashMap<String, Object>();
        parameters.put("auditStatus", OpenServiceConstants.api_audit_pass);
        if (StringUtil.isNotEmpty(parameters.get("name"))) {
            parameters.put("name", parameters.get("name"));
        }
        List<ServiceDef> serviceDef = serviceDefService.listServiceDefs(parameters);
        if (StringUtil.isEmpty(serviceDef)) {
            mpMap.put("total", 0);
            mpMap.put("data", new ArrayList<ServiceDef>());
            return mpMap;
        }
        // 排序
        int total = PageUtil.getTotalCount();
        mpMap.put("total", total != -1 ? total : serviceDef.size());
        mpMap.put("data", serviceDef);
        return mpMap;
    }

    /**
     * 根据数据服务id查询有无开放服务
     *
     * @param remoteId
     * @return
     */
    @RequestMapping(value = "/queryServiceByRemoteId")
    @ResponseBody
    public String queryByRemoteId(@RequestParam("remoteId") String remoteId) {
        if (serviceDefService.queryByRemoteId(remoteId).size() > 0) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "/encryptionList")
    @ResponseBody
    public Map<String, Object> getEncryptionTypeList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : ENCRYPTION_MAP.entrySet()) {
            //暂时只支持BASE64和不加密
            if (ENCRYPT_MODE_KEY_BASE64.equals(entry.getKey())
                    || ENCRYPT_MODE_NO.equals(entry.getKey())) {
                Map<String, String> temp = new HashMap<>();
                temp.put("id", entry.getKey());
                temp.put("name", entry.getValue());
                list.add(temp);
            }
//            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        return result;
    }

    @RequestMapping(value = "/topLimitUnitList")
    @ResponseBody
    public Map<String, Object> getTopLimitUnitList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : TOP_LIMIT_UNIT_MAP.entrySet()) {
            Map<String, String> temp = new HashMap<>();
            temp.put("id", entry.getKey());
            temp.put("name", entry.getValue());
            list.add(temp);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        return result;
    }
}
