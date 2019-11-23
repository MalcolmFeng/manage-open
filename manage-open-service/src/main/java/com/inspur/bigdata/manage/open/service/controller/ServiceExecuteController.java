package com.inspur.bigdata.manage.open.service.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.inspur.bigdata.manage.open.service.data.*;
import com.inspur.bigdata.manage.open.service.pay.data.PayAccountCapital;
import com.inspur.bigdata.manage.open.service.pay.service.IPayService;
import com.inspur.bigdata.manage.open.service.service.*;
import com.inspur.bigdata.manage.open.service.util.apimonitor.ApiServiceMonitorUtil;
import com.inspur.bigdata.manage.open.service.util.OpenServiceConstants;
import com.inspur.bigdata.manage.open.service.util.sign.*;
import com.inspur.bigdata.manage.open.service.util.signconstants.Constants;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import com.inspur.bigdata.manage.utils.SM3;
import com.inspur.bigdata.manage.utils.StringUtil;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;
import net.sf.json.JSONObject;
import org.apache.axis.AxisEngine;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.loushang.framework.util.DateUtil;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.inspur.bigdata.manage.open.service.util.OpenServiceConstants.*;
import static com.inspur.bigdata.manage.open.service.util.apimonitor.ApiServiceMonitorThread.getNcpu;
import static com.inspur.bigdata.manage.utils.EncryptionUtil.*;


import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;

import org.apache.axis2.client.ServiceClient;
import org.w3c.dom.svg.SVGAltGlyphDefElement;


@CrossOrigin
@Controller
@RequestMapping("/api/execute")
public class ServiceExecuteController {
    private static Log log = LogFactory.getLog(ServiceExecuteController.class);
    @Autowired
    private IServiceDefService serviceDefService;
    @Autowired
    private IAppManage appManage;
    @Autowired
    private IServiceApplyService serviceApplyService;
    @Autowired
    private IServiceInputService serviceInputService;

    @Autowired
    private IDevGroupService devGroupService;
    @Autowired
    private IPayService payService;
    @Autowired
    private IServiceMonitorService monitorService;
    @Autowired
    private IServiceIpListService serviceIpListService;

    // 默认限流器大小
    public static Double default_limitCount = 60.0;

    // 每次路由的地址，创建的限流器
    public static Map<String, RateLimiter> map = new ConcurrentHashMap();

    private static final int Ncpu = getNcpu();

    ExecutorService monitorExecutorService = Executors.newFixedThreadPool(Ncpu * 2);

