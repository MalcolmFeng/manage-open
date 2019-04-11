package com.inspur.bigdata.manage.open.service.data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by songlili on 2019/2/13.
 */
@Table(name="api_service_apply")
public class ServiceApply implements Serializable{

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "APP_ID")
    private String app_id;
    @Column(name = "APP_NAME")
    private String app_name;
    @Column(name = "API_SERVICE_ID")
    private String api_service_id;
    @Column(name = "API_SERVICE_NAME")
    private String api_service_name;
    @Column(name="API_PROVIDER")
    private String api_provider;
    @Column(name = "APPLICANT")
    private String applicant;
    @Column(name="APPLY_TIME")
    private  String apply_time;
    @Column(name="AUTH_STATUS")
    private  String auth_status;
    @Column(name="AUTH_TIME")
    private  String auth_time;
    @Column(name="AUTH_USER")
    private String auth_user;
    @Column(name="APPLY_FLAG")
    private String apply_flag;

    @Transient
    private String version;
    @Transient
    private  String name;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Transient
    private  String provider;

    @Transient
    private  String audit_status;

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApi_service_id() {
        return api_service_id;
    }

    public void setApi_service_id(String api_service_id) {
        this.api_service_id = api_service_id;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getAuth_status() {
        return auth_status;
    }

    public void setAuth_status(String auth_status) {
        this.auth_status = auth_status;
    }

    public String getAuth_time() {
        return auth_time;
    }

    public void setAuth_time(String auth_time) {
        this.auth_time = auth_time;
    }

    public String getAuth_user() {
        return auth_user;
    }

    public void setAuth_user(String auth_user) {
        this.auth_user = auth_user;
    }

    public String getApply_flag() {
        return apply_flag;
    }

    public void setApply_flag(String apply_flag) {
        this.apply_flag = apply_flag;
    }

    public String getApi_service_name() {
        return api_service_name;
    }

    public void setApi_service_name(String api_service_name) {
        this.api_service_name = api_service_name;
    }

    public String getApi_provider() {
        return api_provider;
    }

    public void setApi_provider(String api_provider) {
        this.api_provider = api_provider;
    }
}
