package com.inspur.bigdata.manage.open.service.data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * ip_list
 *
 * @author zzl 2019-08-29
 */
@Table(name = "ip_list")
public class IpList implements Serializable {

    /**
     * 主键uuid
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 类型 black white
     */
    private String type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 创建人
     */
    private String provider;

    /**
     * ip地址
     */
    @Column(name = "ip_v4")
    private String ipV4;

    /**
     * ipv6
     */
    @Column(name = "ip_v6")
    private String ipV6;

    /**
     * 是否有效
     */
    private String active;


    public IpList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getIpV4() {
        return ipV4;
    }

    public void setIpV4(String ipV4) {
        this.ipV4 = ipV4;
    }

    public String getIpV6() {
        return ipV6;
    }

    public void setIpV6(String ipV6) {
        this.ipV6 = ipV6;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