    @RequestMapping("/test/{apiServiceId}")
    public ModelAndView toTest(@PathVariable("apiServiceId") String apiServiceId) {
        ServiceDef serviceDef = serviceDefService.getServiceDef(apiServiceId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("serviceDef", serviceDef);
        List<ServiceApply> applylist = serviceApplyService.getByServiceId(apiServiceId);
        model.put("applylist", applylist);
        //获取APP绑定信息
        String userId = OpenDataConstants.getUserId();
        if (StringUtil.isNotEmpty(userId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("apiServiceId", apiServiceId);
            map.put("userId", userId);
            map.put("auth_status", OpenDataConstants.auth_status_pass);
            List<ServiceApply> list = serviceApplyService.isApplyAuthToUser(map);
            if (!list.isEmpty()) {
                AppInstance appInfo = appManage.getById(list.get(0).getApp_id());
                model.put("data", appInfo);
            }
        }
        return new ModelAndView("service/service/servicetest", model);
    }

    @RequestMapping("/getAppList/{apiServiceId}")
    @ResponseBody
    public Map<String, Object> toGetAppList(@PathVariable("apiServiceId") String apiServiceId) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<AppInstance> applist = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", OpenDataConstants.getUserId());
        applist = appManage.getappListByUserId(map);
        result.put("data", applist);
        return result;
    }

    @RequestMapping("/getAppInfo/{appId}")
    @ResponseBody
    public Map<String, Object> toGetAppInfo(@PathVariable("appId") String appId) {
        Map<String, Object> result = new HashMap<String, Object>();
        AppInstance appInfo = appManage.getById(appId);
        result.put("data", appInfo);
        return result;
    }

    @RequestMapping("/getserviceinput/{apiServiceId}")
    @ResponseBody
    public Map<String, Object> getServiceInput(@PathVariable("apiServiceId") String apiServiceId) {
        List<ServiceInput> listServiceInput = serviceInputService.listByServiceId(apiServiceId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", listServiceInput);
        return result;
    }

    @RequestMapping("/apitest/{apiServiceId}")
    @ResponseBody
    public Map<String, Object> apiTest(@PathVariable("apiServiceId") String apiServiceId, HttpServletRequest request, HttpServletResponse response) {
        //判断是否有权限测试
        Map<String, Object> map = new HashMap<>();
        String userId = OpenDataConstants.getUserId();
        if (StringUtil.isNotEmpty(userId)) {
            Map<String, Object> paramap = new HashMap<>();
            paramap.put("apiServiceId", apiServiceId);
            paramap.put("userId", userId);
            paramap.put("auth_status", OpenDataConstants.auth_status_pass);
            List<ServiceApply> list = serviceApplyService.isApplyAuthToUser(paramap);
            if (list.isEmpty()) {
                map.put("result", false);
                map.put("info", "没有访问该服务的权限！");
                return map;
            }
        }

        //测试
        List<ServiceInput> listServiceInput = serviceInputService.listByServiceId(apiServiceId);
        String datalist = request.getParameter("paramlist");
        JSONObject json = JSONObject.fromObject(datalist);
        for (ServiceInput serviceInput : listServiceInput) {
            if (StringUtils.isNotEmpty(serviceInput.getFixedValue())) {
                //有设置固定值
                serviceInput.setValue(serviceInput.getFixedValue());
                continue;
            }
            for (Object key : json.keySet()) {
                if (serviceInput.getName().equals(String.valueOf(key))) {
                    String value = URLDecoder.decode(String.valueOf(json.get(key)));
                    if (StringUtil.isNotEmpty(value)) serviceInput.setValue(value);
                    break;
                }
            }
        }
        Collections.sort(listServiceInput);
        long startTime = System.currentTimeMillis();
        boolean success = true;
        //服务类型
        ServiceDef serviceDef = serviceDefService.getServiceDef(apiServiceId);
        try {
            //获取POST还是Get请求
            if (null == serviceDef) {
                map.put("result", false);
                map.put("info", "未查找到服务信息！");
                return map;
            }
            //后台请求

            String result_str = "";
            if (serviceDef.getScProtocol().equals("webService")) {
                String type = serviceDef.getScFrame();
                if ("Axiom".equals(type)) {
                    result_str = executeAxis2(success, serviceDef, listServiceInput, new ApiServiceMonitor());
                } else if ("RPC".equals(type)) {
//                    result_str = executeRPC(success, serviceDef, listServiceInput);
//                    result_str = executeRPCAxis(success, serviceDef, listServiceInput, new ApiServiceMonitor());
                    result_str = executeRPCAxisClient(success, serviceDef, listServiceInput, new ApiServiceMonitor());
                }
            } else {
                result_str = doRequest(response, success,"", serviceDef, listServiceInput, new ApiServiceMonitor());
            }

//            String result_str = doRequest("", serviceDef, listServiceInput, new ApiServiceMonitor());
            ///发送请求
            map.put("result", true);
            map.put("header", "Content-Type:" + OpenServiceConstants.getContentType(serviceDef.getContentType()));
            map.put("info", result_str);
            return map;
        } catch (Throwable e) {
            success = false;
            log.error("目标服务调用出错", e);
            Map<String, Object> errorResult = new HashMap<String, Object>();
            errorResult.put("error", "执行服务出错");
            errorResult.put("error_description", e.getMessage());
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", false);
            result.put("info", errorResult);
            // 未知错误编码
            return result;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("调用api执行的时间" + (System.currentTimeMillis() - startTime) + "毫秒");
            }
        }
    }

    @RequestMapping("/do/{apiContext}/{reqPath}")
    @ResponseBody
    public void execute(@PathVariable("apiContext") String apiContext, @PathVariable("reqPath") String reqPath,
                        HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = null;
        boolean success = false;
        long startTime = 0;
        String responseTime = null;
        String requestUserId = null;
        BigDecimal servicePrice = null;
        String instream = null;
        String context_path = "/" + apiContext + "/" + reqPath;
        ApiServiceMonitor apiServiceMonitor = new ApiServiceMonitor();
        String logId = UUIDGenerator.getUUID();
        apiServiceMonitor.setId(logId);
        try {
            /**获取请求者IP*/
            String requestIp = ApiServiceMonitorUtil.getClientIp(request);
            String requestTime = DateUtil.getCurrentTime2();

            apiServiceMonitor.setCallerIp(requestIp);
            apiServiceMonitor.setRequestTime(requestTime);
            apiServiceMonitor.setOpenServiceInput(JSONObject.fromObject(request.getParameterMap()).toString());
            apiServiceMonitor.setOpenServiceMethod(request.getMethod());
            apiServiceMonitor.setOpenServiceRequestURL(request.getRequestURL().toString());

            //  ---------------- IP黑名单 start----------------
            log.info("开始查询数据库黑名单和白名单");
            Map<String,Object> ipParams = new HashMap<>();
            ipParams.put("ipV4",requestIp);
            ipParams.put("active","true");
            ipParams.put("type","white");
            List<IpList> ips_white = serviceIpListService.getIpList(ipParams);  // 查白名单

            ipParams.put("type","black");
            List<IpList> ips_black = serviceIpListService.getIpList(ipParams); // 查黑名单

            // 存在于黑名单但不存在于白名单，拒绝访问
            if (ips_black.size()>0 && ips_white.size() ==0){
                writer = response.getWriter();
                writer.print("IP地址被禁用");
                writer.flush();
                apiServiceMonitor.setNotes("IP地址被禁用");
                apiServiceMonitor.setResult(ASM_ERROR_IP_REFUSE);
                return;
            }
            //  ---------------- IP黑名单 end ----------------


            // ---------------- 查询APP start ----------------
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Type", OpenServiceConstants.content_type_html);
            //组装头部并获取 appkey、appSecret 和请求签名signature
            Enumeration<String> headNames = request.getHeaderNames();
            Map<String, String> headers = new HashMap<>();
            String appkey = null;
            String appSecret = null;
            String signature = null;

            // 1.先获取参数中的密钥（page转发时）
            appkey = request.getParameter("xCaKey");
            signature = request.getParameter("xCaSignature");

            // 2.api服务时获取请求头中的密钥
            while (headNames.hasMoreElements()) {
                String headName = headNames.nextElement();
                headers.put(headName, request.getHeader(headName));
                if (headName.toUpperCase().equals(SystemHeader.X_CA_KEY.toUpperCase())) {
                    appkey = request.getHeader(headName);
                }
                if (headName.toUpperCase().equals(SystemHeader.X_CA_SIGNATURE.toUpperCase())) {
                    signature = request.getHeader(headName);
                }
            }
            apiServiceMonitor.setOpenServiceInputHeader(JSONObject.fromObject(headers).toString());
            List<AppInstance> appList = appManage.getAppByAppKey(appkey);
            if (appList == null || appList.size() != 1) {
                writer = response.getWriter();
                writer.print("查询授权应用异常");
                writer.flush();
                apiServiceMonitor.setNotes("查询授权应用异常");
                apiServiceMonitor.setResult(ASM_ERROR_APP_UNAUTHORIZE);
                return;
            }
            appSecret = appList.get(0).getAppSecret();
            String appId = appList.get(0).getAppId();
            requestUserId = appList.get(0).getUserId();
            apiServiceMonitor.setCallerAppId(appId);
            apiServiceMonitor.setCallerUserId(requestUserId);
            // ---------------- 查询APP end ----------------


            // ---------------- 判断签名正确性 start ----------------
            if (!appSecret.equals(signature)) {
                writer = response.getWriter();
                writer.print("验证签名不正确！");
                writer.flush();
                apiServiceMonitor.setNotes("验证签名不正确");
                apiServiceMonitor.setResult(ASM_ERROR_SIGNATURE);
                return;
            }
            // ---------------- 判断签名正确性 end ----------------


            // ---------------- 通过context,reqPath关联查询API是否存在和状态 start ----------------
            ServiceDef serviceDef = checkApiService(apiContext, reqPath, apiServiceMonitor);
            if (serviceDef == null) {
                writer = response.getWriter();
                writer.print("API服务不存在");
                writer.flush();
                return;
            }
            String apiServiceId = serviceDef.getId();
            apiServiceMonitor.setApiServiceId(apiServiceId);
            apiServiceMonitor.setApiServiceName(serviceDef.getName());
            if (!OpenServiceConstants.api_audit_pass.equals(serviceDef.getAuditStatus())) {
                writer = response.getWriter();
                writer.print("API服务当前状态不可用");
                writer.flush();
                apiServiceMonitor.setNotes("API服务当前状态不可用");
                apiServiceMonitor.setResult(ASM_ERROR_SERVICE_NO_PASS);
                return;
            }
            // ----------------通过context,reqPath关联查询API是否存在和状态 end ----------------


            // ---------------- 通过serviceId和appId查询API授权状态 start ----------------
            Map applymap = new HashMap();
            applymap.put("apiServiceId", apiServiceId);
            applymap.put("appId", appId);
            applymap.put("authStatus", OpenServiceConstants.auth_status_pass);
            List<ServiceApply> alist = serviceApplyService.getList(applymap);
            if (alist == null || alist.size() == 0) {
                writer = response.getWriter();
                writer.print("API未授权应用");
                writer.flush();
                apiServiceMonitor.setNotes("API未授权应用");
                apiServiceMonitor.setResult(ASM_ERROR_SERVICE_UNAUTHORIZE_APP);
                return;
            }
            // ---------------- 通过serviceId和appId查询授权记录 end ----------------


            // ---------------- 余额判断 start ----------------
            servicePrice = serviceDef.getPrice();
            if (servicePrice.compareTo(new BigDecimal(0.00)) > 0) {
                //如果调用的API服务售价大于0 需要判断调用者账号下的余额
                if (StringUtils.isNotEmpty(requestUserId)) {
                    PayAccountCapital payAccountCapital = payService.getPayAccountByUserId(requestUserId);
                    BigDecimal balance = new BigDecimal(payAccountCapital.getAccountBalance());
                    if (balance.compareTo(new BigDecimal(0.00)) <= 0) {
                        writer = response.getWriter();
                        writer.print("账户余额不足，请及时充值！");
                        writer.flush();
                        apiServiceMonitor.setNotes("账户余额不足，请及时充值");
                        apiServiceMonitor.setResult(ASM_ERROR_BALANCE);
                        return;
                    } else {
                        BigDecimal bigDecimal = balance.subtract(serviceDef.getPrice());
                        if (bigDecimal.compareTo(new BigDecimal(0.00)) <= 0) {
                            writer = response.getWriter();
                            writer.print("账户余额不足调用，请及时充值！");
                            writer.flush();
                            apiServiceMonitor.setNotes("账户余额不足调用，请及时充值");
                            apiServiceMonitor.setResult(ASM_ERROR_BALANCE);
                            return;
                        }
                    }
                }
            }
            // ---------------- 余额判断 end ----------------


            // ---------------- 入参初始化 start ----------------
            List<ServiceInput> listServiceInput = serviceInputService.listByServiceId(apiServiceId);
            try {
                //读取request数据流
                String contentType = request.getHeader("Content-Type");
                if (StringUtils.isNotEmpty(contentType) && !contentType.equals(OpenServiceConstants.SC_TYPE_APPLICATION_XWWWFORMURLENCODED)) {
                    instream = HttpUtil.getRequestIn(request);
                    if (StringUtil.isNotEmpty(instream)) {
                        apiServiceMonitor.setOpenServiceInput(instream);
                    }
                }
                // 给入参赋值
                initInputList(request, serviceDef, listServiceInput);
            } catch (Exception e) {
                writer = response.getWriter();
                writer.print("输入参数异常:" + e.getMessage());
                writer.flush();
                apiServiceMonitor.setNotes("输入参数异常:" + e.getMessage());
                apiServiceMonitor.setResult(ASM_ERROR_PARAMETER);
                return;
            }
            // ---------------- 入参初始化 end ----------------


            //  ---------------- qps limit start ----------------
            String key = apiContext+"/"+reqPath;
            // 如果是首次请求
            if (map.get(key) == null){
                log.info("apiService: "+ JSON.toJSONString(serviceDef));
                map.putIfAbsent(key, RateLimiter.create( serviceDef != null ? serviceDef.getLimitCount() : default_limitCount ));
            }
            // 进行限流
            RateLimiter rateLimiter = map.get(key);
            if (!rateLimiter.tryAcquire()) {
                writer = response.getWriter();
                writer.print("请求过于频繁");
                writer.flush();
                apiServiceMonitor.setNotes("请求过于频繁");
                apiServiceMonitor.setResult(ASM_ERROR_QPS_LIMIT);
                return;
            }
            //  ---------------- qps limit end ----------------


            // ---------------- 执行转发请求 start ----------------
            startTime = System.currentTimeMillis();
            String result_str = "";
            if (serviceDef.getApiType()!=null && serviceDef.getApiType().equals("page")){
                // 拼接地址和参数，进行重定向
                String urlTarget = "";
                try{
                    urlTarget = httpUrlBuffer(serviceDef, listServiceInput, apiServiceMonitor);
                    success = true;
                    response.sendRedirect( urlTarget );
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }else{
                if (serviceDef.getScProtocol().equals("webService")) {
                    String type = serviceDef.getScFrame();
                    if ("Axiom".equals(type)) {
                        result_str = executeAxis2(success, serviceDef, listServiceInput, apiServiceMonitor);
                    } else if ("RPC".equals(type)) {
//                        result_str = executeRPC(success,serviceDef, listServiceInput);
//                        result_str = executeRPCAxis(success, serviceDef, listServiceInput, apiServiceMonitor);
                        result_str = executeRPCAxisClient(success, serviceDef, listServiceInput, apiServiceMonitor);
                    } else if ("RPCAxis".equals(type)) {
                        result_str = executeRPCAxis(success, serviceDef, listServiceInput, apiServiceMonitor);
                    }
                } else {
                    result_str = doRequest(response,success, instream, serviceDef, listServiceInput, apiServiceMonitor);
                }
                response.addHeader("Content-Type", OpenServiceConstants.getContentType(serviceDef.getContentType()));
                apiServiceMonitor.setOpenServiceOutput(result_str);
                apiServiceMonitor.setResult(ASM_SUCCESS);

                if (!serviceDef.getContentType().equals("binary")){
                    writer = response.getWriter();
                    writer.print(result_str);
                    writer.flush();
                }
            }

        } catch (Throwable e) {
            response.addHeader("Content-Type", OpenServiceConstants.content_type_html);
            success = false;
            log.error("目标服务调用出错", e);
            Map<String, Object> errorResult = new HashMap<String, Object>();
            errorResult.put("error", "执行服务出错");
            errorResult.put("error_description", e.getMessage());
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", errorResult);
            // 未知错误编码
            result.put("status", ASM_ERROR_UNKNOWN);
            writer = response.getWriter();
            writer.print(result);
            writer.flush();
            apiServiceMonitor.setOpenServiceOutput(String.valueOf(result));
            apiServiceMonitor.setNotes(String.valueOf(errorResult));
            apiServiceMonitor.setResult(ASM_ERROR_UNKNOWN);
        } finally {
            if (writer!= null){
                IOUtils.closeQuietly(writer);
            }
            responseTime = DateUtil.getCurrentTime2();
            long serviceTime = System.currentTimeMillis() - startTime;
            apiServiceMonitor.setServiceTotalTime((int) serviceTime);
            if (log.isDebugEnabled()) {
                log.debug("调用api执行的时间" + (serviceTime) + "毫秒");
            }
            // 扣款
            if (success) {
                if (requestUserId != null && servicePrice != null) {
                    // 成功调用、且api价格不为0时扣费
                    if (servicePrice.compareTo(new BigDecimal(0.00)) > 0) {
                        payService.subPayAccountByUserId(requestUserId, servicePrice + "");
                    }
                }
            }
            apiServiceMonitor.setResponseTime(responseTime);
            apiServiceMonitor.setCreateTime(DateUtil.getCurrentTime2());
            ApiServiceMonitorUtil.insertByThreadPool(monitorService, apiServiceMonitor, monitorExecutorService);
        }
    }

    /**
     * 检查api服务是否存在可用
     */
    public ServiceDef checkApiService(String apiContext, String reqPath, ApiServiceMonitor apiServiceMonitor) {
        //根据apiContext和reqPath查询唯一的API service
        ServiceDef serviceDef = null;
        Map<String, Object> tmp = new HashMap<>();
        tmp.put("context", apiContext);
        tmp.put("reqPath", "/" + reqPath);
        String context_path = "/" + apiContext + "/" + reqPath;
        List<ServiceDef> defs2 = serviceDefService.getByGroupContextAndPath(tmp);
        if (defs2 == null || defs2.size() != 1) {
            apiServiceMonitor.setNotes("API服务不存在,context_path=[" + context_path + "]");
            apiServiceMonitor.setResult(ASM_ERROR_SERVICE);
        } else {
            serviceDef = defs2.get(0);
        }
        return serviceDef;
    }


    /**
     * 验证输入参数并赋值
     */
    public void initInputList(HttpServletRequest request, ServiceDef serviceDef, List<ServiceInput> listServiceInput) throws Exception {
        for (ServiceInput serviceInput : listServiceInput) {
            String paramName = serviceInput.getName();
            String paramType = serviceInput.getType();
            String postionType = serviceInput.getScParamType();
            String fixedValue = serviceInput.getFixedValue();
            int required = serviceInput.getRequired();

            // 判断是文件类型还是基本类型
            if (StringUtils.equals(paramType, OpenServiceConstants.SC_TYPE_FILE)){
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile files = multipartRequest.getFile(paramName);
                serviceInput.setValue(files);
            }else{
                // 有设置固定值
                if (StringUtils.isNotEmpty(fixedValue)) {
                    // 先进行赋值。 （如果依然传了，进行覆盖）
                    serviceInput.setValue(fixedValue);
                }

                // 先从请求头中获取（适配可能出现的老方式）
                if (StringUtils.equals(postionType, OpenServiceConstants.SC_PARAMTYPE_HEAD)) {
                    String value = request.getHeader(paramName);
                    if (value != null) { // 防止null将固定值覆盖
                        serviceInput.setValue(value);
                    }
                }

                // 从参数中获取
                String value = request.getParameter(paramName);
                if (value != null){ // 防止null将固定值覆盖
                    serviceInput.setValue(value);
                }

                // 校验 (参数类型和必填性)
                checkData(paramType, (String)serviceInput.getValue(), paramName,required,postionType);
            }
        }
    }

    private void checkData(String type, String value, String key,Integer required,String postionType) throws Exception {
        // 如果是必填参数，校验是否为空
        if (required == 1){
            // body参数 raw类型，不执行校验
            if (StringUtils.equals(postionType,"body") && (type.toLowerCase().equals("application/json") || type.toLowerCase().equals("application/xml") )){
                return;
            }
            if (value == null){
                throw new Exception("请输入必填参数[" + key + "]");
            }
        }
        if (type.toLowerCase().equals("application/json")) {
            try {
                JSON.parseObject(value);
            } catch (Exception e) {
                throw new Exception("applicaiton/json数据格式错误[" + key + "=" + value + "]");
            }
        }
        if (type.toLowerCase().equals("int")) {
            try {
                Integer.parseInt(value);
            } catch (Exception e) {
                throw new Exception("int数据格式错误[" + key + "=" + value + "]");
            }
        }
        if (type.toLowerCase().equals("long")) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                throw new Exception("long数据格式错误[" + key + "=" + value + "]");
            }
        }
        if (type.toLowerCase().equals("double")) {
            try {
                Double.parseDouble(value);
            } catch (Exception e) {
                throw new Exception("double数据格式错误[" + key + "=" + value + "]");
            }
        }
        if (type.toLowerCase().equals("float")) {

            try {
                Float.parseFloat(value);
            } catch (Exception e) {
                throw new Exception("float数据格式错误[" + key + "=" + value + "]");
            }
        }
        if (type.toLowerCase().equals("boolean")) {
            try {
                Boolean.parseBoolean(value);
            } catch (Exception e) {
                throw new Exception("boolean数据格式错误[" + key + "=" + value + "]");
            }
        }
    }


