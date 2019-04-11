package com.inspur.bigdata.manage.open.data.data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;


/**
 * 服务申请
 * 
 * @author songlili
 * 
 */
@Table(name="dt_data_apply")
public class DataApply implements Serializable {


	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "dt_data_id")
	private String dtDataId;
	@Column(name = "applicant")
	private String applicant;
	@Column(name = "apply_time")
	private String applyTime;
	@Column(name = "auth_status")
	private String authStatus;
	@Column(name = "auth_time")
	private String authTime;
	@Column(name = "auth_user")
	private String authUser;

	@Transient
	private String name;

	@Transient
	private String tableName;

	@Transient
	private String description;

	@Transient
	private  String provider;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDtDataId() {
		return dtDataId;
	}

	public void setDtDataId(String dtDataId) {
		this.dtDataId = dtDataId;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getAuthTime() {
		return authTime;
	}

	public void setAuthTime(String authTime) {
		this.authTime = authTime;
	}

	public String getAuthUser() {
		return authUser;
	}

	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
