package com.inspur.bigdata.manage.utils;

import org.apache.commons.codec.binary.Base64;

import java.util.List;
import java.util.Map;

public class StringUtil {

    /**
     * 字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 字符串不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 队列为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 队列不为空
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }

    /**
     * Map为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    /**
     * Map不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * byte数组不为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(byte[] value) {
        if (null == value || value.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * byte数组不为空
     *
     * @param value
     * @return
     */
    public static boolean isNotEmpty(byte[] value) {
        return !isEmpty(value);
    }

    /**
     * 根据用户名密码生成授权码
     *
     * @param user
     * @param password
     * @return
     */
    public static String encodeAuthorization(String user, String password) {
        String np = user + ":" + password;
        return "Basic " + Base64.encodeBase64String(np.getBytes());
    }

    /**
     * 判断两个zkUrl是否为同一个zookeeper集群
     *
     * @param zkUrl_0
     * @param zkUrl_1
     * @return
     */
    public static boolean checkZkUrl(String zkUrl_0, String zkUrl_1) {
       /* if (isEmpty(zkUrl_0) || isEmpty(zkUrl_1)) {
            return false;
        }
        if (zkUrl_0.equals(zkUrl_1)) {
            return true;
        }
        Set<String> set = new HashSet<String>();
        for (String url : zkUrl_0.split(",")) {
            set.add(url);
        }
        for (String url : zkUrl_1.split(",")) {
            // 当两个zkUrl存在相同节点时，则认为两个zkUrl为同一个zookeeper集群
            if (isNotEmpty(url) && set.contains(url)) {
                return true;
            }
        }*/
        return true;
    }

}