    private String executeRPC(boolean success,ServiceDef ws, List<ServiceInput> serviceInputList) throws AxisFault {
        RPCServiceClient serviceClient = new RPCServiceClient();

        System.out.println("***************************:  " + ws.getScAddr());
        EndpointReference targetEPR = new EndpointReference(ws.getScAddr());
        Options options = serviceClient.getOptions();

        options.setTo(targetEPR);

        options.setAction(ws.getNameSpace() + "/" + ws.getSc_ws_function());
        QName qname = new QName(ws.getNameSpace(), ws.getSc_ws_function());

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%:  " + ws.getNameSpace() + "/" + ws.getSc_ws_function());
        Object[] parameters = null;
        if (serviceInputList !=null){
            parameters = new Object[serviceInputList.size()];

            int j = 0;
            for (ServiceInput item : serviceInputList) {
                parameters[j++] = item.getValue();
            }
        }else{
            parameters = new Object[] { null };
        }
//        if (param !=null){
//            parameters = new Object[param.size()];
//            if (serviceInputList.size() > 0 || param.size() > 0) {
//
//                int j = 0;
//                for (Map.Entry<String, Object> entry : param.entrySet()) {
//                    String mapKey = (String)entry.getKey();
//                    String mapValue = entry.getValue().toString();
//                    System.out.println(mapKey + ":" + mapValue);
//
//                    parameters[j++] = mapValue;
//                }
//            } else {
//                parameters = new Object[] { null };
//            }
//        }else{
//            parameters = new Object[] { null };
//        }
        try{
            System.out.println("############################:  " + parameters[0]);
        }catch (Exception e){
            System.out.println(e.toString());
        }

        OMElement element = serviceClient.invokeBlocking(qname, parameters);

        String result = getResults(element);

        System.out.println("result::::::::::::::::::::::" + result);
        return result;
    }

