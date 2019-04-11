package com.inspur.bigdata.manage.open.cloud.utils;

import net.sf.json.JSONObject;

public class Result {

	private String status;
	private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public JSONObject getResultJson() {
		JSONObject json = new JSONObject();
		json.put("result", status);
		json.put("message", message);

		return json;
	}

	public String toString() {
		JSONObject json = new JSONObject();
		json.put("result", status);
		json.put("message", message);

		return json.toString();
	}

}