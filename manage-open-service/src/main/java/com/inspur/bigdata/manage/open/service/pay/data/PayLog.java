package com.inspur.bigdata.manage.open.service.pay.data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "PAY_LOG")
public class PayLog {
    @Id
    @Column(name = "log_id")
    private String log_id;

    @Column(name = "order_id")
    private String order_id;

    @Column(name = "amount")
    private String amount;

    @Column(name = "pay_seq")
    private String pay_seq;

    @Column(name = "pay_time")
    private String pay_time;

    @Column(name = "pay_type")
    private String pay_type;

    @Column(name = "pay_status")
    private String pay_status;

    @Column(name = "update_time")
    private String update_time;

    @Column(name = "pay_user_id")
    private String pay_user_id;

    @Column(name = "pay_user_type")
    private String pay_user_type;

    @Column(name = "pay_bank_id")
    private String pay_bank_id;

    @Column(name = "pay_bank_name")
    private String pay_bank_name;

    @Column(name = "pay_cardholder_name")
    private String pay_cardholder_name;

    @Column(name = "pay_bank_card_no")
    private String pay_bank_card_no;

    @Column(name = "payee_user_id")
    private String payee_user_id;

    @Column(name = "payee_user_type")
    private String payee_user_type;

    @Column(name = "payee_bank_id")
    private String payee_bank_id;

    @Column(name = "payee_bank_name")
    private String payee_bank_name;

    @Column(name = "payee_cardholder_name")
    private String payee_cardholder_name;

    @Column(name = "payee_bank_card_no")
    private String payee_bank_card_no;

    @Column(name = "note")
    private String note;

    public PayLog() {

    }


    //old
    public String getLogId() {
        return log_id;
    }

    public void setLogId(String logId) {
        this.log_id = logId;
    }

    public String getOrderId() {
        return order_id;
    }

    public void setOrderId(String orderId) {
        this.order_id = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaySeq() {
        return pay_seq;
    }

    public void setPaySeq(String paySeq) {
        this.pay_seq = paySeq;
    }

    public String getPayTime() {
        return pay_time;
    }

    public void setPayTime(String payTime) {
        this.pay_time = payTime;
    }

    public String getPayType() {
        return pay_type;
    }

    public void setPayType(String payType) {
        this.pay_type = payType;
    }

    public String getPayStatus() {
        return pay_status;
    }

    public void setPayStatus(String payStatus) {
        this.pay_status = payStatus;
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String updateTime) {
        this.update_time = updateTime;
    }

    public String getPayUserId() {
        return pay_user_id;
    }

    public void setPayUserId(String payUserId) {
        this.pay_user_id = payUserId;
    }

    public String getPayUserType() {
        return pay_user_type;
    }

    public void setPayUserType(String payUserType) {
        this.pay_user_type = payUserType;
    }

    public String getPayBankId() {
        return pay_bank_id;
    }

    public void setPayBankId(String payBankId) {
        this.pay_bank_id = payBankId;
    }

    public String getPayBankName() {
        return pay_bank_name;
    }

    public void setPayBankName(String payBankName) {
        this.pay_bank_name = payBankName;
    }

    public String getPayCardHolderName() {
        return pay_cardholder_name;
    }

    public void setPayCardHolderName(String payCardHolderName) {
        this.pay_cardholder_name = payCardHolderName;
    }

    public String getPayBankCardNo() {
        return pay_bank_card_no;
    }

    public void setPayBankCardNo(String payBankCardNo) {
        this.pay_bank_card_no = payBankCardNo;
    }

    public String getPayeeUserId() {
        return payee_user_id;
    }

    public void setPayeeUserId(String payeeUserId) {
        this.payee_user_id = payeeUserId;
    }

    public String getPayeeUserType() {
        return payee_user_type;
    }

    public void setPayeeUserType(String payeeUserType) {
        this.payee_user_type = payeeUserType;
    }

    public String getPayeeBankId() {
        return payee_bank_id;
    }

    public void setPayeeBankId(String payeeBankId) {
        this.payee_bank_id = payeeBankId;
    }

    public String getPayeeBankName() {
        return payee_bank_name;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payee_bank_name = payeeBankName;
    }

    public String getPayeeCardHolderName() {
        return payee_cardholder_name;
    }

    public void setPayeeCardHolderName(String payeeCardHolderName) {
        this.payee_cardholder_name = payeeCardHolderName;
    }

    public String getPayeeBankCardNo() {
        return payee_bank_card_no;
    }

    public void setPayeeBankCardNo(String payeeBankCardNo) {
        this.payee_bank_card_no = payeeBankCardNo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
