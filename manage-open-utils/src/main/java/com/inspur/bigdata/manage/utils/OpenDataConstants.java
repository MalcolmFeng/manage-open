package com.inspur.bigdata.manage.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sdk.security.authc.AuthenticationProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huzhensheng on 2019/1/22.
 */
public class OpenDataConstants {
    public static SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 项目配置文件
    public static String CONF_PROPERTIES = "conf.properties";
    public static boolean isSuperAdmin(String realm){
        if(masert_realm.equals(realm)){
            return true;
        }
        return false;
    }
    public static String getRealm() {
		String krbPrincipalName = AuthenticationProvider.getKrbPrincipalName();
		String realm=krbPrincipalName.substring(krbPrincipalName.lastIndexOf("-") + 1);
     //String realm="realm1234";
        return realm;
    }
    public static String getUserId() {
		String userId = AuthenticationProvider.getKrbPrincipalName();
        return userId;
        //return "test1-realm1234";
    }
    public static String data_create="0";//数据创建
    public static String data_submit_audit="1";//提交审核
    public static String data_audit_pass="2";//审核通过
    public static String data_audit_reject="3";//审核驳回
    public static String data_audit_offline="4";//审核下线

    public static int NEED_USER_AUTH_NO=0;
    public static int NEED_USER_AUTH_YES=1;

    public static int is_null_yes=0;//允许为空
    public static int is_null_no=1;//不允许为空

    public static String auth_status_submit="0";//代授权
    public static String auth_status_pass="1";//授权通过
    public static String auth_status_reject="2";//授权驳回

    public static String system_user="@system";//通过系统推送数据的默认用户
    public static String masert_realm="master";//管理员域名


    public static String select = "\"select\"";
    public static String update = "\"update\"";
    public static String create = "\"create\"";
    public static String drop = "\"drop\"";
    public static String alter = "\"alter\"";

    public static List<JSONObject> getJsonParse(String resultStr) {
        JSONArray jsonArray = JSONArray.fromObject(resultStr);
        List<JSONObject> list = new ArrayList<>();
        for(Object str:jsonArray){
            JSONObject jsonObject = JSONObject.fromObject(str);
            list.add(jsonObject);
        }
        return list;
    }
}
