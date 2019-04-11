package com.inspur.bigdata.manage.open.service.service;

import com.inspur.bigdata.manage.open.service.data.AppInstance;

import java.util.List;
import java.util.Map;

public interface IAppManage {

    String insert(AppInstance app);

    void delete(String appId);

    void update(AppInstance app);

    AppInstance getById(String appId);

    List<AppInstance> getAppList(Map<String, Object> param);

    boolean isRegisted(String appName, String userId);
    boolean isExistedAppKey(String appKey);
    List<AppInstance> getappListByUserId(Map<String, Object> param);

    List<AppInstance> getAppStatusByUserId(Map<String, Object> param);
    int returnAppkey();
    List<AppInstance> getAppByAppKey(String key);
}
