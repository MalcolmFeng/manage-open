package com.inspur.bigdata.manage.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * 加密工具类
 */
public class EncryptionUtil {
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * MD5加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);

        return md5.digest();
    }

    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {

        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);

        return sha.digest();

    }

    /**
     * BASE64解密,入参出参为String
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64String(String key) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        return new String(decoder.decodeBuffer(key));
    }

    /**
     * 通过REST接口进行解密
     *
     * @return
     * @throws Exception
     */
    public static String decryptRESTString(String url,String name,String value) throws Exception {
        return crypt(url, name, value);
    }
    /**
     * 通过REST接口进行加密
     *
     * @return
     * @throws Exception
     */
    public static String encryptRESTString(String url,String name,String value) throws Exception {
        return crypt(url, name, value);
    }

    private static String crypt(String url, String name, String value) {
        System.out.println("开始执行加解密。。。");
        try {
            Map<String, Object> params = new HashMap<>();
//            params.put(name, value);
            params.put("content", URLEncoder.encode(value));
            HttpResponse response = HttpUtils.doGetSSL(url,params);
            if (response.getStatusLine().getStatusCode() == 200) {
                String entity = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("解密后结果："+ entity);
                return entity;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }


    /**
     * BASE64加密,入参出参为String
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptBASE64String(String data) throws Exception {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.getBytes());
    }

    /**
     * MD5加密,入参出参为String
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptMD5String(String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * SHA-1加密,入参出参为String
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptSHA1(String data) {
        return DigestUtils.sha1Hex(data);
    }

    /**
     * SHA-256加密,入参出参为String
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptSHA256(String data) {
        return DigestUtils.sha256Hex(data);
    }

    /**
     * SHA-384加密,入参出参为String
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptSHA384(String data) {
        return DigestUtils.sha384Hex(data);
    }

    /**
     * SHA-512加密,入参出参为String
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptSHA512(String data) {
        return DigestUtils.sha512Hex(data);
    }
}
