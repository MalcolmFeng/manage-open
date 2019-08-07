package com.inspur.bigdata.manage.open.service.data;

import java.io.Serializable;

/**
 * api服务监控表
 *
 * @author zhuzilian 2019-08-05
 */
public class ApiServiceMonitor implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * api_service_def表的id，api服务的主键
     */
    private String apiServiceId;

    /**
     * api服务名称
     */
    private String apiServiceName;

    /**
     * 开放平台接收到的入参信息
     */
    private String openServiceInput;

    /**
     * 开放平台接收到的入参header
     */
    private String openServiceInputHeader;

    /**
     * 开放平台的返回数据
     */
    private String openServiceOutput;

    /**
     * 调用开放平台使用的方法
     */
    private String openServiceMethod;

    /**
     * 开放平台调用真实服务的入参
     */
    private String serviceInput;

    /**
     * 开放平台调用真实服务的入参header
     */
    private String serviceInputHeader;

    /**
     * 开放平台调用真实服务的返回值
     */
    private String serviceOutput;

    /**
     * 真实服务的调用方法
     */
    private String serviceMethod;

    /**
     * api服务调用结果编码
     */
    private String result;

    /**
     * 调用者ip地址
     */
    private String callerIp;

    /**
     * 调用者的应用id
     */
    private String callerAppId;

    /**
     * 调用者id
     */
    private String callerUserId;

    /**
     * 请求时间
     */
    private String requestTime;

    /**
     * 响应时间
     */
    private String responseTime;

    /**
     * 真实api接口调用时长，单位毫秒
     */
    private Integer serviceTotalTime;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建时间
     */
    private String createTime;


    public ApiServiceMonitor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiServiceId() {
        return apiServiceId;
    }

    public void setApiServiceId(String apiServiceId) {
        this.apiServiceId = apiServiceId;
    }

    public String getApiServiceName() {
        return apiServiceName;
    }

    public void setApiServiceName(String apiServiceName) {
        this.apiServiceName = apiServiceName;
    }

    public String getOpenServiceInput() {
        return openServiceInput;
    }

    public void setOpenServiceInput(String openServiceInput) {
        this.openServiceInput = openServiceInput;
    }

    public String getOpenServiceInputHeader() {
        return openServiceInputHeader;
    }

    public void setOpenServiceInputHeader(String openServiceInputHeader) {
        this.openServiceInputHeader = openServiceInputHeader;
    }

    public String getOpenServiceOutput() {
        return openServiceOutput;
    }

    public void setOpenServiceOutput(String openServiceOutput) {
        this.openServiceOutput = openServiceOutput;
    }

    public String getOpenServiceMethod() {
        return openServiceMethod;
    }

    public void setOpenServiceMethod(String openServiceMethod) {
        this.openServiceMethod = openServiceMethod;
    }

    public String getServiceInput() {
        return serviceInput;
    }

    public void setServiceInput(String serviceInput) {
        this.serviceInput = serviceInput;
    }

    public String getServiceInputHeader() {
        return serviceInputHeader;
    }

    public void setServiceInputHeader(String serviceInputHeader) {
        this.serviceInputHeader = serviceInputHeader;
    }

    public String getServiceOutput() {
        return serviceOutput;
    }

    public void setServiceOutput(String serviceOutput) {
        this.serviceOutput = serviceOutput;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCallerIp() {
        return callerIp;
    }

    public void setCallerIp(String callerIp) {
        this.callerIp = callerIp;
    }

    public String getCallerAppId() {
        return callerAppId;
    }

    public void setCallerAppId(String callerAppId) {
        this.callerAppId = callerAppId;
    }

    public String getCallerUserId() {
        return callerUserId;
    }

    public void setCallerUserId(String callerUserId) {
        this.callerUserId = callerUserId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getServiceTotalTime() {
        return serviceTotalTime;
    }

    public void setServiceTotalTime(Integer serviceTotalTime) {
        this.serviceTotalTime = serviceTotalTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}