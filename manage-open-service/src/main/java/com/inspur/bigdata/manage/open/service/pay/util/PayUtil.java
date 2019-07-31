package com.inspur.bigdata.manage.open.service.pay.util;

/**
 * 网上支付公共类
 *
 * @author zhaoyx
 * <p>
 * 从ecweb包com.inspur.pay.onlinepay.util 拿过来的2018-07-30
 */
public class PayUtil {

    // 支付类型 pay type
    public static final String TYPE_PAY = "01";//支付
    public static final String TYPE_REFUND = "02";//退款
    //    public static final String TYPE_TRANSFER = "06";//转账
    public static final String TYPE_TRANSFER = "03";//转账


    // 付款标识
    public static final String PAY_STATUS_PRE_PAY = "0";  //未付款
    public static final String PAY_STATUS_SUCCESS = "1";  //已付款
    public static final String PAY_STATUS_FAIL = "2";    //支付异常
    public static final String PAY_STATUS_CLOSE = "3";
    public static final String PAY_STATUS_FINISH = "4";


    public static final String PAY_ORDER_SUBJECT = "Open Service Center";
    public static final String PAY_ORDER_BODY = "Open Service Center Description";
    public static final String PAY_ORDER_PRODUCT_CODE = "OPEN_SERVICE_TRADE_PAY";


    /**
     * B2C
     */
//	public static final String B2C = "01";

    /**
     * B2B
     */
//	public static final String B2B = "02";

    /**
     * 支付标志
     */
//	public static final String PAY = "P";

    /**
     * 退款标志
     */
//	public static final String REFUND = "R";

    // 现在还没用到
    public static final String ALIPAY = "95188";
    public static final String ALIPAY_01_CODE = "95188:01";
    public static final String UPOP_CODE = "95516:02";
    public static final String UPOP_CHINAPAY = "CHINAPAY";
    public static final String CFCA_CODE = "95516A";
    public static final String QFB_CODE = "600677";
    public static final String CCB_BANK_CODE = "95533";
    public static final String CUP_CHINAPAY = "95516";
    public static final String CUP_CHINAPAY_B2B = "95516ep";
    public static final String SPD_CODE = "95528";
    public static final String UPOP_B2C_CODE = "95516:01";
    public static final String UPOP_B2B_CODE = "95516:03";
    public static final String SPD_CODE_B2B = "95528:02";
    public static final String SPD_CODE_B2B_KH = "9552802kh";
    public static final String PAB_CODE = "95511";
    public static final String XL_CODE = "95011";
    public static final String KQ_CODE = "5799";
    public static final String WEIXIN_CODE = "weixin";
    public static final String BOC_CODE_B2B = "95566:02";
    public static final String BOC_CODE = "95566";
    public static final String ABC_CODE = "95599";
    public static final String ALLIN_CODE = "95156";
    public static final String ALLINVSP_CODE = "allin";
    public static final String YEE_CODE = "yeepay";
    public static final String WFT_CODE = "wftpay";
    public static final String UMS_QR_CODE = "umsqr";

    // 和healthmall业务不一致，注释掉
//	public static final String TRANS_TYPE_CO = "01";//普通订单
//	public static final String TRANS_TYPE_TRADE = "02";//贸易订单
//	public static final String TRANS_TYPE_FILL = "03";//充值
//	public static final String TRANS_TYPE_SETTLE = "04";//结算单
//	public static final String TRANS_TYPE_BILL = "05";//保证金
//	public static final String TRANS_TYPE_ST = "06";//测土订单
//	public static final String TRANS_TYPE_POF = "07";//配肥订单
//	public static final String TRANS_TYPE_TASK = "08";//作业订单
//	public static final String TRANS_TYPE_PCB = "09";//PCB订单
//	public static final String TRANS_TYPE_HTL = "10";//酒店服务订单
//	public static final String TRANS_TYPE_BIG = "11";//大订单
//	public static final String TRANS_TYPE_QLS_RECHARGE = "12";//祁连山充值订单
//	public static final String TRANS_TYPE_REPAYMENT = "13";//账期还款
//	public static final String TRANS_TYPE_HOTEL = "14";//黄花酒店订单
//	public static final String TRANS_TYPE_CAR = "15";//黄花订车订单
//	public static final String TRANS_TYPE_TRAVEL = "16";//黄花旅游订单
//	public static final String TRANS_TYPE_QLS_CO = "17";//祁连山订单    
//	public static final String TRANS_TYPE_VIPSERVICE = "18";//黄花贵宾服务订单
//	public static final String TRANS_TYPE_TFCOAL_HF = "19";//铁法供应商会费
//	public static final String TRANS_TYPE_TFCOAL_BD = "20";//铁法标书购买
//	public static final String TRANS_TYPE_TFCOAL_BB = "21";//铁法投标保证金
//	public static final String TRANS_TYPE_PCB_REPAY = "22";//PCB补款订单
//	public static final String TRANS_TYPE_TRAN_CAR = "23";//黄花汽运订单
//	public static final String TRANS_TYPE_TRAN_FLGHT = "24";//黄花机票订单
//	public static final String TRANS_TYPE_BFMY_ETRADE_NORMAL = "25";//外贸普通订单支付
//	public static final String TRANS_TYPE_BFMY_ETRADE_LOAN = "26";//外贸借款支付
//	public static final String TRANS_TYPE_BFMY_ETRADE_ACCOUNT_PERIOD = "26";//外贸账期支付


//  public static final String NOTIFY_TYPE_FG = "1";//页面通知
//  public static final String NOTIFY_TYPE_BG = "2";//服务器通知

//  public static final String PAY_FLAG_FAIL = "0";//服务器通知
//  public static final String PAY_FLAG_SUCCESS = "1";//服务器通知
//  public static final String PAY_FLAG_UNKNOW = "2";//服务器通知


}
