package com.inspur.bigdata.manage.open.service.pay.dao;


import com.inspur.bigdata.manage.open.service.pay.data.PayAccountCapital;

public interface PayAccountMapper {

	/**
	 * 查询账户余额
	 * @param userId
	 * @return
	 */
	PayAccountCapital getPayAccount(String userId);

	void insertPayAccount(PayAccountCapital payAccount);

	void addPayAccount(PayAccountCapital payAccount);
	void subPayAccount(PayAccountCapital payAccount);
}
