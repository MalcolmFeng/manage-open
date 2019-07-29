package com.inspur.bigdata.manage.open.service.pay.service;

import com.inspur.bigdata.manage.open.service.pay.data.PayAccountCapital;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 网上支付操作的接口
 *
 * @author maosx
 *
 */
public interface IPayService {

	/**
	 * 查询账户余额
	 * @param userId 调用方法需要检验userId不为空！！
	 * @return 查询账户余额
	 */
	PayAccountCapital getPayAccountByUserId(String userId);

    Map<String, String> subPayAccountByUserId(String userId, String amount);
}
