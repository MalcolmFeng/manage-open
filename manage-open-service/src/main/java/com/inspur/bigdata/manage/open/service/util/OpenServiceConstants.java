package com.inspur.bigdata.manage.open.service.util;

import com.inspur.bigdata.manage.common.utils.PropertiesUtil;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.loushang.framework.util.HttpRequestUtils;
import sdk.security.authc.AuthenticationProvider;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huzhensheng on 2019/1/22.
 */
public class OpenServiceConstants {
    public static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 输入参数 scParamType 后端入参位置
     */
    public final static String SC_PARAMTYPE_BODY = "body";
    public final static String SC_PARAMTYPE_PATH = "path";
    public final static String SC_PARAMTYPE_HEAD = "head";
    public final static String SC_PARAMTYPE_QUERY = "query";

    /**
     *输入参数类型
     */
    public final static String SC_TYPE_STRING = "string";
    public final static String SC_TYPE_INT = "int";
    public final static String SC_TYPE_LONG = "long";
    public final static String SC_TYPE_DOUBLE = "double";
    public final static String SC_TYPE_BOOLEAN = "boolean";
    public final static String SC_TYPE_FLOAT = "float";
    public final static String SC_TYPE_APPLICATION_JSON = "application/json";
    public final static String SC_TYPE_TEXT_XML = "text/xml";
    public final static String SC_TYPE_APPLICATION_XWWWFORMURLENCODED = "application/x-www-form-urlencoded";

    /*
    ApiServiceMonitor表，api监控审计用到的错误代码等常量
     */
    public final static String ASM_SUCCESS = "200";

    //API分组错误
    public final static String ASM_ERROR_GROUP = "10001";

    //API服务不存在
    public final static String ASM_ERROR_SERVICE = "10002";

    //API服务当前状态不可用
    public final static String ASM_ERROR_SERVICE_NO_PASS = "10003";

    //查询授权应用异常
    public final static String ASM_ERROR_APP_UNAUTHORIZE = "10004";

    //API未授权应用
    public final static String ASM_ERROR_SERVICE_UNAUTHORIZE_APP = "10005";

    //验证签名不正确
    public final static String ASM_ERROR_SIGNATURE = "10006";

    //账户余额不足
    public final static String ASM_ERROR_BALANCE = "10007";

    //输入参数异常
    public final static String ASM_ERROR_PARAMETER = "10008";

    // ip在黑名单里
    public final static String ASM_ERROR_IP = "10009";

    // 超过限流值
    public final static String ASM_ERROR_LIMIT = "10010";


    //未知错误
    public final static String ASM_ERROR_UNKNOWN = "99999";


    public static boolean isSuperAdmin(String realm) {
        return masert_realm.equals(realm);
    }

    public static String getRealm() {
        String krbPrincipalName = AuthenticationProvider.getKrbPrincipalName();
        return krbPrincipalName.substring(krbPrincipalName.lastIndexOf("-") + 1);
//        return "master";
    }

    public static String getUserId() {
        String userId = AuthenticationProvider.getLoginUserId();
        return userId;
        //return "test2-realm1234";                                                 g
    }

    public static String api_create = "0";//api创建
    public static String api_submit_audit = "1";//api提交审核(发布)
    public static String api_audit_pass = "2";//审核api通过
    public static String api_audit_reject = "3";//审核api驳回
    public static String api_offline = "4";//api下线


    public static String auth_status_submit = "0";//代授权
    public static String auth_status_pass = "1";//授权通过
    public static String auth_status_reject = "2";//授权驳回

    public static String system_user = "@system";//通过系统推送数据的默认用户
    public static String masert_realm = "master";//管理员域名

    public static String auth_type_no = "0";//不需要授权
    public static String auth_type_yes = "1";//需要授权


