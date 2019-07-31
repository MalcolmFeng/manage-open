package com.inspur.bigdata.manage.open.service.pay.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.PartSource;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


/* *
 *类名：AlipayFunction
 *功能：支付宝接口公用函数类
 *详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 *
 *从ecweb包com.inspur.pay.onlinepay.util.ali.util拿过来的2018-07-30
 */

public class AlipayCore {

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        /******代码审查***/
        Iterator<Map.Entry<String, String>> it = sArray.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> me = it.next();
            String key = me.getKey();
            String value = me.getValue();

            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        /******代码审查***/
	/*	for (String key : sArray.keySet()) {
			
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}*/

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                sb.append(key);
                sb.append("=");
                sb.append(value);
                //	prestr = prestr + key + "=" + value;
            } else {
                sb.append(key);
                sb.append("=");
                sb.append(value);
                sb.append("&");
                //	prestr = prestr + key + "=" + value + "&";
            }
            prestr = sb.toString();
        }

        return prestr;
    }

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord
     *            要写入日志里的文本内容
     */
//	public static void logResult(String sWord) {
//		FileWriter writer = null;
//		try {
//			try {
//				writer = new FileWriter(AlipayConfig.log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				if(writer!=null){
//					writer.write(sWord);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		} finally {
//			if (writer != null) {
//				try {
//					writer.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

    /**
     * 生成文件摘要
     *
     * @param strFilePath      文件路径
     * @param file_digest_type 摘要算法
     * @return 文件摘要结果
     */
    public static String getAbstract(String strFilePath, String file_digest_type) throws IOException {
        PartSource file = new FilePartSource(new File(strFilePath));
        if (file_digest_type.equals("MD5")) {
            return DigestUtils.md5Hex(file.createInputStream());
        } else if (file_digest_type.equals("SHA")) {
            return DigestUtils.sha256Hex(file.createInputStream());
        } else {
            return "";
        }
    }

    /**
     * 把数组拼接成键值对格式
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接键值对
     */
    public static String createKeyValueString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());

        String prestr = "";
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                sb.append("\"" + key + "\"" + ":\"" + value + "\"");
            } else {
                sb.append("\"" + key + "\"" + ":\"" + value + "\",");
            }
        }
        sb.append("}");
        prestr = sb.toString();
        return prestr;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串,并对value进行encode
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkStringWithEncode(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            try {
                if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                    sb.append(key);
                    sb.append("=");
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                    //	prestr = prestr + key + "=" + value;
                } else {
                    sb.append(key);
                    sb.append("=");
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                    sb.append("&");
                    //	prestr = prestr + key + "=" + value + "&";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            prestr = sb.toString();
        }

        return prestr;
    }

//	把数组所有元素排序，并按照“参数:参数值”的模式用“,”字符拼接成json字符串,并对value进行encode

    /**
     * 按照“参数:参数值”的模式用“,”字符拼接成json字符串
     * 形如: {"out_trade_no":"66666","total_amount":"55555","body":"this is body","product_code":"FAST_INSTANT_TRADE_PAY"}
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkJsonString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
//		Collections.sort(keys); // 有排序的必要么？？？

        String prestr = "";
        StringBuffer sb = new StringBuffer("{");

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            try {
                if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                    sb.append("\"");
                    sb.append(key);
                    sb.append("\":\"");
//					sb.append(URLEncoder.encode(value, "UTF-8"));
                    sb.append(value);
                    sb.append("\"");
                    //	prestr = prestr + key + "=" + value;
                } else {
                    sb.append("\"");
                    sb.append(key);
                    sb.append("\":\"");
//					sb.append(":");
                    sb.append(value);
                    sb.append("\",");
//					sb.append(",");
                    //	prestr = prestr + key + "=" + value + "&";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("}");
        prestr = sb.toString();
        return prestr;
    }
}
