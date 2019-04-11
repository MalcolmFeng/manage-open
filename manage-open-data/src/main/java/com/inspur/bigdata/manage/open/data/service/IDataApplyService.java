package com.inspur.bigdata.manage.open.data.service;

import com.inspur.bigdata.manage.open.data.data.DataApply;
import java.util.List;
import java.util.Map;

public interface IDataApplyService {
	List<DataApply> getDataApplyList(Map<String, Object> param);
	void insertDataApply(DataApply dataApply);
	List<DataApply> getUserApplyList(Map<String, Object> parameters);
	DataApply getById(String id);
	void updateDataApply(DataApply dataApply);
	void delete(String id);
}
