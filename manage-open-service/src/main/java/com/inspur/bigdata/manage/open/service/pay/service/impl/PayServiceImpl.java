package com.inspur.bigdata.manage.open.service.pay.service.impl;

import com.inspur.bigdata.manage.open.service.pay.dao.PayAccountMapper;
import com.inspur.bigdata.manage.open.service.pay.data.PayAccountCapital;
import com.inspur.bigdata.manage.open.service.pay.service.IPayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loushang.framework.util.DateUtil;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("payService")
@Transactional("mybatisTransactionManager")
public class PayServiceImpl implements IPayService {

	private static final Log log = LogFactory.getLog(PayServiceImpl.class);

	@Autowired
	private PayAccountMapper payAccountMapper;


//	private String PAY_STATUS_PRE_PAY = "0";  //未付款
//	private String PAY_STATUS_SUCCESS = "1";
//	private String PAY_STATUS_FAIL = "2";
//	private String PAY_STATUS_CLOSE = "3";
//	private String PAY_STATUS_FINISH = "4";


	//查询账户余额
	@Override
	public PayAccountCapital getPayAccountByUserId(String userId) {
		PayAccountCapital payAccount = payAccountMapper.getPayAccount(userId);
		if (payAccount == null) { // 如果查询的用户不在账户余额表里，返回余额为0
			payAccount = new PayAccountCapital();
			payAccount.setUserId(userId);
			payAccount.setAccountBalance("0");
		}
		return payAccount;
	}

	@Override
	public Map<String, String> subPayAccountByUserId(String userId, String amount) {

		Map<String, String> retMap = new HashMap<>();
		try {
			PayAccountCapital payAccount = new PayAccountCapital();
			payAccount.setUserId(userId);
			payAccount.setAccountBalance(amount);
			payAccount.setUpdateTime(DateUtil.getCurrentTime2());
			payAccountMapper.subPayAccount(payAccount);
			retMap.put("status", "success");
			retMap.put("msg", "余额更新成功!");
		} catch (Exception e) {
			log.debug("余额更新出错" + e);
			retMap.put("status", "failed");
			retMap.put("msg", e.getMessage());
		}

		return retMap;
	}

}
