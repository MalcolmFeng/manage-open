package com.inspur.bigdata.manage.open.service.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSON;
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
import net.sf.json.JSONObject;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.tools.ant.taskdefs.EchoXML;
import org.loushang.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.json.JSONObject;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
    public Map<String, Object> apiTest(@PathVariable("apiServiceId") String apiServiceId, HttpServletRequest request) {
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
            String result_str = doRequest("", serviceDef, listServiceInput, new ApiServiceMonitor());
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
            if (success) {
                // TODO 服务调用成功，查询之前调用此服务失败的记录，将之前的失败的etime
                // (end_time)修改为当前时间，表示服务已经修正可用
                // 首先根据service_id获取表monitor_item中有关服务的记录，有则处理
            }
        }
    }

    @RequestMapping("/do/{apiContext}/{reqPath}")
    @ResponseBody
    public void execute(@PathVariable("apiContext") String apiContext, @PathVariable("reqPath") String reqPath,
                        HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        boolean success = true;
        long startTime = 0;
        String requestUserId = null;
        BigDecimal servicePrice = null;
        String instream = null;
        /**获取请求者IP*/
        String requestIp = ApiServiceMonitorUtil.getClientIp(request);
        String requestTime = DateUtil.getCurrentTime2();
        String responseTime = null;
        ApiServiceMonitor apiServiceMonitor = new ApiServiceMonitor();
        apiServiceMonitor.setCallerIp(requestIp);
        apiServiceMonitor.setRequestTime(requestTime);
        apiServiceMonitor.setOpenServiceInput(JSONObject.fromObject(request.getParameterMap()).toString());
        apiServiceMonitor.setOpenServiceMethod(request.getMethod());
        apiServiceMonitor.setOpenServiceRequestURL(request.getRequestURL().toString());
        String context_path = "/" + apiContext + "/" + reqPath;
        try {
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Type", OpenServiceConstants.content_type_html);
            writer = response.getWriter();
            //组装头部并获取 appkey、appSecret 和请求签名signature
            Enumeration<String> headNames = request.getHeaderNames();
            Map<String, String> headers = new HashMap<>();
            String appkey = null;
            String appSecret = null;
            String signature = null;
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
                success = false;
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
            /**
             * 前台传入X-Ca-Key=appKey,X-Ca-Signature=appSecret 验证身份
             */

            //生成签名验证是否一致
//            Map<String,String[]> pmap=request.getParameterMap();
//            Iterator<String> iterator= pmap.keySet().iterator();
//            Map<String,String> reqmap=new HashMap<>();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                reqmap.put(key,pmap.get(key)[0]);
//            }
//            String pre_sign= SignUtil.sign(appSecret,
//                    request.getMethod().toUpperCase(),
//                    context_path,
//                    headers,
//                    reqmap,
//                    null,
//                    new ArrayList<String>());
            if (!appSecret.equals(signature)) {//判断签名正确性
                success = false;
//                headers.remove(SystemHeader.X_CA_SIGNATURE_HEADERS);
//                String signurl=SignUtil.buildStringToSign(request.getMethod().toUpperCase(),
//                        context_path,
//                        headers,
//                        reqmap,
//                        null,
//                        new ArrayList<String>());
                writer.print("验证签名不正确！");
                writer.flush();
                apiServiceMonitor.setNotes("验证签名不正确");
                apiServiceMonitor.setResult(ASM_ERROR_SIGNATURE);
                return;
            }
            //通过context,reqPath关联查询apiService
            ServiceDef serviceDef = checkApiService(apiContext, reqPath, apiServiceMonitor);
            if (serviceDef == null) {
                success = false;
                writer.print("API服务不存在");
                writer.flush();
                return;
            }
            String apiServiceId = serviceDef.getId();
            apiServiceMonitor.setApiServiceId(apiServiceId);
            apiServiceMonitor.setApiServiceName(serviceDef.getName());
            if (!OpenServiceConstants.api_audit_pass.equals(serviceDef.getAuditStatus())) {
                success = false;
                writer.print("API服务当前状态不可用");
                writer.flush();
                apiServiceMonitor.setNotes("API服务当前状态不可用");
                apiServiceMonitor.setResult(ASM_ERROR_SERVICE_NO_PASS);
                return;
            }

            //TODO 通过serviceId和appId查询授权记录，无授权直接返回
            Map applymap = new HashMap();
            applymap.put("apiServiceId", apiServiceId);
            applymap.put("appId", appId);
            applymap.put("authStatus", OpenServiceConstants.auth_status_pass);
            List<ServiceApply> alist = serviceApplyService.getList(applymap);
            if (alist == null || alist.size() == 0) {
                success = false;
                writer.print("API未授权应用");
                writer.flush();
                apiServiceMonitor.setNotes("API未授权应用");
                apiServiceMonitor.setResult(ASM_ERROR_SERVICE_UNAUTHORIZE_APP);
                return;
            }

            servicePrice = serviceDef.getPrice();
            if (servicePrice.compareTo(new BigDecimal(0.00)) > 0) {
                //如果调用的API服务售价大于0 需要判断调用者账号下的余额
                if (StringUtils.isNotEmpty(requestUserId)) {
                    PayAccountCapital payAccountCapital = payService.getPayAccountByUserId(requestUserId);
                    BigDecimal balance = new BigDecimal(payAccountCapital.getAccountBalance());
                    if (balance.compareTo(new BigDecimal(0.00)) <= 0) {
                        success = false;
                        writer.print("账户余额不足，请及时充值！");
                        writer.flush();
                        apiServiceMonitor.setNotes("账户余额不足，请及时充值");
                        apiServiceMonitor.setResult(ASM_ERROR_BALANCE);
                        return;
                    } else {
                        BigDecimal bigDecimal = balance.subtract(serviceDef.getPrice());
                        if (bigDecimal.compareTo(new BigDecimal(0.00)) <= 0) {
                            success = false;
                            writer.print("账户余额不足调用，请及时充值！");
                            writer.flush();
                            apiServiceMonitor.setNotes("账户余额不足调用，请及时充值");
                            apiServiceMonitor.setResult(ASM_ERROR_BALANCE);
                            return;
                        }
                    }
                }
            }
            List<ServiceInput> listServiceInput = serviceInputService.listByServiceId(apiServiceId);
            try {
                //读取request数据流
                String contentType = request.getHeader("Content-Type");
                if (StringUtils.isNotEmpty(contentType) && !contentType.equals(OpenServiceConstants.SC_TYPE_APPLICATION_XWWWFORMURLENCODED)) {
                    instream = HttpUtil.getRequestIn(request);
                    apiServiceMonitor.setOpenServiceInput(instream);
                }
                initInputList(request, listServiceInput);
            } catch (Exception e) {
                success = false;
                e.printStackTrace();
                writer.print("输入参数异常:" + e.getMessage());
                writer.flush();
                apiServiceMonitor.setNotes("输入参数异常:" + e.getMessage());
                apiServiceMonitor.setResult(ASM_ERROR_PARAMETER);
                return;
            }
            startTime = System.currentTimeMillis();

            String result_str = "";
            Cookie[] cookies = request.getCookies();
            if (serviceDef.getScProtocol().equals("webService")) {
                String type = serviceDef.getScFrame();
                if ("Axiom".equals(type)) {

                    result_str = executeAxis2(serviceDef, listServiceInput);
                } else if ("RPC".equals(type)) {
                    result_str = executeRPC(serviceDef, listServiceInput);
                }
            } else {
//                result_str = doRequest(serviceDef, listServiceInput, cookies);
                result_str = doRequest(instream, serviceDef, listServiceInput, apiServiceMonitor);
            }
            response.addHeader("Content-Type", OpenServiceConstants.getContentType(serviceDef.getContentType()));
            writer.print(result_str);
            writer.flush();
            apiServiceMonitor.setOpenServiceOutput(result_str);
            apiServiceMonitor.setResult(ASM_SUCCESS);
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
            writer.print(result);
            writer.flush();
            apiServiceMonitor.setOpenServiceOutput(String.valueOf(result));
            apiServiceMonitor.setNotes(String.valueOf(errorResult));
            apiServiceMonitor.setResult(ASM_ERROR_UNKNOWN);
        } finally {
            IOUtils.closeQuietly(writer);
//            responseTime = OpenServiceConstants.sf.format(new Date());
            responseTime = DateUtil.getCurrentTime2();
            long serviceTime = System.currentTimeMillis() - startTime;
            if (log.isDebugEnabled()) {
                log.debug("调用api执行的时间" + (serviceTime) + "毫秒");
            }
            if (success) {
                // TODO 服务调用成功，查询之前调用此服务失败的记录，将之前的失败的etime
                // (end_time)修改为当前时间，表示服务已经修正可用
                // 首先根据service_id获取表monitor_item中有关服务的记录，有则处理
                if (requestUserId != null && servicePrice != null) {
                    //成功调用时扣费
                    payService.subPayAccountByUserId(requestUserId, servicePrice + "");
                }
                apiServiceMonitor.setServiceTotalTime((int) serviceTime);
            }
            apiServiceMonitor.setResponseTime(responseTime);
            apiServiceMonitor.setCreateTime(DateUtil.getCurrentTime2());
//            monitorService.insert(apiServiceMonitor);
//            ApiServiceMonitorUtil.insert(monitorService, apiServiceMonitor);
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
    public void initInputList(HttpServletRequest request, List<ServiceInput> listServiceInput) throws Exception {
        Map<String, String[]> requestmap = request.getParameterMap();
        for (ServiceInput serviceInput : listServiceInput) {
            if (StringUtils.isNotEmpty(serviceInput.getFixedValue())) {
                //有设置固定值
                serviceInput.setValue(serviceInput.getFixedValue());
                continue;
            }
            if (serviceInput.getScParamType().equals(OpenServiceConstants.SC_PARAMTYPE_HEAD)) {
                serviceInput.setValue(request.getHeader(serviceInput.getScName()));
            }
            if (requestmap != null) {
                for (Object key : requestmap.keySet()) {//循环请求所有参数
                    if (serviceInput.getName().equals(String.valueOf(key))) {
                        String value = String.valueOf(requestmap.get(key)[0]);
                        checkData(serviceInput.getType(), value, (String) key);
                        serviceInput.setValue(value);
                        break;
                    }
                }
            }
        }
    }

    private void checkData(String type, String value, String key) throws Exception {
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


    private String executeRPC(ServiceDef ws, List<ServiceInput> serviceInputList) throws AxisFault {
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

    private String executeAxis2(ServiceDef ws, List<ServiceInput> listServiceInput) throws AxisFault {
        try {
            String[] params = new String[listServiceInput.size()];
            int i = 0;
            for (ServiceInput serviceInput : listServiceInput) {
                checkData(serviceInput.getScType(), serviceInput.getValue(), serviceInput.getScName());
                String paramType = serviceInput.getScParamType();
                if (paramType.equalsIgnoreCase("body")) {
                    System.out.println(serviceInput.getScType() + ":" + serviceInput.getValue());
                    params[i++] = serviceInput.getName();
                }
            }

            String[] paramValues = new String[listServiceInput.size()];
            int j = 0;
            for (ServiceInput item : listServiceInput) {
                paramValues[j++] = item.getValue();
            }

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


    private String doRequest(String instream, ServiceDef serviceDef, List<ServiceInput> listServiceInput, ApiServiceMonitor apiServiceMonitor) throws Exception {
        String scType = null;
        String addr = serviceDef.getScAddr();
        addr = addr.endsWith("/") ? addr.substring(0, addr.length() - 1) : addr;
        StringBuilder httpurl = new StringBuilder();
        httpurl.append(addr);
        //组装后端参数param
        Map<String, String> header = new HashMap<>();
        Map<String, String> bodys = new HashMap<String, String>();
        JSONObject serviceInputParam = new JSONObject();
        header.put(Constants.HTTP_HEADER_ACCEPT, "*/*");
        Collections.sort(listServiceInput);//根据scSeq排序

        boolean firstQueryParam = true;
        for (ServiceInput serviceInput : listServiceInput) {
            serviceInputParam.put(serviceInput.getScName(), serviceInput.getValue());
            //判断必填属性
            if (OpenDataConstants.is_null_no == serviceInput.getRequired() && StringUtils.isBlank(serviceInput.getValue()) && !serviceInput.getScType().equals("text/xml") && !serviceInput.getScType().equals("application/json")) {
                throw new Exception("请传入必填参数" + serviceInput.getName());
            }
            String decryptedParam = decryptedParam(serviceInput, serviceDef.getEncryptionType());
            serviceInputParam.put(serviceInput.getScName(), decryptedParam);
            checkData(serviceInput.getScType(), decryptedParam, serviceInput.getScName());//判断参数类型是否正确
            String paramType = serviceInput.getScParamType();
            if (paramType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_PATH)) {
                httpurl.append("/").append(decryptedParam);
            } else if (paramType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_BODY)) {
                if (!serviceInput.getScType().equals(OpenServiceConstants.SC_TYPE_APPLICATION_JSON) && !serviceInput.getScType().equals(OpenServiceConstants.SC_TYPE_TEXT_XML)) {
                    if (StringUtils.isNotBlank(decryptedParam)) {
                        bodys.put(serviceInput.getScName(), decryptedParam);
                    }
                } else {
                    if (StringUtils.isEmpty(instream) && StringUtils.isNotBlank(decryptedParam)) {
                        instream = decryptedParam;
                    }
                    scType = serviceInput.getScType();
                }
            } else if (paramType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_QUERY)) {
                if (firstQueryParam) {
                    if (StringUtils.isNotBlank(decryptedParam)) {
                        httpurl.append("?").append(serviceInput.getScName()).append("=").append(URLEncoder.encode(decryptedParam));
                        firstQueryParam = false;
                    }
                } else {
                    if (StringUtils.isNotBlank(decryptedParam)) {
                        httpurl.append("&").append(serviceInput.getScName()).append("=").append(URLEncoder.encode(decryptedParam));
                    }
                }
            } else if (paramType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_HEAD)) {
                header.put(serviceInput.getScName(), decryptedParam);
            }
        }
        int timeout = 30000;
        String method = serviceDef.getScHttpMethod().toUpperCase();
        String result = null;
        apiServiceMonitor.setServiceInput(serviceInputParam.toString());
        apiServiceMonitor.setServiceInputHeader(JSONObject.fromObject(header).toString());
        apiServiceMonitor.setServiceMethod(method);
        switch (method) {
            case "GET":
                result = execGet(httpurl.toString(), timeout, header);
                break;
            case "POST":
                result = execPost(instream, scType, httpurl.toString(), timeout, header, bodys);
                break;
            default:
                result = "{error:404}";
        }
        apiServiceMonitor.setServiceOutput(result);
        //TODO:暂时将返回值全部加密，具体加密解密形式再继续探讨
        result = encryptionResult(result, serviceDef.getEncryptionType());
        return result;
    }

    /**
     * 解密参数 接口级
     *
     * @param encryptionType
     * @return
     * @throws Exception
     */
    private static String decryptedParam(ServiceInput serviceInput, String encryptionType) throws Exception {
        String decryptedParamStr = "";
        String param = serviceInput.getValue();
        String url = serviceInput.getValue();
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
        String param = serviceInput.getValue();
        String name = serviceInput.getName();

        String decryptType = serviceInput.getDecryptType();
        String encryptType = serviceInput.getEncryptType();
        String decryptUrl = serviceInput.getDecryptUrl();
        String encryptUrl = serviceInput.getEncryptUrl();

        // 执行解密
        if ( StringUtil.isNotEmpty(decryptType) || StringUtils.equals(decryptType,"")) {
            switch (encryptType) {
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
                    throw new Exception("暂不支持对所选加密方式解密，param = [" + param + "],encryptionType = [" + decryptType + "]");
            }
        }
        // 执行加密
        if ( StringUtil.isNotEmpty(encryptType) && !StringUtils.equals(encryptType,"") ) {
            switch (encryptType) {
                case ENCRYPT_MODE_NO:
                    after_value = param;
                    break;
                case ENCRYPT_MODE_KEY_BASE64:
                    after_value = encryptBASE64String(param);
                    break;
                case ENCRYPT_MODE_KEY_REST:
                    after_value = encryptRESTString(encryptUrl,name,param);
                    break;
                case ENCRYPT_MODE_KEY_SM3:
                    throw new Exception("暂不支持国密SM3加密，param = [" + param + "]");
                case ENCRYPT_MODE_KEY_MD5:
                    throw new Exception("暂不支持MD5加密，param = [" + param + "]");
                case ENCRYPT_MODE_KEY_SHA_1:
                    throw new Exception("暂不支持SHA-1加密，param = [" + param + "]");
                default:
                    throw new Exception("暂不支持对所选加密方式，param = [" + param + "],encryptionType = [" + decryptType + "]");
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
     * @param headers
     * @return
     */
    public static String execGet(String url, int timeout, Map<String, String> headers) {
        String str = "{error:404}";
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        if (StringUtil.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                httpGet.addHeader(key, headers.get(key));
            }
        }
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            str = "API网关POST method failed to access URL！";
            log.error("API网关GET method failed to access URL，URL：" + url, e);
        }
        return str;
    }

    /**
     * POST方法访问URL
     *
     * @param url
     * @param timeout
     * @param headers
     * @param parameters
     * @return
     */

    public static String execPost(String instream, String scType, String url, int timeout, Map<String, String> headers, Map<String, String> parameters) {
        String str = "{error:404}";
        HttpPost httpPost = new HttpPost(url);
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        if (StringUtil.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                httpPost.addHeader(key, headers.get(key));
            }
        }
        if (StringUtil.isNotEmpty(parameters)) {
            List<NameValuePair> nvps = new ArrayList<>();
            for (String key : parameters.keySet()) {
                nvps.add(new BasicNameValuePair(key, parameters.get(key)));
            }

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            } catch (Exception e) {
            }
        } else if (StringUtils.isNotEmpty(instream) && (scType.equals(OpenServiceConstants.SC_TYPE_APPLICATION_JSON) || scType.equals(OpenServiceConstants.SC_TYPE_TEXT_XML))) {
            switch (scType) {
                case OpenServiceConstants.SC_TYPE_APPLICATION_JSON:
                    httpPost.addHeader("Content-Type", OpenServiceConstants.content_type_json);
                    break;
                case OpenServiceConstants.SC_TYPE_TEXT_XML:
                    httpPost.addHeader("Content-Type", OpenServiceConstants.content_type_text_xml);
                    break;
            }
            try {
                httpPost.setEntity(new StringEntity(instream, "utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse response = httpclient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            str = "API网关POST method failed to access URL!";
            e.printStackTrace();
            log.error("API网关POST method failed to access URL，URL：" + url, e);
        }
        return str;
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

    public static void main(String[] args) throws Exception {
//        RPCServiceClient serviceClient = new RPCServiceClient();
//
//        EndpointReference targetEPR = new EndpointReference("http://222.74.69.242:8090/ws/service/hisForHzfwhMessage");
//        Options options = serviceClient.getOptions();
//
//        options.setTo(targetEPR);
//
//        options.setAction("http://itf.bussiness.jythis.com/interfaceAPC");
//        QName qname = new QName("http://itf.bussiness.jythis.com", "interfaceAPC");
//
//        Object[] parameters = new Object[]{"<req> \n" +
//                "<methodCode>getDoctorInfo001</methodCode> \n" +
//                "<methodParam> \n" +
//                "<hospitalId>747554124</hospitalId> \n" +
//                "<signature>80C428E098CE42258084F9C502AEA206</signature> \n" +
//                "<nonceStr>D3332E9938464648847CA0F450E9294A</nonceStr> \n" +
//                "<openId>Udffsdfsdfdsf81</openId> \n" +
//                "<deptId>8000001</deptId> \n" +
//                "<doctorId/> \n" +
//                "</methodParam> \n" +
//                "</req>"};
//
//            OMElement element = serviceClient.invokeBlocking(qname, parameters);
//
//        List result = getResults(element);
//
//        String str = JSON.toJSON(result).toString();
//        System.out.println("result" + str);


        // axis2 服务端
//        String url = "http://222.74.69.242:8090/founderWebs/services/ICalculateService";
//        // 使用RPC方式调用WebService
//        RPCServiceClient serviceClient = new RPCServiceClient();
//        EndpointReference targetEPR = new EndpointReference(url);
//        Options options = serviceClient.getOptions();
//
//        options.setTo(targetEPR);
//
////        options.setAction("urn:FounderRequestData");
//
//        QName qname = new QName("http://services.founder.com", "FounderRequestData");
//
//
//        Object[] parameters = new Object[] { "?","?","?","<![CDATA[<Request> <ServiceName>getDeptInfo001</ServiceName> <hospitalId>747554124</hospitalId> <signature>80C428E098CE42258084F9C502AEA206</signature> <nonceStr>D3332E9938464648847CA0F450E9294A</nonceStr> <openId>Udffsdfsdfdsf81</openId> <deptId>1001011</deptId> </Request>]]>" };
//        OMElement element = serviceClient.invokeBlocking(qname, parameters);
//        List result = getResults(element);
//        String str = JSON.toJSON(result).toString();
//        System.out.println("result" + str);

        String str = "<ns1:out xmlns:ns1=\"http://itf.bussiness.jythis.com\">&lt;?xml version=\"1.0\" encoding=\"utf-8\"?>&lt;res>&#xd;\n" +
                "&lt;returnCode>0&lt;/returnCode>&#xd;\n" +
                "&lt;returnMsg>成功&lt;/returnMsg>&#xd;\n" +
                "&lt;returnData>&lt;doctorInfo>&lt;doctorId>00001&lt;/doctorId>&lt;octorName>崔其福&lt;/octorName>&lt;branchId/>&lt;branchName/>&lt;deptClassId/>&lt;deptClassName/>&lt;deptId>8000001&lt;/deptId>&lt;deptName>崔其福院长办公室&lt;/deptName>&lt;doctorTitle>主任医师&lt;/doctorTitle>&lt;doctorRemark/>&lt;doctorGender>未知&lt;/doctorGender>&lt;betterFor/>&lt;urlPic/>&lt;visitTimeInfo/>&lt;doctorDesc/>&lt;/doctorInfo>&lt;/returnData>&#xd;\n" +
                "&lt;/res>&#xd;\n" +
                "</ns1:out>";
        String xml = str.replace("&lt;","<").replace("&#xd","").replace(";","").replace("</ns1:out>","");;

        Pattern pat = Pattern.compile("<ns1:out.*?>");
        Matcher mat = pat.matcher(xml);
        String result = mat.replaceAll("");
        System.out.println(result);


    }


}
