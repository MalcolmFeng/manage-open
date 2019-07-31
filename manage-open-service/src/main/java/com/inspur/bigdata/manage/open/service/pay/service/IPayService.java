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
	 * 向银行网上支付平台请求，网上支付的地址 返回Map，数据 CODE:代码，000执行成功，其他执行失败 MSG:结果描述 PAY_URL:银行地址
	 * IS_SYS:是否一致 parameter：参数
	 */
	Map requestForPay(Map parmmap);

	/**
	 * 查询账户余额
	 * @param userId 调用方法需要检验userId不为空！！
	 * @return 查询账户余额
	 */
	PayAccountCapital getPayAccountByUserId(String userId);

    Map<String, String> subPayAccountByUserId(String userId, String amount);

	Map payNotifyUrl(HttpServletRequest request, Map map);


	/**
	 * 业务系统查询订单支付结果的接口，批量查询 如果没有支付日志，则不返回结果 如果有日志，但支付异常，则跟银行校验支付结果，否则以支付日志为准
	 * 传入参数list中包含很多Map,每个map是一个订单的数据 这些Map中包含ORG_CODE、CUST_CODE、CO_NUM这三个字段
	 * <p>
	 * 返回结果是Map，包含 code：000为执行成功 msg：结果描述 resultList：订单结果List只有code为000时，才返回
	 *
	 * @param parm
	 * @return
	 */
	Map queryPayResult(List parm);

	/**
	 * 刷新支付结果，属于强制性的，一次只能刷新一个订单。 如果支付日志中的支付结果是失败或异常，则与银行校验支付结果
	 * Map至少包含ORG_CODE、CUST_CODE、CO_NUM这三个字段
	 *
	 * @param parm
	 * @return
	 */
	Map refreshPayResult(Map parm);

	/**
	 * 零售户点击支付成功后，向支付平台校验支付结果。 只查询支付日志，返回paymap
	 *
	 * @param
	 * @return
	 */
	Map checkPayResult(Map parmmap);

	/**
	 * 处理支付请求的结果
	 *
	 * @param parametersMap http参数map集合
	 */
	Map handPayResult(String bankId, Map parmmap);

	Map payReturnUrl(HttpServletRequest request, Map map);

	Map requestForPayClose(Map map);

	//查询支付日志
	List queryPayLog(Map map);

	/**
	 * 充值操作
	 * 如果表里没有信息，新增一条记录，如果表里已经有了，账户余额= 当前余额+充值金额
	 *
	 * @param userId
	 * @param amount
	 * @return String status "success"|"failed"
	 * String msg 提示信息
	 */
	Map<String, String> addOrUpdatePayAccountByUserId(String userId, String amount);

}
