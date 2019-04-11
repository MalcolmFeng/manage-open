package com.inspur.bigdata.manage.open.data.data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="dt_table_columns")
public class DataTableColumn implements Serializable{

	@Id
	@Column(name = "ID")
	private String id;
	@Column(name = "DT_DATA_ID")
	private String	dtDataId;
	/**
	 * 字段名
	 */
	@Column(name = "COLUMN_NAME")
	private String columnName;
	/**
	 * 字段类型
	 */
	@Column(name = "COLUMN_TYPE")
	private String	columnType;
	/**
	 * 字段描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;
	/**
	 * 是否允许为空  1否 0是
	 */
	@Column(name = "IS_NULL")
	private int isNull;

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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIsNull() {
		return isNull;
	}

	public void setIsNull(int isNull) {
		this.isNull = isNull;
	}
}
