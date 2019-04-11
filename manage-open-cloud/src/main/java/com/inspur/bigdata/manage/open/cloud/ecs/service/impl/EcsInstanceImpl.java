package com.inspur.bigdata.manage.open.cloud.ecs.service.impl;

import com.inspur.bigdata.manage.open.cloud.ecs.service.IEcsInstance;
import com.inspur.bigdata.manage.open.cloud.utils.Constants;
import com.inspur.bigdata.manage.open.cloud.utils.HttpRequestUtil;
import com.inspur.bigdata.manage.open.cloud.utils.PropertiesUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.inspur.bigdata.manage.utils.OpenDataConstants;

import java.util.HashMap;
import java.util.Map;

@Service("ecsInstance")
public class EcsInstanceImpl implements IEcsInstance {

    private static final Log LOG = LogFactory.getLog(EcsInstanceImpl.class);

    public String checkEcsName(Map<String, Object> params) {
        String response = "";

        params.put("user_id", OpenDataConstants.getUserId());

        try {
            response = HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/cloudplatform/ecsvmnamecheckbefore", params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Map<String, String> applyInstance(Map<String, Object> params) {
        LOG.debug("开始创建云服务器实例");
        Map<String, String> result = new HashMap<String, String>();

        params.put("user_id", OpenDataConstants.getUserId());

        try {
            HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/cloudplatform/ecsapply", params, "UTF-8");
        } catch (Exception e) {
            LOG.error("创建云服务器实例失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "创建云服务器实例失败。");

            return result;
        }

        LOG.debug("创建云服务器实例成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "创建云服务器实例实例成功。");

        return result;
    }

    public String getEcsInstancesByUser() {
        String response = "";
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("user_id", OpenDataConstants.getUserId());

        try {
            response = HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/cloudplatform/ecsapplysearch", params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getEcsInstanceByAuditor() {
        String response = "";
        Map<String, String> params = new HashMap<String, String>();

        try {
            response = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/cloudplatform/ecsauditsearch", params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Map<String, String> passInstance(Map<String, Object> params) {
        LOG.debug("开始通过云服务器实例");
        Map<String, String> result = new HashMap<String, String>();

        result.put("status", Constants.RESULT_FAILED);
        result.put("message", "通过云服务器实例失败。");

        String response = "";
        try {
            response = HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/cloudplatform/ecsaudit", params, "UTF-8");
        } catch (Exception e) {
            LOG.error("通过云服务器实例失败:" + e);

            return result;
        }

        JSONObject obj = new JSONObject(response);
        String result1 = obj.getString("result");

        if (!result1.equals("success")) {
            LOG.error("通过云服务器实例失败");

            return result;
        }

        LOG.debug("通过云服务器实例成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "通过云服务器实例实例成功。");

        return result;
    }

    public Map<String, String> rejectInstance(Map<String, Object> params) {
        LOG.debug("开始驳回云服务器实例");
        Map<String, String> result = new HashMap<String, String>();

        try {
            HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/cloudplatform/ecsaudit", params, "UTF-8");
        } catch (Exception e) {
            LOG.error("驳回云服务器实例失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "驳回云服务器实例失败。");

            return result;
        }

        LOG.debug("驳回云服务器实例成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "驳回云服务器实例实例成功。");

        return result;
    }

    public Map<String, String> powerOperation(Map<String, Object> params) {
        LOG.debug("开始进行主机电源操作");
        Map<String, String> result = new HashMap<String, String>();

        JSONObject obj = new JSONObject();
        obj.put("vms", params.get("instance_name"));
        obj.put("operation", params.get("operation"));
        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("data", obj.toString());

        try {
            HttpRequestUtil.doPostUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/poweroperation", params1, "UTF-8");
        } catch (Exception e) {
            LOG.error("主机电源操作失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "主机电源操作失败。");

            return result;
        }

        LOG.debug("主机电源操作成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "主机电源操作成功。");

        return result;
    }

    public Map<String, String> applyChangeFormat(Map<String, Object> params) {
        LOG.debug("开始申请变更规格");
        Map<String, String> result = new HashMap<String, String>();

        try {
            HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/cloudplatform/ecschangeformatapply", params, "UTF-8");
        } catch (Exception e) {
            LOG.error("申请变更规格失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "申请变更规格失败。");

            return result;
        }

        LOG.debug("申请变更规格成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "申请变更规格成功。");

        return result;
    }

    public Map<String, String> auditChangeFormat(Map<String, Object> params) {
        LOG.debug("开始审核变更规格");
        Map<String, String> result = new HashMap<String, String>();

        try {
            HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/cloudplatform/ecschangeformataudit", params, "UTF-8");
        } catch (Exception e) {
            LOG.error("审核变更规格失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "审核变更规格失败。");

            return result;
        }

        LOG.debug("审核变更规格成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "审核变更规格成功。");

        return result;
    }

    public Map<String, String> resetPwd(Map<String, Object> params) {
        LOG.debug("开始修改密码");
        Map<String, String> result = new HashMap<String, String>();

        try {
            HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/changepassword", params, "UTF-8");
        } catch (Exception e) {
            LOG.error("修改密码失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "修改密码失败。");

            return result;
        }

        LOG.debug("修改密码成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "修改密码成功。");

        return result;
    }

    public Map<String, String> modifyKey(Map<String, Object> params) {
        LOG.debug("开始修改密钥");
        Map<String, String> result = new HashMap<String, String>();

        params.put("user_id", OpenDataConstants.getUserId());

        try {
            HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/changekeypair", params, "UTF-8");
        } catch (Exception e) {
            LOG.error("修改密钥失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "修改密钥失败。");

            return result;
        }

        LOG.debug("修改密钥成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "修改密钥成功。");

        return result;
    }

    public Map<String, String> deleteInstance(Map<String, Object> params) {
        LOG.debug("开始删除主机");
        Map<String, String> result = new HashMap<String, String>();

        JSONArray array = new JSONArray();
        array.put(params.get("instance_name"));
        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("data", array.toString());

        try {
            HttpRequestUtil.doPostUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/deletevms", params1, "UTF-8");
        } catch (Exception e) {
            LOG.error("删除主机失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "删除主机失败。");

            return result;
        }

        LOG.debug("删除主机成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "删除主机成功。");

        return result;
    }
}
