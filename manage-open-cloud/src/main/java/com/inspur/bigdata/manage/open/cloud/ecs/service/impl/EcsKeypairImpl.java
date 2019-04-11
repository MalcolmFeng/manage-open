package com.inspur.bigdata.manage.open.cloud.ecs.service.impl;

import com.inspur.bigdata.manage.open.cloud.ecs.service.IEcsKeypair;
import com.inspur.bigdata.manage.open.cloud.utils.Constants;
import com.inspur.bigdata.manage.open.cloud.utils.HttpRequestUtil;
import com.inspur.bigdata.manage.open.cloud.utils.PropertiesUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.inspur.bigdata.manage.utils.OpenDataConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ecsKeypair")
public class EcsKeypairImpl implements IEcsKeypair {

    private static final Log LOG = LogFactory.getLog(EcsKeypairImpl.class);


    public Map<String, String> createKey(Map<String, String> params) {
        LOG.debug("开始创建密钥对");
        Map<String, String> result = new HashMap<String, String>();

        params.put("user_name", OpenDataConstants.getUserId());

        try {
            HttpRequestUtil.doPutUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/createaccesskey", params, "UTF-8");
        } catch (Exception e) {
            LOG.error("创建密钥对失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "创建密钥对失败。");

            return result;
        }

        LOG.debug("创建密钥对成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "创建密钥对成功。");

        return result;
    }

    public String getKey(Map<String, String> params) {
        String response = "";

        JSONObject jsonObject = new JSONObject(params);
        jsonObject.put("user_name", OpenDataConstants.getUserId());
        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("data", jsonObject.toString());

        try {
            response = HttpRequestUtil.doPostUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/createaccesskey", params1, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public List<String> getKeys() {
        List<String> result = new ArrayList<String>();

        Map<String, String> params = new HashMap<String, String>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_name", OpenDataConstants.getUserId());
        params.put("data", jsonObject.toString());

        try {
            String response = HttpRequestUtil.doPostUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/createaccesskey", params, "UTF-8");
            if (response != null && response.length() != 0) {
                JSONObject obj = new JSONObject(response);
                JSONArray keyNames = obj.getJSONArray("response");
                for (int i = 0; i < keyNames.length(); i++) {
                    result.add(keyNames.getString(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Map<String, String> deleteKey(Map<String, String> params) {
        LOG.debug("开始删除密钥对");
        Map<String, String> result = new HashMap<String, String>();

        JSONObject jsonObject = new JSONObject(params);
        jsonObject.put("user_name", OpenDataConstants.getUserId());
        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("data", jsonObject.toString());

        try {
            HttpRequestUtil.doDeleteUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/createaccesskey", params1, "UTF-8");
        } catch (Exception e) {
            LOG.error("删除密钥对失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "删除密钥对失败。");

            return result;
        }

        LOG.debug("删除密钥对成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "删除密钥对成功。");

        return result;
    }

    public Map<String, String> deleteKeys(Map<String, String> params) {
        LOG.debug("开始批量删除密钥对");
        Map<String, String> result = new HashMap<String, String>();

        String[] keyNameArray = params.get("key_name").toString().split(",");

        try {
            for (int i = 0; i < keyNameArray.length; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_name", OpenDataConstants.getUserId());
                jsonObject.put("key_name", keyNameArray[i]);
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("data", jsonObject.toString());

                HttpRequestUtil.doDeleteUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/createaccesskey", params1, "UTF-8");
            }
        } catch (Exception e) {
            LOG.error("批量删除密钥对失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "批量删除密钥对失败。");

            return result;
        }

        LOG.debug("批量删除密钥对成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "批量删除密钥对成功。");

        return result;
    }

    public byte[] downloadPrivateKey() {
        Map<String, String> params = new HashMap<String, String>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_name", OpenDataConstants.getUserId());
        params.put("data", jsonObject.toString());

        try {
            String response = HttpRequestUtil.doPostUrlEncoded(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "ecs.domain") + "/resource/downloadprivatekey", params, "UTF-8");
            if (!response.equals("\"\"\n")) {
                return response.getBytes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}