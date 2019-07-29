package com.inspur.bigdata.manage.open.service.pay.data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "pay_account_capital")
public class PayAccountCapital {
    @Id
    @Column(name = "user_id")
    private String userId;
    @Column(name ="account_balance")
    private String accountBalance;
    @Column(name = "update_time")
    private String updateTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
