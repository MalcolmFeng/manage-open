package com.inspur.bigdata.manage.open.service.pay.dao;

import com.inspur.bigdata.manage.open.service.pay.data.PayLog;

import java.util.List;
import java.util.Map;

public interface PayServiceMapper {

    //支付日志表
    void addPayLog(PayLog payLog);

    void updatePayLog(PayLog payLog);

    List<PayLog> queryPayLog(Map payLog);


    //账户资金表
//	public void addPayAccount(PayAccount act);
//	
//	public void updatePayAccount(PayAccount act);

    //public List<PayAccount> queryPayAccount(Map map);

}
