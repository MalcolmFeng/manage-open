package com.inspur.bigdata.manage.open.service.pay.service.impl;

import com.inspur.bigdata.manage.open.service.pay.dao.PayAccountMapper;
import com.inspur.bigdata.manage.open.service.pay.dao.PayServiceMapper;
import com.inspur.bigdata.manage.open.service.pay.data.PayAccountCapital;
import com.inspur.bigdata.manage.open.service.pay.data.PayLog;
import com.inspur.bigdata.manage.open.service.pay.domain.AliPayDomain;
import com.inspur.bigdata.manage.open.service.pay.service.IPayService;
import com.inspur.bigdata.manage.open.service.pay.util.AliConfig;
import com.inspur.bigdata.manage.open.service.pay.util.PayUtil;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
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
    private PayServiceMapper payServiceMapper;

    @Autowired
    private PayAccountMapper payAccountMapper;

    @Autowired
    private AliPayDomain aliPayDomain;

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

    @Override
    public Map requestForPay(Map param) {
        log.info("requestForPay : " + param.toString());
        Map<String, String> mapRet = new HashMap<String, String>();
        try {
            //查询历史充值记录
            Map<String, String> map = new HashMap<String, String>();
            String orderId = (String) param.get("orderId");
            map.put("orderId", orderId);
            List<PayLog> payLogList = payServiceMapper.queryPayLog(map);
            PayLog payLog = new PayLog();
            if (payLogList.size() > 0 && payLogList.get(0) != null) {
                //已支付订单
                if (PayUtil.PAY_STATUS_SUCCESS.equals(payLogList.get(0).getPayStatus())) {
                    log.info("requestForPay : " + param.get("orderId") + " 已付款！");
                    mapRet.put("code", "901");
                    mapRet.put("msg", "订单" + param.get("orderId") + " 已付款！");
                    return mapRet;
                } else {
                    //未支付订单
                    PayLog existPayLog = payLogList.get(0);
                    existPayLog.setUpdateTime(DateUtil.getCurrentTime2());
                    Map<String, String> payMap = new HashMap<String, String>();
                    payMap.put("out_trade_no", existPayLog.getOrderId());
                    payMap.put("total_amount", existPayLog.getAmount());
                    payMap.put("subject", AliConfig.getInstance().getSubject());
                    payMap.put("body", AliConfig.getInstance().getBody());
                    payMap.put("product_code", AliConfig.getInstance().getProductCode());
                    //调用支付宝支付接口
                    String aliPayPageContent = aliPayDomain.askForPay(payMap);
                    payServiceMapper.updatePayLog(existPayLog);
                    mapRet.put("code", "200");
                    mapRet.put("data", aliPayPageContent);
                    return mapRet;
                }
            }
            Map<String, String> payMap = new HashMap<String, String>();
            if (orderId == null || "".equals(orderId)) {
                orderId = UUIDGenerator.getUUID();
            }
            payMap.put("out_trade_no", orderId);
            payMap.put("total_amount", (String) param.get("amount"));
            payMap.put("subject", AliConfig.getInstance().getSubject());
            payMap.put("body", AliConfig.getInstance().getBody());
            payMap.put("product_code", AliConfig.getInstance().getProductCode());
//			payMap.put("subject", (String)param.get("orderTitle"));
//			payMap.put("body", (String)param.get("orderDesc"));

            //调用支付宝支付接口
            String aliPayPageContent = aliPayDomain.askForPay(payMap);

            //添加支付日志
            String now = DateUtil.getCurrentTime2(); // yyyy-MM-dd HH:mm:ss
            payLog.setAmount((String) param.get("amount"));
            payLog.setLogId(UUIDGenerator.getUUID());
            payLog.setPayTime(now);
            payLog.setUpdateTime(now);
            payLog.setPayUserId((String) param.get("userId"));
            payLog.setOrderId(orderId);
            payLog.setPayBankId((String) param.get("bankId"));
            payLog.setPayBankName((String) param.get("bankName"));
            payLog.setPayeeUserId((String) param.get("userId"));
            payLog.setPayStatus(PayUtil.PAY_STATUS_PRE_PAY);  //未付款
            payLog.setPayTime(DateUtil.getCurrentTime());
            payLog.setPayType(PayUtil.TYPE_PAY);  //支付
            payServiceMapper.addPayLog(payLog);
            mapRet.put("code", "200");
            mapRet.put("data", aliPayPageContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapRet;
    }

    @Override
    public Map queryPayResult(List parm) {
        return null;
    }

    @Override
    public Map refreshPayResult(Map parm) {
        return null;
    }

    @Override
    public Map checkPayResult(Map parmmap) {
        return null;
    }

    @Override
    public Map handPayResult(String bankId, Map parmmap) {
        return null;
    }

    //查询支付日志
    @Override
    public List queryPayLog(Map map) {
        List list = Collections.emptyList();
        try {
            list = payServiceMapper.queryPayLog(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Map<String, String> addOrUpdatePayAccountByUserId(String userId, String amount) {
        Map<String, String> retMap = new HashMap<>();
        try {
            PayAccountCapital payAccount = payAccountMapper.getPayAccount(userId);
            if (payAccount == null) {
                payAccount = new PayAccountCapital();
                payAccount.setUserId(userId);
                payAccount.setAccountBalance(amount);
                payAccount.setUpdateTime(DateUtil.getCurrentTime2());
                payAccountMapper.insertPayAccount(payAccount);
            } else {
                payAccount.setAccountBalance(amount);
                payAccount.setUpdateTime(DateUtil.getCurrentTime2());
                payAccountMapper.addPayAccount(payAccount);
            }
            retMap.put("status", "success");
            retMap.put("msg", "余额更新成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("余额更新出错" + e);
            retMap.put("status", "failed");
            retMap.put("msg", e.getMessage());
        }
        return retMap;
    }

    /**
     * 更新pay_log表，及pay_account表
     *
     * @param payLog
     * @param newPayStatus 支付宝回传的状态
     * @param trade_no     支付宝流水号
     * @param total_amount 金额
     */
    private void updatePaylogAndAccount(PayLog payLog, String newPayStatus, String trade_no, String total_amount) {
        String payStatus = payLog.getPayStatus(); // pay log表记录的当前充值记录的状态
        // 如果return_url返回状态成功的前提下，paylog表里状态还是未支付的话，更新余额
        if (PayUtil.PAY_STATUS_PRE_PAY.equals(payStatus)
                && total_amount != null && !"".equals(total_amount)) {
            payLog.setAmount(total_amount);
            Map<String, String> addOrUpdatePayAccountByUserId = addOrUpdatePayAccountByUserId(OpenDataConstants.getUserId(), total_amount);
            // 万一往数据库更新余额失败，要怎么处理？
        }
        payLog.setPaySeq(trade_no);
        payLog.setPayStatus(newPayStatus);
        payLog.setUpdateTime(DateUtil.getCurrentTime2());
        payServiceMapper.updatePayLog(payLog);
    }

    @Override
    public Map payNotifyUrl(HttpServletRequest request, Map map) {
        Map<String, String> mapRet = new HashMap<String, String>();
        try {
            Map aliResult = aliPayDomain.alipayNotify(request);
            //支付成功
            String out_trade_no = (String) aliResult.get("out_trade_no");
            String trade_no = (String) aliResult.get("trade_no");
            String trade_status = (String) aliResult.get("trade_status");
            String total_amount = (String) aliResult.get("total_amount");
            if ("200".equals(aliResult.get("code"))) {
                map.put("orderId", out_trade_no);
                List<PayLog> payLogList = payServiceMapper.queryPayLog(map);
                if (payLogList.size() > 0) {
                    PayLog payLog = payLogList.get(0);
                    updatePaylogAndAccount(payLog, PayUtil.PAY_STATUS_SUCCESS, trade_no, total_amount);
                    mapRet.put("code", "200");
                    return mapRet;
                } else {
                    mapRet.put("code", "902");
                    mapRet.put("msg", "未找到记录");
                }
                //支付完成并关闭退款功能
            } else if ("201".equals(aliResult.get("code"))) {
                map.put("orderId", out_trade_no);
                List<PayLog> payLogList = payServiceMapper.queryPayLog(map);
                if (payLogList.size() > 0) {
                    PayLog payLog = payLogList.get(0);
                    updatePaylogAndAccount(payLog, PayUtil.PAY_STATUS_FINISH, trade_no, total_amount);
                    mapRet.put("code", "200");
                    return mapRet;
                } else {
                    mapRet.put("code", "902");
                    mapRet.put("msg", "未找到记录");
                }
            } else {
                mapRet.putAll(aliResult);
            }
        } catch (Exception e) {
            log.debug("payNotifyUrl 出错: " + e);
        }
        return mapRet;
    }

    @Override
    public Map payReturnUrl(HttpServletRequest request, Map map) {
        Map<String, String> mapRet = new HashMap<String, String>();
        try {
            Map aliResult = aliPayDomain.alipayReturn(request);
            if ("200".equals(aliResult.get("code"))) {
                String out_trade_no = (String) aliResult.get("out_trade_no");
                String trade_no = (String) aliResult.get("trade_no");
                String total_amount = (String) aliResult.get("total_amount");
                map.put("orderId", out_trade_no);
                List<PayLog> payLogList = payServiceMapper.queryPayLog(map);
                if (payLogList.size() > 0) {
                    PayLog payLog = payLogList.get(0);
                    updatePaylogAndAccount(payLog, PayUtil.PAY_STATUS_SUCCESS, trade_no, total_amount);
                    mapRet.putAll(aliResult);

                } else {
                    mapRet.put("code", "902");
                    mapRet.put("msg", "未找到记录");
                }
            } else {
                mapRet.putAll(aliResult);
            }
        } catch (Exception e) {
            log.debug("payReturnUrl 出错: " + e);
        }
        return mapRet;
    }

    @Override
    public Map requestForPayClose(Map map) {
        String paySeq = (String) map.get("paySeq");
        String payLogId = (String) map.get("payLogId");
        String payStatus = PayUtil.PAY_STATUS_CLOSE;
        PayLog payLog = new PayLog();
        payLog.setLogId(payLogId);
        payLog.setPaySeq(paySeq);
        payLog.setPayStatus(payStatus);
        try {
            aliPayDomain.closePayOrder(map);
            payServiceMapper.updatePayLog(payLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
