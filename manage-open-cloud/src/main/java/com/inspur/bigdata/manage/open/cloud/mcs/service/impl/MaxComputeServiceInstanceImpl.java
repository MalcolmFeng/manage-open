package com.inspur.bigdata.manage.open.cloud.mcs.service.impl;

import com.inspur.bigdata.manage.open.cloud.mcs.dao.MaxComputeServiceInstanceMapper;
import com.inspur.bigdata.manage.open.cloud.mcs.data.MaxComputeServiceInstance;
import com.inspur.bigdata.manage.open.cloud.mcs.service.IMaxComputeServiceInstance;
import com.inspur.bigdata.manage.open.cloud.utils.*;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("maxComputeServiceInstance")
public class MaxComputeServiceInstanceImpl implements IMaxComputeServiceInstance {

    private static final Log LOG = LogFactory.getLog(MaxComputeServiceInstanceImpl.class);

    private static ExecutorService executor = Executors.newCachedThreadPool();

	@Autowired
	private MaxComputeServiceInstanceMapper maxComputeServiceInstanceMapper;

	public List<MaxComputeServiceInstance> getMaxComputeServiceInstances(Map<String, String> params) {
		return maxComputeServiceInstanceMapper.getMaxComputeServiceInstances(params);
	}

	public List<MaxComputeServiceInstance> getMaxComputeServiceInstancesOrderByApplyTimeDesc() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", OpenDataConstants.getUserId());