    private String executeRPCAxis(boolean success, ServiceDef ws, List<ServiceInput> serviceInputList, ApiServiceMonitor apiServiceMonitor) throws Exception {
        // monitor记录所有入参
        JSONObject serviceInputParam = new JSONObject();
        Service service = new Service();
        Call call = (Call) service.createCall();
        call.setTargetEndpointAddress(ws.getScAddr());
        call.setOperationName(new QName(ws.getNameSpace(), ws.getSc_ws_function()));
        Object[] parameters = null;
        if (serviceInputList != null) {
            parameters = new Object[serviceInputList.size()];
            int j = 0;
            for (ServiceInput item : serviceInputList) {
                parameters[j++] = item.getValue();
                call.addParameter(item.getName(), org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                serviceInputParam.put(item.getName(), item.getValue());
            }
            call.setReturnType(org.apache.axis.Constants.XSD_STRING);
        } else {
            parameters = new Object[]{null};
        }
        apiServiceMonitor.setServiceInput(serviceInputParam.toString());
        String result = (String) call.invoke(parameters);
        return result;
    }

    private String executeRPCAxisClient(boolean success, ServiceDef ws, List<ServiceInput> serviceInputList, ApiServiceMonitor apiServiceMonitor) throws Exception {
        JSONObject serviceInputParam = new JSONObject();
        Object[] parameters = new Object[serviceInputList.size()];
        Call call = initAxisClientCall(ws.getScAddr(), ws.getNameSpace(), ws.getSc_ws_function(), serviceInputList, parameters, serviceInputParam);
        apiServiceMonitor.setServiceInput(serviceInputParam.toString());
        String resp = (String) call.invoke(parameters);
        return resp;
    }

    /**
     * 初始化Call
     *
     * @param address           调用地址
     * @param namespaceURI      命名空间地址
     * @param localPart         方法名
     * @param serviceInputList  入参List
     * @param parameters        webservice调用参数
     * @param serviceInputParam monitor记录入参信息
     * @return
     * @throws ServiceException
     * @throws MalformedURLException
     */
    private static Call initAxisClientCall(String address, String namespaceURI, String localPart, List<ServiceInput> serviceInputList,
                                           Object[] parameters, JSONObject serviceInputParam) throws ServiceException, MalformedURLException {
        Service service = new Service();
        Call call = null;
        call = (Call) service.createCall();
        call.setTargetEndpointAddress(new URL(address));
        call.setOperation(initAxisClientOperation(namespaceURI, localPart, localPart, serviceInputList, parameters, serviceInputParam));
        call.setUseSOAPAction(true);
        call.setSOAPActionURI(namespaceURI + localPart);
        call.setEncodingStyle(null);
        call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(new QName(namespaceURI, localPart));
        return call;
    }

    /**
     * 初始化服务描述
     *
     * @param namespaceURI      命名空间地址
     * @param localPart         方法名
     * @param returnName        ReturnQName，暂时写为方法名也能调通
     * @param serviceInputList
     * @param parameters
     * @param serviceInputParam
     * @return
     */
    private static OperationDesc initAxisClientOperation(String namespaceURI, String localPart, String returnName, List<ServiceInput> serviceInputList,
                                                         Object[] parameters, JSONObject serviceInputParam) {
        OperationDesc oper;
        ParameterDesc param;
        oper = new OperationDesc();
        oper.setName(localPart);
        int j = 0;
        //取出所有的入参并赋值
        for (ServiceInput in : serviceInputList) {
            param = new ParameterDesc(new QName(namespaceURI, in.getScName()),
                    ParameterDesc.IN, new QName(namespaceURI, "string"), String.class, false, false);
            param.setOmittable(true);
            oper.addParameter(param);
            parameters[j++] = in.getValue();
            serviceInputParam.put(in.getScName(), in.getValue());
        }
        //暂时只支持string格式的返回值
        oper.setReturnType(new QName(namespaceURI, "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName(namespaceURI, returnName));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        return oper;
    }

    public static String getResults(OMElement element) {
        if (element == null) {
            return null;
        }

        Iterator<OMElement> iterator = element.getChildElements();

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        OMElement result = null;
        Map<String, String> data = new HashMap<String, String>();
        while (iterator.hasNext()) {
            OMElement item = iterator.next();
            String xmlOld = item.toString();
            System.out.println("xmlOld:" + xmlOld);
            if (xmlOld.contains("jythis")){
                String xmlNew1 = xmlOld.replace("&lt;","<").replace("&#xd","").replace(";","").replace("</ns1:out>","");

                Pattern pat = Pattern.compile("<ns1:out.*?>");
                Matcher mat = pat.matcher(xmlNew1);
                String xmlNew = mat.replaceAll("");
                return xmlNew;
            }else{
                Iterator innerItr = item.getChildElements();
                while (innerItr.hasNext()) {
                    OMElement elem = (OMElement)innerItr.next();

                    System.out.println("\t\t" + elem.getLocalName() + ": " + elem.getText());
                    data.put(elem.getLocalName(), elem.getText());
                    list.add(data);
                    data = new HashMap<String, String>();
                }
            }
        }
        return JSON.toJSONString(list);
    }

    private String executeAxis2(boolean success, ServiceDef ws, List<ServiceInput> listServiceInput, ApiServiceMonitor apiServiceMonitor) throws AxisFault {
        try {
            // monitor记录所有入参
            JSONObject serviceInputParam = new JSONObject();
            String[] params = new String[listServiceInput.size()];
            int i = 0;
            for (ServiceInput serviceInput : listServiceInput) {
                String paramType = serviceInput.getScParamType();
                if (paramType.equalsIgnoreCase("body")) {
                    System.out.println(serviceInput.getScType() + ":" + serviceInput.getValue());
                    params[i++] = serviceInput.getName();
                }
            }

            String[] paramValues = new String[listServiceInput.size()];
            int j = 0;
            for (ServiceInput item : listServiceInput) {
                paramValues[j++] = (String)item.getValue();
                serviceInputParam.put(item.getName(), item.getValue());
            }
            apiServiceMonitor.setServiceInput(serviceInputParam.toString());
//            String[] paramValues = new String[param.size()];
//            int j = 0;
//            for (Map.Entry<String, Object> entry : param.entrySet()) {
//                String mapKey = (String)entry.getKey();
//                String mapValue = entry.getValue().toString();
//                System.out.println(mapKey + ":" + mapValue);
//                paramValues[j++] = mapValue;
//            }
            OMElement getPricePayload = buildParam(ws.getNameSpace(), params, paramValues, "tn", ws.getSc_ws_function(), "tn");

            Options options = new Options();
            options.setTo(new EndpointReference(ws.getScAddr()));
            options.setTransportInProtocol("http");


            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);


            OMElement result = sender.sendReceive(getPricePayload);
            String response = result.getFirstElement().getText();
            System.out.println("Current price of WSO: " + response);
            success = true;
            return response;
        } catch (AxisFault e) {
            log.error("axis2执行异常", e);
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static OMElement buildParam(String nameSpace, String[] params, String[] paramValues, String paramPrefix, String method, String wsMethodPrefix) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace(nameSpace, wsMethodPrefix);
        OMNamespace omNsParam = (paramPrefix == null) ? null : fac.createOMNamespace(nameSpace, paramPrefix);
        OMElement data = fac.createOMElement(method, omNs);
        for (int i = 0; i < params.length; i++) {
            OMElement inner = fac.createOMElement(params[i], omNsParam);
            inner.setText(paramValues[i]);
            data.addChild(inner);
        }
        return data;
    }

    private String doRequest(HttpServletResponse response,boolean success,String instream, ServiceDef serviceDef, List<ServiceInput> listServiceInput, ApiServiceMonitor apiServiceMonitor) throws Exception {
        String scType = null;
        String dataType = serviceDef.getContentType();

        // 地址拼接
        String addr = serviceDef.getScAddr();
        addr = addr.endsWith("/") ? addr.substring(0, addr.length() - 1) : addr;
        StringBuilder httpurl = new StringBuilder();
        httpurl.append(addr);

        // monitor记录所有入参
        JSONObject serviceInputParam = new JSONObject();

        Collections.sort(listServiceInput);//根据scSeq排序

        //组装后端参数
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(Constants.HTTP_HEADER_ACCEPT, "*/*");

        Map<String,Object> paramsMap = new HashMap<>();
        Map<String,Object> paramsTypeMap = new HashMap<>();

        // 核保定制化（将日志id传递给核保获取模型接口）
        if (httpurl.indexOf("getHealthModel") != -1){
            String key = "logId";
            String value = apiServiceMonitor.getId();
            paramsMap.put(key,value);
            paramsTypeMap.put(key,"string");
            serviceInputParam.put(key, value);
        }

        for (ServiceInput serviceInput : listServiceInput) {
            String paramsType = serviceInput.getType();
            String paramsName = serviceInput.getName();
            String paramPositionType = serviceInput.getScParamType();

            // 入参解密
            Object decryptedParam = decryptedParam(serviceInput, serviceDef.getEncryptionType());
            if (decryptedParam!=null){
                serviceInputParam.put(paramsName, decryptedParam.toString());
            }

            // 根据参数位置 来进行处理
            if (paramPositionType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_PATH)) {
                httpurl.append("/").append(decryptedParam);
            } else if (paramPositionType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_QUERY)) {
                Object object = serviceInput.getValue();
                if (object != null){
                    paramsMap.put(paramsName,object);
                }
            } else if (paramPositionType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_HEAD)) {
                headerMap.put(paramsName, (String)decryptedParam);
            } else if (paramPositionType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_BODY)) {
                // 如果选择了Body类型，但不是raw类型，跟query一样，也放到 K-V Map
                if (!serviceInput.getScType().equals(OpenServiceConstants.SC_TYPE_APPLICATION_JSON) && !serviceInput.getScType().equals(OpenServiceConstants.SC_TYPE_TEXT_XML)) {
                    if (decryptedParam != null ) {
                        paramsMap.put(paramsName, decryptedParam);
                    }
                } else {
                    if (StringUtils.isEmpty(instream) && decryptedParam != null) {
                        instream = (String) decryptedParam;
                    }
                    scType = serviceInput.getScType();
                }
            }
            paramsTypeMap.put(paramsName,paramsType);
        }

