package com.inspur.bigdata.manage.open.service.pay.util;

import com.inspur.hsf.service.common.utils.UUIDGenerator;
import org.loushang.framework.util.DateUtil;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 说明：UUID相关的工具类
 * <p>
 * 生成随机数，对于无业务意义的内码可以用随机数
 * <p>
 * 从ecweb包com.inspur.pub.tool拿过来的 2018-07-30
 */
public class UUIDTool {
    public static void main(String[] args) {
//		System.out.println("返回一个指定位数的随机字符串:" + getUUID(10));
//		System.out.println("返回一个指定前缀和总位数的随机字符串:" + getUUIDWithPrefix("PEFIX", 16));
//		System.out.println("返回一个指定后缀和总位数的随机字符串:" + getUUIDWithSuffix("SUFFIX", 16));
//		System.out.println("返回一个yyyyMMddHHmmssSSS+n位随机数的字符串:" + getRandomWithTime(6));
//		System.out.println("返回一个定长的随机字符串(包含大小写字母、数字):" + generateMixString(10));
//		System.out.println("返回一个定长的随机纯字母字符串(包含大小写字母):" + generateMixCharString(10));
//		System.out.println("返回一个定长的随机纯小写字母字符串(只包含小写字母):" + generateLowerString(10));
//		System.out.println("返回一个定长的随机纯大写字母字符串(只包含大写字母):" + generateUpperString(10));
//		System.out.println("生成一个定长的纯0字符串:" + generateZeroString(10));
//		System.out.println("根据数字生成一个定长的字符串，长度不够前面补0:" + toFixdLengthString(123, 10));
//		System.out.println("前缀+yyyyMMddHHmmssSSS+n位随机数的字符串:" + getPrefixRandomWithTime("FT", 6));

        System.out.println(com.inspur.sc.utils.DateUtil.getCurrentTime());
        System.out.println(DateUtil.getCurrentTime());
        System.out.println(DateUtil.getCurrentTime2());

    }

    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERCHAR = "0123456789";

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateMixString(int length) {
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateMixCharString(int length) {
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(LETTERCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯小写字母字符串
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateLowerString(int length) {
        return generateMixCharString(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机纯大写字母字符串
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateUpperString(int length) {
        return generateMixCharString(length).toUpperCase();
    }

    /**
     * 生成一个定长的纯0字符串
     *
     * @param length 字符串长度
     * @return 纯0字符串
     */
    public static String generateZeroString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param num       数字
     * @param fixdlenth 字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(long num, int fixdlenth) {
        StringBuilder sb = new StringBuilder();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 根据时间获取随机数
     * yyyyMMddHHmmssSSS+ number位的随机数
     *
     * @param number int 时间后面的随机数位数
     * @return String 随机数
     */
    public static String getRandomWithTime(int number) {
        String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder().append(time);
        for (int i = 0; i < number; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();

    }

    /**
     * 延迟10毫秒内的一个随机毫秒数后产生时间随机序列
     * for循环调用产生时间随机序列时使用，防止冲突
     *
     * @param number
     * @return
     */
    public static String getRandomWithTimeDelay(int number) {
        int r = new SecureRandom().nextInt(10);
        try {
            Thread.sleep(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getRandomWithTime(number);
    }

    /**
     * 根据时间格式获取随机数
     *
     * @param format
     * @param number
     * @return
     */
    public static String getRandomWithTime(String format, int number) {
        String time = new SimpleDateFormat(format).format(new Date());
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder().append(time);
        for (int i = 0; i < number; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 前缀+时间+获取随机数 pre+yyyyMMddHHmmssSSS+ number位的随机数
     *
     * @param number int 时间后面的随机数位数
     * @return String 随机数
     * @author tanglibao
     */
    public static String getPrefixRandomWithTime(String prefix, int number) {
        String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder().append(prefix).append(time);
        for (int i = 0; i < number; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();

    }

    /**
     * 获得指定位数的UUID
     *
     * @param number int 指定位数
     * @return String 随机数
     */
    public static String getUUID(int number) {
        UUIDGenerator uuid = new UUIDGenerator();
        return uuid.getNextSeqId(number).toString();
    }

    /**
     * 获得带前缀的指定位数的UUID
     *
     * @param prefix 前缀
     * @param number int 生成随机数的位数
     * @return String 随机数
     */
    public static String getUUIDWithPrefix(String prefix, int number) {

        int count = number - prefix.length();
        return prefix + getUUID(count);
    }

    /**
     * 获得带后缀的指定数目的UUID
     *
     * @param prefix 后缀
     * @param number int 生成随机数的位数
     * @return String 随机数
     */
    public static String getUUIDWithSuffix(String suffix, int number) {

        return getUUID(number - suffix.length()) + suffix;
    }

    /**
     * 获取30位长度的内码
     *
     * @return
     */
    public static String getInnerId() {
        return getInnerIdByLength(30);
    }

    /**
     * 获取指定长度的uuid,建议不少于10位
     *
     * @param length
     * @return
     */
    public static String getInnerIdByLength(int length) {
        UUIDGenerator uuid = new UUIDGenerator();
        return uuid.getNextSeqId(length).toString();
    }

}