		return maxComputeServiceInstanceMapper.getMaxComputeServiceInstancesOrderByApplyTimeDesc(params);
	}

	public void saveMaxComputeServiceInstance(MaxComputeServiceInstance maxComputeServiceInstance) {
		maxComputeServiceInstanceMapper.saveMaxComputeServiceInstance(maxComputeServiceInstance);
	}

	public MaxComputeServiceInstance getMaxComputeServiceInstanceByInstanceId(String instanceId) {
		return maxComputeServiceInstanceMapper.getMaxComputeServiceInstanceByInstanceId(instanceId);
	}

	public void updateMaxComputeServiceInstance(MaxComputeServiceInstance maxComputeServiceInstance) {
		maxComputeServiceInstanceMapper.updateMaxComputeServiceInstance(maxComputeServiceInstance);
	}

	public Map<String, String> applyInstance(Map<String, Object> params) {
		Map<String, String> result = new HashMap<String, String>();

		String instanceId = UUID.randomUUID().toString().replace("-", "");
		String instanceName = params.get("instanceName").toString();
		String serviceVersionTemp = params.get("serviceVersion").toString();
		String serviceId = serviceVersionTemp.split("-")[0];
		String serviceVersion = serviceVersionTemp.substring(serviceVersionTemp.indexOf("-") + 1);
		Integer coreNum = Integer.valueOf(params.get("coreNum").toString().split(" ")[0]);
		Integer  memory = Integer.valueOf(params.get("memory").toString().split(" ")[0]);
		Integer storageVolume = Integer.valueOf(params.get("storageVolume").toString());
		String applyTime = DateUtil.getCurrentTime2();

		LOG.debug("开始申请大数据计算服务实例");

		MaxComputeServiceInstance mcs = new MaxComputeServiceInstance();
		mcs.setInstanceId(instanceId);
		mcs.setInstanceName(instanceName);
		mcs.setUserId(OpenDataConstants.getUserId());
		mcs.setServiceId(serviceId);
		mcs.setServiceVersion(serviceVersion);
		mcs.setCoreNum(coreNum);
		mcs.setMemory(memory);
		mcs.setStorageVolume(storageVolume);
		mcs.setApplyTime(applyTime);
		mcs.setApplyStatus(Constants.APPLY_STATUS_INIT);
		mcs.setUpdateTime(DateUtil.getCurrentTime2());
		mcs.setRunStatus("6");

		try {
			saveMaxComputeServiceInstance(mcs);
		} catch (Exception e) {
			LOG.error("申请大数据计算服务实例失败:" + e);

			result.put("status", Constants.RESULT_FAILED);
			result.put("message", "申请大数据计算服务实例失败。");

			return result;
		}

		LOG.debug("申请大数据计算服务实例成功");

		result.put("status", Constants.RESULT_SUCCEED);
		result.put("message", "申请大数据计算服务实例成功。");

		return result;
	}

    public Map<String, String> passInstance(Map<String, Object> params) {
	    Map<String, String> result = new HashMap<String, String>();

        LOG.debug("开始通过大数据计算服务实例");

        try {
            String instanceId = params.get("instance_id").toString();
            String auditOpinion = params.get("audit_opinion").toString();
            String adminName = OpenDataConstants.getUserId();
            MaxComputeServiceInstance instance = getMaxComputeServiceInstanceByInstanceId(instanceId);
            instance.setAuditOpinion(auditOpinion);
            instance.setReplyTime(DateUtil.getCurrentTime2());
            instance.setApplyStatus(Constants.APPLY_STATUS_PASS);
            instance.setRunStatus(Constants.MCS_INSTANCE_DEPLOYING);
            instance.setUpdateTime(DateUtil.getCurrentTime2());
            updateMaxComputeServiceInstance(instance);

           // createInstance(instance, adminName);

            result = checkCapacity(instance);
            if (result.get("status").equals(Constants.RESULT_SUCCEED)) {
                createInstance(instance,adminName);
            } else {
                createInstanceError(instance);
                return result;
            }

        } catch (Exception e) {
            LOG.debug("通过大数据计算服务实例失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "通过大数据计算服务实例失败。");
        }

        LOG.debug("通过大数据计算服务实例成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "通过大数据计算服务实例成功。");

        return result;
    }

    public Map<String, String> passInstances(Map<String, Object> params) {
        Map<String, String> result = new HashMap<String, String>();

        LOG.debug("开始批量通过大数据计算服务实例");

        String[] instanceIdArray = params.get("instance_id").toString().split(",");
        String adminName = OpenDataConstants.getUserId();

        try {
            for (int i = 0; i < instanceIdArray.length; i++) {
                String instanceId = instanceIdArray[i];
                MaxComputeServiceInstance instance = getMaxComputeServiceInstanceByInstanceId(instanceId);

                if (instance.getApplyStatus().equals(Constants.APPLY_STATUS_INIT)) {
                    instance.setReplyTime(DateUtil.getCurrentTime2());
                    instance.setApplyStatus(Constants.APPLY_STATUS_PASS);
                    instance.setRunStatus(Constants.MCS_INSTANCE_DEPLOYING);
                    instance.setUpdateTime(DateUtil.getCurrentTime2());
                    updateMaxComputeServiceInstance(instance);

                    createInstance(instance, adminName);
                }
            }
        } catch (Exception e) {
            LOG.debug("批量通过大数据计算服务实例失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "批量通过大数据计算服务实例失败。");
        }

        LOG.debug("批量通过大数据计算服务实例成功");

        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "批量通过大数据计算服务实例成功。");

        return result;
	}

    public Map<String, String> rejectInstance(Map<String, Object> params) {
	    Map<String, String> result = new HashMap<String, String>();

        LOG.debug("开始驳回大数据计算服务实例");

        String instanceId = params.get("instance_id").toString();
        String auditOpinion = params.get("audit_opinion").toString();

        try {
            MaxComputeServiceInstance instance = getMaxComputeServiceInstanceByInstanceId(instanceId);
            instance.setAuditOpinion(auditOpinion);
            instance.setReplyTime(DateUtil.getCurrentTime2());
            instance.setApplyStatus(Constants.APPLY_STATUS_REJECT);
            instance.setUpdateTime(DateUtil.getCurrentTime2());
            updateMaxComputeServiceInstance(instance);
        } catch (Exception e) {
            LOG.error("驳回大数据计算服务实例失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "驳回大数据计算服务实例失败。");

            return result;
        }

        LOG.debug("驳回大数据计算服务实例成功");
        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "驳回大数据计算服务实例成功。");

        return result;
    }

    public Map<String, String> rejectInstances(Map<String, Object> params) {
        Map<String, String> result = new HashMap<String, String>();

        LOG.debug("开始批量驳回大数据计算服务实例");

        String[] instanceIdArray = params.get("instance_id").toString().split(",");

        try {
            for (int i = 0; i < instanceIdArray.length; i++) {
                MaxComputeServiceInstance instance = getMaxComputeServiceInstanceByInstanceId(instanceIdArray[i]);

                if (instance.getApplyStatus().equals(Constants.APPLY_STATUS_INIT)) {
                    instance.setReplyTime(DateUtil.getCurrentTime2());
                    instance.setApplyStatus(Constants.APPLY_STATUS_REJECT);
                    instance.setUpdateTime(DateUtil.getCurrentTime2());
                    updateMaxComputeServiceInstance(instance);
                }
            }
        } catch (Exception e) {
            LOG.error("批量驳回大数据计算服务实例失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "批量驳回大数据计算服务实例失败。");

            return result;
        }

        LOG.debug("批量驳回大数据计算服务实例成功");
        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "批量驳回大数据计算服务实例成功。");

        return result;
    }

    // TODO 向接口发送删除请求
    public Map<String, String> deleteInstance(Map<String, Object> params) {
        Map<String, String> result = new HashMap<String, String>();

        LOG.debug("开始删除大数据计算服务实例");

        String instanceId = params.get("instance_id").toString();
        MaxComputeServiceInstance instance = getMaxComputeServiceInstanceByInstanceId(instanceId);
        String adminName = OpenDataConstants.getUserId();

        try {
            String preStatus = instance.getRunStatus();
            instance.setRunStatus(Constants.MCS_INSTANCE_DELETING);
            instance.setUpdateTime(DateUtil.getCurrentTime2());
            updateMaxComputeServiceInstance(instance);

            if (Constants.MCS_INSTANCE_RUNNING.equals(preStatus)) {
                deleteInstance(instance, adminName);
            }
        } catch (Exception e) {
            LOG.error("删除大数据计算服务实例失败:" + e);

            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "删除大数据计算服务实例失败。");

            return result;
        }

        LOG.debug("删除大数据计算服务实例成功");
        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "删除大数据计算服务实例成功。");

        return result;
    }

    public Map<String, String> deleteInstances(Map<String, Object> params) {
        Map<String, String> result = new HashMap<String, String>();

        LOG.debug("开始批量删除大数据计算服务实例");

        String[] instanceIdArray = params.get("instance_id").toString().split(",");
        String adminName = OpenDataConstants.getUserId();

        for (int i = 0; i < instanceIdArray.length; i++) {
            String instanceId = instanceIdArray[i];
            MaxComputeServiceInstance instance = getMaxComputeServiceInstanceByInstanceId(instanceId);

            try {
                String preStatus = instance.getRunStatus();
                instance.setRunStatus(Constants.MCS_INSTANCE_DELETING);
                instance.setUpdateTime(DateUtil.getCurrentTime2());
                updateMaxComputeServiceInstance(instance);

                if (Constants.MCS_INSTANCE_RUNNING.equals(preStatus)) {
                    deleteInstance(instance, adminName);
                }
            } catch (Exception e) {
                LOG.error("批量删除大数据计算服务实例失败:" + e);

                result.put("status", Constants.RESULT_FAILED);
                result.put("message", "批量删除大数据计算服务实例失败。");

                return result;
            }
        }

        LOG.debug("批量删除大数据计算服务实例成功");
        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "批量删除大数据计算服务实例成功。");

        return result;
    }


    // 私有方法，用于异步创建大数据服务实例
    private void createInstance(final MaxComputeServiceInstance instance, final String adminName) {
        executor.execute(new Runnable() {
            public void run() {
                LOG.debug("开始创建大数据计算服务实例");

                String keycloak_file_content = FileReader.getFile(Constants.KEYCLOAK_JSON);
                JSONObject keycloak_json = new JSONObject(keycloak_file_content);
                String keycloak_auth_server_url = keycloak_json.getString("auth-server-url");
                StringBuffer post_url = new StringBuffer();
                post_url.append(keycloak_auth_server_url).append("/realms/master/protocol/openid-connect/token");

                Map<String, String> params_temp1 = new HashMap<String, String>();
                params_temp1.put("grant_type", "password");
                params_temp1.put("username", "superadmin");
                params_temp1.put("password", "superadmin");
                params_temp1.put("client_id", "manage-cluster");

                try {
                    String result1 = HttpRequestUtil.doPostUrlEncoded(post_url.toString(), params_temp1, "utf-8");
                    if (null != result1 && !"".equals(result1)) {
                        JSONObject response1 = new JSONObject(result1);
                        String token = response1.getString("access_token");

                        Map<String, Object> params_temp2 = new HashMap<String, Object>();
                        params_temp2.put("user_name", instance.getUserId().split("-")[0]);
                        //获取当前管理员登录账号
                        params_temp2.put("admin_name", adminName);
                        params_temp2.put("realm", instance.getUserId().split("-")[1]);
                        params_temp2.put("token", token);
                        params_temp2.put("instance_id", instance.getInstanceId());
                        params_temp2.put("memory", instance.getMemory());
                        params_temp2.put("core_num", instance.getCoreNum());
                        params_temp2.put("storage_volume", instance.getStorageVolume());
                        params_temp2.put("od_domain", PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "od.domain"));

                        String result2 = HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/manage-cluster/service/indata/component/instanceWithTenantUser", params_temp2, "utf-8");
                        if (null != result2 && !"".equals(result2)) {
                            JSONObject response2 = new JSONObject(result2);
                            String serverUrl = response2.getJSONObject("data").getString("serviceUrl");
                            String instanceUserName = response2.getJSONObject("data").getString("instanceUserName");
                            String instanceUserPwd = response2.getJSONObject("data").getString("instanceUserPwd");
                            instance.setServiceAddress(serverUrl);
                            instance.setServiceUsername(instanceUserName);
                            instance.setServicePasswd(instanceUserPwd);
                            instance.setCreateTime(DateUtil.getCurrentTime2());
                            instance.setRunStatus(Constants.MCS_INSTANCE_RUNNING);
                            instance.setUpdateTime(DateUtil.getCurrentTime2());
                            updateMaxComputeServiceInstance(instance);

                            LOG.debug("创建大数据计算服务实例成功");
                            return;
                        }
                    }
                    LOG.error("创建大数据计算服务实例失败");
                    createInstanceError(instance);
                } catch (Exception e) {
                    LOG.error("创建大数据计算服务实例失败:" + e.getMessage());
                    createInstanceError(instance);
                }
            }
        });
    }

    // 私有方法，用于将实例状态设置为创建失败
    private void createInstanceError(MaxComputeServiceInstance instance) {
        instance.setRunStatus(Constants.MCS_INSTANCE_DEPLOY_FAILED);
        instance.setUpdateTime(DateUtil.getCurrentTime2());
        updateMaxComputeServiceInstance(instance);
    }

    // 私有方法，用于将实例状态设置为删除失败
    private void deleteInstanceError(MaxComputeServiceInstance instance) {
        instance.setRunStatus(Constants.MCS_INSTANCE_DELETE_FAILED);
        instance.setUpdateTime(DateUtil.getCurrentTime2());
        updateMaxComputeServiceInstance(instance);
    }
    public Map<String, Object> getStorageVolume(String instanceId, String userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("spaceQuota", 0);
        result.put("spaceUsed", 0);
        result.put("useRate", 0);

        try {
            String clusterResponse = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/manage-cluster/service/indata/cluster/clusterInfo/"
                    + userId.split("-")[1], new HashMap<>(), "UTF-8");
            String clusterId = new JSONObject(clusterResponse).getString("clusterId");

            Map<String, String> params1 = new HashMap<>();
            params1.put("clusterId", clusterId);
            params1.put("path", "/dev/" + userId.split("-")[0] + "/" + instanceId.substring(0, 4));
            params1.put("userId", userId);
            String quotaResponse = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/manage-store/service/hdfs/rest/getQuota",
                    params1, "UTF-8");
            Integer spaceQuota = new JSONObject(quotaResponse).getInt("spaceQuota");

            String spaceUsedResponse = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/manage-store/service/hdfs/rest/getSpaceAndFileCountUsed",
                    params1, "UTF-8");
            Float spaceUsed = new BigDecimal(new JSONObject(spaceUsedResponse).getLong("space")).divide(new BigDecimal(3L * 1024 * 1024 * 1024), 2, BigDecimal.ROUND_HALF_UP).floatValue();
            Float useRate = new BigDecimal(spaceUsed).divide(new BigDecimal(spaceQuota), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).floatValue();

            result.put("spaceQuota", spaceQuota);
            result.put("spaceUsed", spaceUsed);
            result.put("useRate", useRate);
        } catch (Exception e) {
            LOG.debug("获取存储容量错误:" + e);
        }

        return result;
    }
    private void deleteInstance(final MaxComputeServiceInstance instance, final String adminName) {
        executor.execute(new Runnable() {
            public void run() {
                LOG.debug("开始删除大数据计算服务实例");

                try {
                    String keycloak_file_content = FileReader.getFile(Constants.KEYCLOAK_JSON);
                    JSONObject keycloak_json = new JSONObject(keycloak_file_content);
                    String keycloak_auth_server_url = keycloak_json.getString("auth-server-url");
                    StringBuffer post_url = new StringBuffer();
                    post_url.append(keycloak_auth_server_url).append("/realms/master/protocol/openid-connect/token");

                    Map<String, String> params_temp1 = new HashMap<String, String>();
                    params_temp1.put("grant_type", "password");
                    params_temp1.put("username", "superadmin");
                    params_temp1.put("password", "superadmin");
                    params_temp1.put("client_id", "manage-cluster");

                    String result1 = HttpRequestUtil.doPostUrlEncoded(post_url.toString(), params_temp1, "utf-8");
                    JSONObject response1 = new JSONObject(result1);
                    String token = response1.getString("access_token");

                    Map<String, Object> param = new HashMap<>();
                    param.put("realm", instance.getUserId().split("-")[1]);
                    param.put("masterUser", adminName);
                    param.put("instanceId", instance.getInstanceId());
                    param.put("token", token);

                    String deleteResponse = HttpRequestUtil.doPostJson(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain")
                            + "/manage-cluster/service/indata/component/delete/tenant/instance", param, "UTF-8");

                    if (!new JSONObject(deleteResponse).getString("result").equals("true")) {
                        LOG.error("删除大数据计算服务实例失败");
                        deleteInstanceError(instance);
                    }
                } catch (Exception e) {
                    LOG.error("删除大数据计算服务实例失败");
                    deleteInstanceError(instance);
                }
            }
        });
    }

    private Map<String, String> checkCapacity(MaxComputeServiceInstance instance) {
        Map<String, String> result = new HashMap<>();
        result.put("status", Constants.RESULT_SUCCEED);
        result.put("message", "检查容量充足");

        try {
            String clusterResponse = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/manage-cluster/service/indata/cluster/clusterInfo/"
                    + instance.getUserId().split("-")[1], new HashMap<>(), "UTF-8");
            String clusterId = new JSONObject(clusterResponse).getString("clusterId");

            Map<String, String> params1 = new HashMap<>();
            params1.put("adminName",OpenDataConstants.getUserId());
            params1.put("clusterId", clusterId);
            String hdfsCapacityStr = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/manage-cluster/service/indata/component/hdfsCapacity",
                    params1, "UTF-8");

            LOG.error("====================>hdfsCapacityStr:"+hdfsCapacityStr);

            Double hdfsCapacity = Double.valueOf(hdfsCapacityStr);

            if (hdfsCapacity - instance.getStorageVolume() < 0) {
                result.put("status", Constants.RESULT_FAILED);
                result.put("message", "HDFS空间不足");
                return result;
            }

            Map<String, String> params2 = new HashMap<>();
            params2.put("clusterId",clusterId);
            String yarnUrl = HttpRequestUtil.doGet(PropertiesUtil.getValue(Constants.CONF_PROPERTIES, "mcs.domain") + "/manage-cluster/service/indata/component/yarnUrl",
                    params2, "UTF-8");
            LOG.error("====================>yarnurl:"+yarnUrl);


            String metricsStr = HttpRequestUtil.doGet(yarnUrl, new HashMap<>(), "UTF-8");
            LOG.error("====================>metricsStr:"+metricsStr);
            JSONObject metrics = new JSONObject(metricsStr);
            JSONObject clusterResourceInfo = metrics.getJSONObject("clusterMetrics");
            long totalMB = clusterResourceInfo.getLong("totalMB");

            List<MaxComputeServiceInstance> runningInstances = maxComputeServiceInstanceMapper.getRunningInstances();
            Integer totalMemory = 0;
            for (MaxComputeServiceInstance runningInstance : runningInstances) {
                totalMemory += runningInstance.getMemory();
            }
            LOG.error("====================>totalMB:"+totalMB);
            LOG.error("====================>totalMemory:"+totalMemory);
            LOG.error("====================>instance.getMemory():"+instance.getMemory());
            if (totalMB/1024 - totalMemory-instance.getMemory() < 0) {
                result.put("status", Constants.RESULT_FAILED);
                result.put("message", "Yarn内存不足");
                return result;
            }
        } catch (Exception e) {
            LOG.error("检查容量错误:" + e);
            result.put("status", Constants.RESULT_FAILED);
            result.put("message", "检查容量错误");
        }

        return result;
    }


}