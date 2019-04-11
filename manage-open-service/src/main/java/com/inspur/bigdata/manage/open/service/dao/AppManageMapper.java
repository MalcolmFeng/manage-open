package com.inspur.bigdata.manage.open.service.dao;

import com.inspur.bigdata.manage.open.service.data.AppInstance;

import java.util.List;
import java.util.Map;

public interface AppManageMapper {

    void insert(AppInstance app);

    void delete(String appId);

    void update(AppInstance app);

    AppInstance getById(String appId);

    List<AppInstance> getAppList(Map<String, Object> param);

    AppInstance isRegisted(String appName,String userId);
    boolean isExistedAppKey(String appKey);
    List<AppInstance> getappListByUserId(Map<String, Object> param);

    List<AppInstance> getAppStatusByUserId(Map<String, Object> param);
    List<AppInstance> getAppByAppKey(String key);
}
