package com.inspur.bigdata.manage.open.data.service.impl;


import com.inspur.bigdata.manage.open.data.dao.DataApplyMapper;
import com.inspur.bigdata.manage.open.data.data.DataApply;
import com.inspur.bigdata.manage.open.data.service.IDataApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("dataApplyService")
@Transactional("mybatisTransactionManager")
public class DataApplyServiceImpl implements IDataApplyService {


	@Autowired
	private DataApplyMapper dataApplyMapper;

	@Override
	public List<DataApply> getDataApplyList(Map<String, Object> param){
       return dataApplyMapper.getApplyList(param);
	}

	public void insertDataApply(DataApply dataApply) {
		dataApplyMapper.insert(dataApply);
	}

	@Override
	public List<DataApply> getUserApplyList(Map<String, Object> parameters) {
		return dataApplyMapper.getUserApplyList(parameters);
	}

	@Override
	public DataApply getById(String id) {
		return dataApplyMapper.get(id);
	}

	@Override
	public void updateDataApply(DataApply dataApply) {
		dataApplyMapper.update(dataApply);
	}

	@Override
	public void delete(String id) {
		dataApplyMapper.deleteById(id);
	}
}
