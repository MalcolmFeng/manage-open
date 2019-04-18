package com.inspur.bigdata.manage.open.service.data;

import javax.persistence.Table;

@Table(name = "api_service_output")
public class ServiceOutput {
    /**
     * 主键id
     */
    private String id;
    /**
     * 开放服务id
     */
    private String openServiceId;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 类型
     */
    private String type;
    /**
     * 排序 默认0
     */
    private Integer seq;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenServiceId() {
        return openServiceId;
    }

    public void setOpenServiceId(String openServiceId) {
        this.openServiceId = openServiceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
