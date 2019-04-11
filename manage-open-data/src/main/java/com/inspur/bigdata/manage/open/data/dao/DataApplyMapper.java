package com.inspur.bigdata.manage.open.data.dao;

import java.util.List;
import java.util.Map;

import com.inspur.bigdata.manage.open.data.data.DataApply;
import org.loushang.framework.mybatis.mapper.EntityMapper;

/**
 * 数据申请相关
 * 
 * @author
 * 
 */
public interface DataApplyMapper extends EntityMapper<DataApply> {

	 List<DataApply> getApplyList(Map<String, Object> parameters);

	 List<DataApply> getUserApplyList(Map<String, Object> parameters);
	void deleteById(String applyId);

}
