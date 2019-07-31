package com.inspur.bigdata.manage.open.service.pay.domain;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.inspur.bigdata.manage.open.service.pay.util.AliConfig;
import com.inspur.bigdata.manage.open.service.pay.util.AlipayCore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 对接支付宝支付<br>
 * <a href="https://docs.open.alipay.com/api_1/">支付宝支付API列表<a><br>
 * <a href="https://docs.open.alipay.com/270/105900//">支付宝电脑支付API列表<a>
 */

@Service("aliPayDomain")
public class AliPayDomain {

    private static final Log log = LogFactory.getLog(AliPayDomain.class);

    /**
     * 请求支付宝支付页面
     *
     * @param params out_trade_no: 商户订单号
     *               total_amount: 付款金额
     *               subject     : 订单金额
     *               body        : 商品描述
     * @return String result
     * 返回一个form表单直接将订单信息提交到支付宝收银台
     * 类似下面这种
     * <form name="punchout_form" method="post" action="https://openapi.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=FuPK5ou6stEohGCrw1f2RX9EoI3wb9MzraWQQ%2B%2B0AuiPW%2F2R3Ow0fI3sZN6Py4jgm8CYCj7NIvxdXxg1WpnrNevAl2GQ65fCVCatbBr4Qo%2FKZ5JJJ2Wn8BYDGpGdwH9oaQGcZkuNQCQH4Sv6MWvx01eKdna4fHxwL0%2ByW3hkyPgWNFhDlXwNOaJFG63LRVn6d%2B%2BHmTu1uRaGasnnOwZgxr2BhHEe%2B60aLYurr1Ki6yMeMmclDhNcmACddrunP5rK6zm65G1sGGzUIlvfa46BBXAfa8q7yHkEKVZvfXQzxpvM55Lj2poaIQQZskJWiHlxI5RZ2mOkAN6N0ljU8s199g%3D%3D&return_url=http%3A%2F%2Flocalhost%3A8080%2Fitzixi-maven-ssm-alipay%2Falipay%2FalipayReturnNotice.action&notify_url=http%3A%2F%2Flocalhost%3A8080%2Fitzixi-maven-ssm-alipay%2Falipay%2FalipayNotifyNotice.action&version=1.0&app_id=2016091800538648&sign_type=RSA2&timestamp=2018-07-26+14%3A36%3A03&alipay_sdk=alipay-sdk-java-3.1.0&format=json">
     * <input type="hidden" name="biz_content" value="{&quot;out_trade_no&quot;:&quot;180726ABNHTZ4DP0&quot;,&quot;total_amount&quot;:&quot;1998.0&quot;,&quot;subject&quot;:&quot;hongmi&quot;,&quot;body&quot;:&quot;用户订购商品个数：2&quot;,&quot;timeout_express&quot;:&quot;1c&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}">
     * <input type="submit" value="立即支付" style="display:none" >
     * </form>
     * <script>document.forms[0].submit();</script>
     * @throws AlipayApiException
     */
    public String askForPay(Map params) throws AlipayApiException {

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConfig.getInstance().getGatewayUrl(), AliConfig.getInstance().getAppId()
                , AliConfig.getInstance().getMerchantPrivateKey(), "json", AliConfig.getInstance().getCharset(),
                AliConfig.getInstance().getAlipayPublicKey(), AliConfig.getInstance().getSignType());

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AliConfig.getInstance().getReturnUrl());
        alipayRequest.setNotifyUrl(AliConfig.getInstance().getNotifyUrl());

//		//商户订单号，商户网站订单系统中唯一订单号，必填
//		String out_trade_no = (String)params.get("out_trade_no");
//		//付款金额，必填
//		String total_amount = (String)params.get("total_amount");
//		//订单名称，必填
//		String subject = (String)params.get("subject");
//		//商品描述，可空
//		String body = (String)params.get("body");


        // 该笔订单允许的最晚付款时间，逾期将关闭交易。
        // 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        // 如不设置，则付款时间不会超时
//    	String timeout_express = "1c";

//		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
//				+ "\"total_amount\":\""+ total_amount +"\","
//				+ "\"subject\":\""+ subject +"\","
//				+ "\"body\":\""+ body +"\","
////				+ "\"timeout_express\":\""+ timeout_express +"\","
//				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