        int timeout = 30000;
        String method = serviceDef.getScHttpMethod().toUpperCase();
        String result = null;
        apiServiceMonitor.setServiceInput(serviceInputParam.toString());
        apiServiceMonitor.setServiceInputHeader(JSONObject.fromObject(headerMap).toString());
        apiServiceMonitor.setServiceMethod(method);
        System.out.println("请其头为:" + JSONObject.fromObject(headerMap).toString());
        switch (method) {
            case "GET":
                result = execGet(dataType,response,success, httpurl.toString(), timeout, headerMap,paramsMap,listServiceInput);
                break;
            case "POST":
                result = execPost(dataType,response,success, instream, scType, httpurl.toString(), timeout, headerMap, paramsMap, paramsTypeMap,listServiceInput);
                break;
            default:
                result = "{error:404}";
        }
        apiServiceMonitor.setServiceOutput(result);

        result = encryptionResult(result, serviceDef.getEncryptionType());
        return result;
    }


    private String httpUrlBuffer(ServiceDef serviceDef, List<ServiceInput> listServiceInput, ApiServiceMonitor apiServiceMonitor) throws Exception {

        String addr = serviceDef.getScAddr();
        addr = addr.endsWith("/") ? addr.substring(0, addr.length() - 1) : addr;
        StringBuilder httpurl = new StringBuilder();
        httpurl.append(addr);

        //组装后端参数param
        Map<String, String> header = new HashMap<>();
        JSONObject serviceInputParam = new JSONObject();
        header.put(Constants.HTTP_HEADER_ACCEPT, "*/*");
        Collections.sort(listServiceInput);//根据scSeq排序

        boolean firstQueryParam = true;
        for (ServiceInput serviceInput : listServiceInput) {
            serviceInputParam.put(serviceInput.getScName(), serviceInput.getValue());
            //判断必填属性
            if (OpenDataConstants.is_null_no == serviceInput.getRequired() && serviceInput.getValue()!=null && !serviceInput.getScType().equals("text/xml") && !serviceInput.getScType().equals("application/json")) {
                throw new Exception("请传入必填参数" + serviceInput.getName());
            }

            // 执行加解密，接口级别和参数级别
            Object decryptedParam = decryptedParam(serviceInput, serviceDef.getEncryptionType());
            serviceInputParam.put(serviceInput.getScName(), decryptedParam);

            checkData(serviceInput.getScType(), (String)decryptedParam, serviceInput.getScName(),serviceInput.getRequired(),serviceInput.getScParamType());//判断参数类型是否正确
            String paramType = serviceInput.getScParamType();

            if (paramType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_QUERY)) {
//                if (StringUtils.equals(serviceInput.getScName(),"xCaKey") || StringUtils.equals(serviceInput.getScName(),"xCaSignature") ){
//                    continue;
//                }
                if (firstQueryParam) {
                    if (decryptedParam != null) {
                        httpurl.append("?").append(serviceInput.getScName()).append("=").append(URLEncoder.encode((String)decryptedParam));
                        firstQueryParam = false;
                    }
                } else {
                    if (decryptedParam != null) {
                        httpurl.append("&").append(serviceInput.getScName()).append("=").append(URLEncoder.encode((String)decryptedParam));
                    }
                }
            }
        }

        apiServiceMonitor.setServiceInput(serviceInputParam.toString());
        apiServiceMonitor.setServiceInputHeader(JSONObject.fromObject(header).toString());
        apiServiceMonitor.setServiceMethod(serviceDef.getScHttpMethod().toUpperCase());

        System.out.println("请其头为:" + JSONObject.fromObject(header).toString());
        return httpurl.toString();
    }

    /**
     * 解密参数 接口级
     *
     * @param encryptionType
     * @return
     * @throws Exception
     */
    private static Object decryptedParam(ServiceInput serviceInput, String encryptionType) throws Exception {
        if (serviceInput.getType().equals(SC_TYPE_FILE)){
            return serviceInput.getValue();
        }
        String decryptedParamStr = "";
        String param = (String)serviceInput.getValue();
        String url = serviceInput.getDecryptUrl();
        String name = serviceInput.getName();
        if (StringUtil.isNotEmpty(encryptionType) && StringUtil.isNotEmpty(ENCRYPTION_MAP.get(encryptionType))) {
            switch (encryptionType) {
                case ENCRYPT_MODE_NO:
                    decryptedParamStr = param;
                    break;
                case ENCRYPT_MODE_KEY_BASE64:
                    decryptedParamStr = decryptBASE64String(param);
                    break;
                case ENCRYPT_MODE_KEY_REST:
                    decryptedParamStr = decryptRESTString(url,name,param);
                    break;
                case ENCRYPT_MODE_KEY_SM3:
                    throw new Exception("暂不支持国密SM3解密，param = [" + param + "]");
                case ENCRYPT_MODE_KEY_MD5:
                    throw new Exception("暂不支持MD5解密，param = [" + param + "]");
                case ENCRYPT_MODE_KEY_SHA_1:
                    throw new Exception("暂不支持SHA-1解密，param = [" + param + "]");
                default:
                    throw new Exception("暂不支持对所选加密方式解密，param = [" + param + "],encryptionType = [" + encryptionType + "]");
            }
        } else {
            throw new Exception("传入加密方式不正确，请检查后重试，param = [" + param + "],encryptionType = [" + encryptionType + "]");
        }
        // 赋值后进行参数级加解密
        serviceInput.setValue(decryptedParamStr);
        return decryptedParam_paramLevel(serviceInput);
    }

    /**
     * 解密参数 参数级
     *
     * @return
     * @throws Exception
     */
    private static String decryptedParam_paramLevel(ServiceInput serviceInput) throws Exception {
        String after_value = "";
        String param = (String)serviceInput.getValue();
        String name = serviceInput.getName();

        String decryptType = serviceInput.getDecryptType();
        String encryptType = serviceInput.getEncryptType();
        String decryptUrl = serviceInput.getDecryptUrl();
        String encryptUrl = serviceInput.getEncryptUrl();

        // 执行解密
        if ( StringUtil.isNotEmpty(decryptType) && !StringUtils.equals(decryptType,"")) {
            switch (decryptType) {
                case ENCRYPT_MODE_NO:
                    after_value = param;
                    break;
                case ENCRYPT_MODE_KEY_BASE64:
                    after_value = decryptBASE64String(param);
                    break;
                case ENCRYPT_MODE_KEY_REST:
                    after_value = decryptRESTString(decryptUrl,name,param);
                    break;
                case ENCRYPT_MODE_KEY_SM3:
                    throw new Exception("暂不支持国密SM3解密，param = [" + param + "]");
                case ENCRYPT_MODE_KEY_MD5:
                    throw new Exception("暂不支持MD5解密，param = [" + param + "]");
                case ENCRYPT_MODE_KEY_SHA_1:
                    throw new Exception("暂不支持SHA-1解密，param = [" + param + "]");
                default:
                    throw new Exception("暂不支持对所选解密方式，param = [" + param + "],encryptionType = [" + decryptType + "]");
            }
        }
        // 执行加密
        if ( StringUtil.isNotEmpty(encryptType) && !StringUtils.equals(encryptType,"") ) {
            switch (encryptType) {
                case ENCRYPT_MODE_NO:
                    after_value = after_value;
                    break;
                case ENCRYPT_MODE_KEY_BASE64:
                    after_value = encryptBASE64String(after_value);
                    break;
                case ENCRYPT_MODE_KEY_REST:
                    after_value = encryptRESTString(encryptUrl,name,after_value);
                    break;
                case ENCRYPT_MODE_KEY_SM3:
                    throw new Exception("暂不支持国密SM3加密，param = [" + after_value + "]");
                case ENCRYPT_MODE_KEY_MD5:
                    throw new Exception("暂不支持MD5加密，param = [" + after_value + "]");
                case ENCRYPT_MODE_KEY_SHA_1:
                    throw new Exception("暂不支持SHA-1加密，param = [" + after_value + "]");
                default:
                    throw new Exception("暂不支持对所选加密方式，param = [" + after_value + "],encryptionType = [" + encryptType + "]");
            }
        }
        return after_value;
    }

    /**
     * 加密返回值
     *
     * @param result
     * @param encryptionType
     * @return
     * @throws Exception
     */
    private static String encryptionResult(String result, String encryptionType) throws Exception {
        String encryptionResultStr = "";
        if (StringUtil.isNotEmpty(encryptionType) && StringUtil.isNotEmpty(ENCRYPTION_MAP.get(encryptionType))) {
            switch (encryptionType) {
                case ENCRYPT_MODE_NO:
                    encryptionResultStr = result;
                    break;
                case ENCRYPT_MODE_KEY_BASE64:
                    encryptionResultStr = encryptBASE64String(result);
                    break;
                case ENCRYPT_MODE_KEY_SM3:
                    encryptionResultStr = SM3.byteArrayToHexString(SM3.hash(result.getBytes()));
                    break;
                case ENCRYPT_MODE_KEY_MD5:
                    encryptionResultStr = encryptMD5String(result);
                    break;
                case ENCRYPT_MODE_KEY_SHA_1:
                    encryptionResultStr = encryptSHA1(result);
                    break;
                default:
                    throw new Exception("暂不支持所选加密方式，param = [" + result + "],encryptionType = [" + encryptionType + "]");
            }
        } else {
            throw new Exception("传入加密方式不正确，请检查后重试，param = [" + result + "],encryptionType = [" + encryptionType + "]");
        }
        return encryptionResultStr;
    }

    /**
     * GET方法访问URL
     *
     * @param url
     * @param timeout
     * @param headersMap
     * @return
     */
    public static String execGet(String dataType,HttpServletResponse responseOut, boolean success,String url, int timeout, Map<String, String> headersMap, Map<String,Object> paramsMap,List<ServiceInput> listServiceInput) {

        StringBuffer urlAppend = new StringBuffer();
        boolean firstQueryParam = true;
        for (Map.Entry<String,Object> temp : paramsMap.entrySet()){
            String key = temp.getKey();
            Object value = temp.getValue();
            if (firstQueryParam) {
                    urlAppend.append("?").append(key).append("=").append(URLEncoder.encode((String)value));
                    firstQueryParam = false;
            } else {
                urlAppend.append("&").append(key).append("=").append(URLEncoder.encode((String)value));
            }
        }
        HttpGet httpGet = new HttpGet(url + urlAppend.toString());

        // 设置请求头
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        if (StringUtil.isNotEmpty(headersMap)) {
            for (String key : headersMap.keySet()) {
                System.out.println("赋值head："+key+"  "+headersMap.get(key));
                httpGet.addHeader(key, headersMap.get(key));
            }
        }

        // 执行请求
        String result = "";
        try {
            CloseableHttpClient httpclient = createCloseableHttpClient(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200){
                success = true;
            }
            HttpEntity entity = response.getEntity();

            if (StringUtils.equals(dataType,"binary")){
                createResponseFile(headersMap,responseOut, response, entity,listServiceInput);
            }else{
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);

        } catch (Exception e) {
            result = "API网关GET method failed to access URL！";
            log.error("API网关GET method failed to access URL，URL：" + url, e);
        }
        return result;
    }

    private static void createResponseFile(Map<String,String> headersMap,HttpServletResponse responseOut, CloseableHttpResponse response,HttpEntity entity,List<ServiceInput> listServiceInput) throws Exception {
        Header contentHead = response.getLastHeader("Content-Disposition");
        String filename = null;
        if (contentHead != null){
            HeaderElement[] elements = contentHead.getElements();
            for (HeaderElement el : elements) {
                //遍历，获取filename
                NameValuePair pair = el.getParameterByName("filename");
                filename = pair.getValue();

                if (null != filename) {
                    break;
                }
            }
        }else{
            filename = UUID.randomUUID().toString();
            for (ServiceInput serviceInput : listServiceInput) {
                String paramPositionType = serviceInput.getScParamType();
                if (paramPositionType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_PATH)) {
                    filename = (String)serviceInput.getValue();
                    break;
                }
            }
        }

        InputStream inputStream = entity.getContent();

        // 生成本地文件
