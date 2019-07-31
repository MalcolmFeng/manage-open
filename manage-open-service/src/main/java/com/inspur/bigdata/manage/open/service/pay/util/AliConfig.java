package com.inspur.bigdata.manage.open.service.pay.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 获取支付宝支付配置信息
 * 使用楼上框架的PropertiesUtil工具类
 * 如果配置文件中配置项为空，会查询环境变量中该值。
 */
public class AliConfig {

    private static Log log = LogFactory.getLog(AliConfig.class);

    private static AliConfig config;

    private String gatewayUrl;
    private String appId;
    private String merchantPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
    private String returnUrl;
    private String signType;
    private String charset;


    private String subject;
    private String body;
    private String productCode;

    public AliConfig() {
        init();
    }

    public static AliConfig getInstance() {

        if (config == null) {
            config = new AliConfig();
        }
        return config;

    }

    public void init() {
        Properties p = new Properties();
        //System.out.println(Thread.currentThread().getContextClassLoader());
        try {
            p.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties"), StandardCharsets.UTF_8));
            if (!p.isEmpty()) {

                gatewayUrl = getValue(p, "alipay.gatewayUrl");
                appId = getValue(p, "alipay.app_id");
                merchantPrivateKey = getValue(p, "alipay.merchant_private_key");
                alipayPublicKey = getValue(p, "alipay.alipay_public_key");
                notifyUrl = getValue(p, "alipay.notify_url");
                returnUrl = getValue(p, "alipay.return_url");
                signType = getValue(p, "alipay.sign_type");
                charset = getValue(p, "alipay.charset");

                subject = getValue(p, "pay_order_subject");
                body = getValue(p, "pay_order_body");
                productCode = getValue(p, "pay_order_product_code");
            }
        } catch (IOException e) {
            log.error("读取config.properties出错" + e);
        }

    }

    public static String getValue(Properties p, String key) {
        String val = p.getProperty(key);

        if (val == null) {
            val = System.getenv(key);
        }

        return val;
    }

    public static AliConfig getConfig() {
        return config;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public String getAppId() {
        return appId;
    }

    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getSignType() {
        return signType;
    }

    public String getCharset() {
        return charset;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getProductCode() {
        return productCode;
    }

    public static void main(String[] args) {

        System.out.println(AliConfig.getInstance().getBody());
    }
}