//		params.put("product_code", "FAST_INSTANT_TRADE_PAY"); // 必选 https://docs.open.alipay.com/270/alipay.trade.page.pay 

        Map<String, String> paraFilter = AlipayCore.paraFilter(params);
        String bizContent = AlipayCore.createLinkJsonString(paraFilter);
        alipayRequest.setBizContent(bizContent);

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }

    /**
     * 获取支付结果
     *
     * @param params
     * @return
     */
    public String checkPayResult(Map params) {

        return null;
    }

    /**
     * 交易查询
     * 两个参数二选一
     *
     * @param params out_trade_no: 商户订单号
     *               trade_no    : 支付宝交易号
     * @return
     * @throws AlipayApiException
     */
    public String queryAliPayLog(Map params) throws AlipayApiException {

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConfig.getInstance().getGatewayUrl(), AliConfig.getInstance().getAppId()
                , AliConfig.getInstance().getMerchantPrivateKey(), "json", AliConfig.getInstance().getCharset(),
                AliConfig.getInstance().getAlipayPublicKey(), AliConfig.getInstance().getSignType());

        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = (String) params.get("out_trade_no");
        //支付宝交易号
        String trade_no = (String) params.get("trade_no");
        //请二选一设置

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"trade_no\":\"" + trade_no + "\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        return result;
    }


    /**
     * 支付宝回调return_url的请求
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    public Map alipayReturn(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        Map result = new HashMap<>();
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AliConfig.getInstance().getAlipayPublicKey(),
                AliConfig.getInstance().getCharset(), AliConfig.getInstance().getSignType()); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);


            result.put("code", "200");
            result.put("out_trade_no", out_trade_no);//订单编号
            result.put("trade_no", trade_no);//支付编号
            result.put("total_amount", total_amount);//金额
            result.put("msg", "恭喜您！支付成功！");
//			out.println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);

        } else {
//			out.println("验签失败");
            result.put("code", "901");
            result.put("msg", "验签失败");
        }

        // 这里根据需要返回数据，目前没有往result里放数据
        return result;
    }


    /**
     * 支付宝回调notify_url的请求
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    public Map alipayNotify(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        Map result = new HashMap<>();
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AliConfig.getInstance().getAlipayPublicKey(),
                AliConfig.getInstance().getCharset(), AliConfig.getInstance().getSignType()); //调用SDK验证签名

        log.debug("支付宝异步回调，验签" + (signVerified ? "成功" : "出错"));

        //——请在这里编写您的程序（以下代码仅作参考）——
		
		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
        if (signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            if (trade_status.equals("TRADE_FINISHED")) { // 交易完成
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                result.put("code", "201");
                result.put(out_trade_no, "out_trade_no");
                result.put(trade_no, "trade_no");
                result.put(trade_status, "trade_status");

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) { // 交易成功
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                result.put("code", "200");
                result.put(out_trade_no, "out_trade_no");
                result.put(trade_no, "trade_no");
                result.put(trade_status, "trade_status");

            }

//			out.println("success");

        } else {//验证失败
//			out.println("fail");

            result.put("code", "901");
            result.put("msg", "验签失败");
            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }

        // 这里根据需要返回数据，目前没有往result里放数据
        return result;
    }

    /**
     * 退款
     *
     * @param params out_trade_no: 商户订单号
     *               trade_no    : 支付宝交易号
     *               refund_amount:退款金额
     *               refund_reason: 退款的原因说明
     *               out_request_no:标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
     * @return
     * @throws AlipayApiException
     */
    public String askForRefund(Map params) throws AlipayApiException {

        //获得初始化的AlipayClient
//		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        AlipayClient alipayClient = new DefaultAlipayClient(AliConfig.getInstance().getGatewayUrl(), AliConfig.getInstance().getAppId()
                , AliConfig.getInstance().getMerchantPrivateKey(), "json", AliConfig.getInstance().getCharset(),
                AliConfig.getInstance().getAlipayPublicKey(), AliConfig.getInstance().getSignType());

        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = (String) params.get("out_trade_no");
        //支付宝交易号
        String trade_no = (String) params.get("trade_no");
        //请二选一设置
        //需要退款的金额，该金额不能大于订单金额，必填
        String refund_amount = (String) params.get("refund_amount");
        //退款的原因说明
        String refund_reason = (String) params.get("refund_reason");
        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        String out_request_no = (String) params.get("out_request_no");

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"trade_no\":\"" + trade_no + "\","
                + "\"refund_amount\":\"" + refund_amount + "\","
                + "\"refund_reason\":\"" + refund_reason + "\","
                + "\"out_request_no\":\"" + out_request_no + "\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        return result;
    }

    /**
     * 关闭交易
     * 两个参数二选一
     *
     * @param params out_trade_no: 商户订单号
     *               trade_no    : 支付宝交易号
     * @return
     * @throws AlipayApiException
     */
    public String closePayOrder(Map params) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConfig.getInstance().getGatewayUrl(), AliConfig.getInstance().getAppId()
                , AliConfig.getInstance().getMerchantPrivateKey(), "json", AliConfig.getInstance().getCharset(),
                AliConfig.getInstance().getAlipayPublicKey(), AliConfig.getInstance().getSignType());

        //设置请求参数
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = (String) params.get("out_trade_no");
        //支付宝交易号
        String trade_no = (String) params.get("trade_no");
        //请二选一设置

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"trade_no\":\"" + trade_no + "\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        return result;
    }

}