    public static String content_type_json = "application/json;charset=utf-8";
    public static String content_type_text = "text/plain;charset=utf-8";
    public static String content_type_binary = "application/octet-stream;charset=utf-8";
    public static String content_type_xml = "application/xml;charset=utf-8";
    public static String content_type_text_xml = "text/xml;charset=utf-8";
    public static String content_type_html = "text/html;charset=utf-8";
    /**
     * 加密方式
     */
    public final static String ENCRYPT_MODE_NO = "0";
    public final static String ENCRYPT_MODE_KEY_BASE64 = "BASE64";
    public final static String ENCRYPT_MODE_KEY_MD5 = "MD5";
    public final static String ENCRYPT_MODE_KEY_SHA_1 = "SHA-1";
    public final static String ENCRYPT_MODE_KEY_SHA_256 = "SHA-256";
    public final static String ENCRYPT_MODE_KEY_SHA_384 = "SHA-384";
    public final static String ENCRYPT_MODE_KEY_SHA_512 = "SHA-512";
    public final static String ENCRYPT_MODE_KEY_SM3 = "SM3";

    public static final Map<String, String> ENCRYPTION_MAP;

    static {
        Map aMap = new HashMap();
        aMap.put(ENCRYPT_MODE_NO, "不加密");
        aMap.put(ENCRYPT_MODE_KEY_BASE64, ENCRYPT_MODE_KEY_BASE64);
        aMap.put(ENCRYPT_MODE_KEY_MD5, ENCRYPT_MODE_KEY_MD5);
        aMap.put(ENCRYPT_MODE_KEY_SHA_1, ENCRYPT_MODE_KEY_SHA_1);
        aMap.put(ENCRYPT_MODE_KEY_SM3, ENCRYPT_MODE_KEY_SM3);
        ENCRYPTION_MAP = Collections.unmodifiableMap(aMap);
    }
    public static JSONArray getRemoteApiList(String userId) {
        String url = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "od.domain") +
                "/service/rest/service/getServiceListByUser?userId=" + userId;
        String resultStr = HttpRequestUtils.get(url);
        return JSONArray.fromObject(resultStr);
    }

    public static JSONObject getRemoteApiDetail(String id) {
        String apiHost = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "od.domain");
        String url = apiHost +
                "/service/rest/service/getServiceDetail?serviceId=" + id;
        String resultStr = HttpRequestUtils.get(url);
//        String resultStr= "{\"inputSample\":\"value_name_1:value1\",\"requestAddr\":\"/api/agent/mysqlService241915/1.1\",\"requestType\":\"http\",\"inputParam\":[{\"name\":\"param_1\",\"description\":\"des_1\",\"type\":\"string\",\"required\":\"0\",\"seq\":0},{\"name\":\"param_2\",\"description\":\"des_2\",\"id\":\"\",\"type\":\"string\",\"required\":\"1\",\"seq\":1}],\"returnSample\":\"value_name_1:value1\",\"serviceId\":\"123\",\"serviceName\":\"service1\",\"outputParam\":[{\"name\":\"value_name_1\",\"description\":\"1\",\"seq\":0},{\"name\":\"value_name_2\",\"description\":\"2\",\"id\":\"\",\"seq\":1}],\"version\":\"1.0\",\"desc\":\"服务描述\"}";
        JSONObject json = JSONObject.fromObject(resultStr);
        String requestAddr = json.getString("requestAddr");//拼装地址
        json.put("requestAddr", apiHost + "/service" + requestAddr);
        return json;
    }

    public static String getContentType(String type) {
        switch (type.toLowerCase()) {
            case "json":
                return content_type_json;
            case "text":
                return content_type_text;
            case "binary":
                return content_type_binary;
            case "xml":
                return content_type_xml;
            case "html":
                return content_type_html;
            default:
                return "*/*";
        }
    }

    /**
     * 组装开发者访问api实际路径
     *
     * @param apiContext
     * @param reqPath
     * @return
     */
    public static String getOpenAddr(String apiContext, String reqPath) {
        String url = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "api.domain") +
                "/" + apiContext + reqPath;
        return url;
    }
}
