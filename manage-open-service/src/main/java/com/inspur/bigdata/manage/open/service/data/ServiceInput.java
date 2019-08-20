package com.inspur.bigdata.manage.open.service.data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Table(name = "api_service_input")
public class ServiceInput implements Serializable, Comparable<ServiceInput> {
    @Id
    @Column(name = "id")
    private String id;

    /*所属api主键*/
    @Column(name = "api_service_id")
    private String apiServiceId;

    /*参数名称*/
    @Column(name = "name")
    private String name;

    /*参数类型*/
    @Column(name = "type")
    private String type;

    /*是否必填 1是 0 否*/
    @Column(name = "required")
    private int required;

    /*参数描述*/
    @Column(name = "description")
    private String description;

    /*后端参数名称*/
    @Column(name = "sc_name")
    private String scName;

    /*后端参数类型*/
    @Column(name = "sc_type")
    private String scType;

    /*后端参数是否必填*/
    @Column(name = "sc_required")
    private int scRequired;

    /*后端参数描述*/
    @Column(name = "sc_description")
    private String scDescription;

    /*后端参数排序*/
    @Column(name = "sc_seq")
    private int scSeq;

    /*后端参数位置类型*/
    @Column(name = "sc_param_type")
    private String scParamType;

    /**
     * 入参固定值
     */
    @Column(name = "fixed_value")
    private String fixedValue;


    @Transient
    private String value;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScName() {
        return scName;
    }

    public void setScName(String scName) {
        this.scName = scName;
    }

    public String getScType() {
        return scType;
    }

    public void setScType(String scType) {
        this.scType = scType;
    }

    public int getScRequired() {
        return scRequired;
    }

    public void setScRequired(int scRequired) {
        this.scRequired = scRequired;
    }

    public String getScDescription() {
        return scDescription;
    }

    public void setScDescription(String scDescription) {
        this.scDescription = scDescription;
    }

    public int getScSeq() {
        return scSeq;
    }

    public void setScSeq(int scSeq) {
        this.scSeq = scSeq;
    }

    public String getScParamType() {
        return scParamType;
    }

    public void setScParamType(String scParamType) {
        this.scParamType = scParamType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(ServiceInput o) {
        try {
            return this.scSeq - o.getScSeq();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(String fixedValue) {
        this.fixedValue = fixedValue;
    }
}
