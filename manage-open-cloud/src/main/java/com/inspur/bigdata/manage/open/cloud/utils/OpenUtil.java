package com.inspur.bigdata.manage.open.cloud.utils;

import java.util.UUID;

public class OpenUtil {

    // 生成实例名称
    public static String generateInstanceName(String instanceType) {
        return instanceType + "-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }

    // 随机生成字符串（用于数据库名称、用户名、密码）
    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append("abcdefghijklmnopqrstuvwxyz".charAt((int) (Math.random() * 26)));
        }
        return sb.toString();
    }
}
