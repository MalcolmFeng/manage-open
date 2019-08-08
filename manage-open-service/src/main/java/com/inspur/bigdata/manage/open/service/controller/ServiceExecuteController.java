package com.inspur.bigdata.manage.open.service.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.inspur.bigdata.manage.open.service.data.*;
import com.inspur.bigdata.manage.open.service.pay.data.PayAccountCapital;
import com.inspur.bigdata.manage.open.service.pay.service.IPayService;
import com.inspur.bigdata.manage.open.service.service.*;
import com.inspur.bigdata.manage.open.service.util.ApiServiceMonitorUtil;
import com.inspur.bigdata.manage.open.service.util.OpenServiceConstants;
import com.inspur.bigdata.manage.open.service.util.sign.*;
import com.inspur.bigdata.manage.open.service.util.signconstants.Constants;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import com.inspur.bigdata.manage.utils.StringUtil;
import net.sf.json.JSONObject;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import static com.inspur.bigdata.manage.open.service.util.OpenServiceConstants.*;


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
        String requestIp=ApiServiceMonitorUtil.getClientIp(request);
        String requestTime = OpenServiceConstants.sf.format(new Date());
        String responseTime = null;
        ApiServiceMonitor apiServiceMonitor = new ApiServiceMonitor();
        apiServiceMonitor.setCallerIp(requestIp);
        apiServiceMonitor.setRequestTime(requestTime);
        apiServiceMonitor.setOpenServiceInput(JSONObject.fromObject(request.getParameterMap()).toString());
        apiServiceMonitor.setOpenServiceMethod(request.getMethod());
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
            JSONObject json = JSONObject.fromObject(headers);
            apiServiceMonitor.setOpenServiceInputHeader(json.toString());
            //通过context,reqPath关联查询apiService
            ServiceDef serviceDef = checkApiService(apiContext, reqPath, apiServiceMonitor);
            if (serviceDef == null) {
                success =false;
                writer.print("API分组错误或API服务错误");
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
            String result_str = doRequest(instream, serviceDef, listServiceInput, apiServiceMonitor);
            response.addHeader("Content-Type", OpenServiceConstants.getContentType(serviceDef.getContentType()));
            writer.print(result_str);
            writer.flush();
            apiServiceMonitor.setOpenServiceOutput(result_str);
            apiServiceMonitor.setResult("200");
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
            responseTime = OpenServiceConstants.sf.format(new Date());
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
            apiServiceMonitor.setCreateTime(OpenServiceConstants.sf.format(new Date()));
//            monitorService.insert(apiServiceMonitor);
            ApiServiceMonitorUtil.insert(monitorService, apiServiceMonitor);
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
            apiServiceMonitor.setNotes("API分组错误或API服务错误,context_path=[" + context_path + "]");
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
            checkData(serviceInput.getScType(), serviceInput.getValue(), serviceInput.getScName());//判断参数类型是否正确
            String paramType = serviceInput.getScParamType();
            if (paramType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_PATH)) {
                httpurl.append("/").append(serviceInput.getValue());
            } else if (paramType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_BODY)) {
                if (!serviceInput.getScType().equals(OpenServiceConstants.SC_TYPE_APPLICATION_JSON) && !serviceInput.getScType().equals(OpenServiceConstants.SC_TYPE_TEXT_XML)) {
                    if (StringUtils.isNotBlank(serviceInput.getValue())) {
                        bodys.put(serviceInput.getScName(), serviceInput.getValue());
                    }
                } else {
                    if (StringUtils.isEmpty(instream) && StringUtils.isNotBlank(serviceInput.getValue())) {
                        instream = serviceInput.getValue();
                    }
                    scType = serviceInput.getScType();
                }
            } else if (paramType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_QUERY)) {
                if (firstQueryParam) {
                    if (StringUtils.isNotBlank(serviceInput.getValue())) {
                        httpurl.append("?").append(serviceInput.getScName()).append("=").append(URLEncoder.encode(serviceInput.getValue()));
                        firstQueryParam = false;
                    }
                } else {
                    if (StringUtils.isNotBlank(serviceInput.getValue())) {
                        httpurl.append("&").append(serviceInput.getScName()).append("=").append(URLEncoder.encode(serviceInput.getValue()));
                    }
                }
            } else if (paramType.equalsIgnoreCase(OpenServiceConstants.SC_PARAMTYPE_HEAD)) {
                header.put(serviceInput.getScName(), serviceInput.getValue());
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
        return result;
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
            str = "API网关POST method failed to access URL，URL：" + url + "," + e.getMessage();
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
            str = "API网关POST method failed to access URL，URL：" + url + "," + e.getMessage();
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
}