//        File tmp = File.createTempFile("tmp", filename, new File("C:\\"));
//        OutputStream os1 = new FileOutputStream(tmp);
//        int bytesRead = 0;
//        byte[] buffer1 = new byte[8192];
//        while ((bytesRead = inputStream.read(buffer1, 0, 8192)) != -1) {
//            os1.write(buffer1, 0, bytesRead);
//        }
//        inputStream.close();

//        if(tmp.exists()) {
            responseOut.setContentType("application/octet-stream");
            responseOut.addHeader("Content-Disposition","attachment;filename="+filename);
            byte[] buffer = new byte[1024];
            InputStream fis =null;
            BufferedInputStream bis =null;
            try {
                fis = inputStream;
                bis = new BufferedInputStream(fis);
                OutputStream os = responseOut.getOutputStream();
                int i = bis.read(buffer);
                while ( i!=-1 ) {
                    os.write(buffer, 0, i);
                    i=bis.read(buffer);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//        }
    }

    /**
     * POST方法访问URL
     *
     * @param url
     * @param timeout
     * @return
     */

    public static String execPost(String dataType, HttpServletResponse responseOut, boolean success,String instream, String scType, String url, int timeout, Map<String, String> headersMap, Map<String,Object> paramsMap, Map<String,Object>  paramsType,List<ServiceInput> serviceInputList) throws IOException {
        // 创建请求对象，进行初始化设置
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);

        // 赋值请求头
        if (StringUtil.isNotEmpty(headersMap)) {
            for (String key : headersMap.keySet()) {
                httpPost.addHeader(key, headersMap.get(key));
            }
        }

        // 如果参数不为空
        File file = null;
        if (paramsMap.size()>0) {
            // 复杂Entity构造器
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

            // 遍历Query参数，赋值到 entityBuilder
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                //添加普通参数
                if(paramsType.get(entry.getKey()).equals(SC_TYPE_FILE)){
                    //添加上传的文件
                    MultipartFile mulFile = (MultipartFile) entry.getValue();
                    String fileName = mulFile.getOriginalFilename();
                    file = asFile(mulFile.getInputStream(),fileName);
                    entityBuilder.addPart(entry.getKey(),new FileBody(file));
                }else{
                    entityBuilder.addTextBody(entry.getKey(), (String)entry.getValue());
                }
            }
            HttpEntity httpEntity = entityBuilder.build();
            httpPost.setEntity(httpEntity);

//        } else if (StringUtils.isNotEmpty(instream) && (scType.equals(OpenServiceConstants.SC_TYPE_APPLICATION_JSON) || scType.equals(OpenServiceConstants.SC_TYPE_TEXT_XML))) {
        } else if (StringUtils.isNotEmpty(instream)) {
            switch (scType) {
                case OpenServiceConstants.SC_TYPE_APPLICATION_JSON:
                    httpPost.addHeader("Content-Type", OpenServiceConstants.content_type_json);
                    break;
                case OpenServiceConstants.SC_TYPE_TEXT_XML:
                    httpPost.addHeader("Content-Type", OpenServiceConstants.content_type_text_xml);
                    break;
                default:
                    httpPost.addHeader("Content-Type", "text/plain");
                    break;
            }
            try {
                httpPost.setEntity(new StringEntity(instream, "utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 执行请求
        String result = "";
        try{
            final CloseableHttpClient httpclient = createCloseableHttpClient(url);
            final CloseableHttpResponse response = httpclient.execute(httpPost);
            // 请求成功
            if (response.getStatusLine().getStatusCode() == 200){
                success = true;
            }
            // 响应Entity
            HttpEntity entity = response.getEntity();
            if (StringUtils.equals(dataType,"binary")){
                createResponseFile(headersMap, responseOut, response, entity, serviceInputList);
            }else{
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            result = "API网关POST method failed to access URL!";
            e.printStackTrace();
            log.error("API网关POST method failed to access URL，URL：" + url, e);
        }finally {
            if (file != null){
                file.delete();
            }
        }

        return result;
    }

    public static File asFile(InputStream inputStream,String fileName) throws IOException{
        File tmp = File.createTempFile(fileName+"-", ".zip", new File("/usr/local/tempFile/"));
//        File tmp = File.createTempFile(fileName+"-", ".zip", new File("C:\\"));
        OutputStream os = new FileOutputStream(tmp);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        return tmp;
    }

    public static CloseableHttpClient createCloseableHttpClient(String url) throws Exception {
        if (!url.toLowerCase().startsWith("https")) {
            return HttpClients.createDefault();
        }
        SSLContext sslcontext = createIgnoreVerifySSL();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(100);
        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(connManager)
                .build();
    }

    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLS");
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

}
