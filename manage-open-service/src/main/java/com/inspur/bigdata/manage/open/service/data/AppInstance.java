package com.inspur.bigdata.manage.open.service.data;

import javax.persistence.Transient;

public class AppInstance {
    private String appId;
    private String appName;
    private String appDescription;
    private String appKey;
    private String appSecret;
    private String appCreateTime;
    private String appUpdateTime;
    private String userId;
    @Transient
    private boolean applyFor;

    @Transient
    private String api_service_id;

    @Transient
    private String auth_status;

    public String getAuth_status() {
        return auth_status;
    }

    public void setAuth_status(String auth_status) {
        this.auth_status = auth_status;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppCreateTime() {
        return appCreateTime;
    }

    public void setAppCreateTime(String appCreateTime) {
        this.appCreateTime = appCreateTime;
    }

    public String getAppUpdateTime() {
        return appUpdateTime;
    }

    public void setAppUpdateTime(String appUpdateTime) {
        this.appUpdateTime = appUpdateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isApplyFor() {
        return applyFor;
    }

    public void setApplyFor(boolean applyFor) {
        this.applyFor = applyFor;
    }

    public String getApi_service_id() {
        return api_service_id;
    }

    public void setApi_service_id(String api_service_id) {
        this.api_service_id = api_service_id;
    }

}
