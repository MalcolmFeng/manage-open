package com.inspur.bigdata.manage.utils;

import com.inspur.bigdata.manage.cluster.manager.api.ManagerService;
import com.inspur.bigdata.manage.cluster.tenant.api.TenantClusterService;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liufucai on 2017/6/26.
 */
public class NifiKonxUtil {

    private static Logger logger = LoggerFactory.getLogger(NifiKonxUtil.class);

    /**
     * 获取Nifi集群列表
     *
     * @return 集群节点列表
     */
    public static  Map<String,String> getKnoxHost(String realm) {
       Map<String,String> map = new HashMap<String,String>();
        String KnoxUrl = "";
        String NiFiNginxPort = "";
        Map<String, Object> infos = ManagerService.getManagerInfo();
        String ip = (String) infos.get("managerIp");
        String port = (String) infos.get("managerPort");
        List<String> data_list = new ArrayList<String>();
        List<String> host_list = new ArrayList<String>();
        String clusters_url = "http://" + ip + ":" + port + "/api/v1/clusters/";
        String np = infos.get("userName") + ":" + infos.get("userPassword");
        String authorization = "Basic " + Base64.encodeBase64String(np.getBytes());
        String cluster = TenantClusterService.getTenantClusterInfoByRealm(realm).get("clusterName");
            // ---- 获取nifi配置文件最新版本 ----
            String desired_configs_url = clusters_url + cluster + "?fields=Clusters/desired_configs/nifi-properties";
            JSONObject desiredJson = JSONObject.fromObject(HttpUtil.execGet(authorization,desired_configs_url));
            JSONObject clusters = desiredJson.getJSONObject("Clusters");
            if (!clusters.has("desired_configs")) {
            logger.warn("Cluster：" + cluster + "No NiFi components installed");
            }
            String tag = clusters.getJSONObject("desired_configs").getJSONObject("nifi-properties").getString("tag");
            // ---- 获取nifi配置信息 ----
            String configurations_url = clusters_url + cluster + "/configurations?type=nifi-properties&tag=" + tag;
            JSONObject confJson = JSONObject.fromObject(HttpUtil.execGet(authorization,configurations_url));
            JSONObject properties = confJson.getJSONArray("items").getJSONObject(0).getJSONObject("properties");
            KnoxUrl = properties.getString("nifi.security.user.knox.url");
            NiFiNginxPort = properties.getString("nifi.nginx.port");
        if("".equals(KnoxUrl)||KnoxUrl==null){
        	return map;
        }
        map.put("KnoxUrl",KnoxUrl.split("/")[2]);
        map.put("NiFiNginxPort",NiFiNginxPort);
        return map;
    }


}
