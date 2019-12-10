package com.inspur.bigdata.manage.open.service.util;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;

public class NetworkUtil {
    private static Log log = LogFactory.getLog(NetworkUtil.class);

    /**
     * 判断ip、端口是否可连接
     *
     * @param host ip或主机名
     * @param port 端口号
     * @return
     */
    public static boolean isHostConnectable(String host, String port) {
        Socket socket = new Socket();
        try {
            socket.setSoTimeout(5000);
            socket.connect(new InetSocketAddress(host, Integer.valueOf(port)));
        } catch (IOException e) {
            log.warn("Test host and port false :" + e.getMessage());
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 调用ping命令检查是否可以ping通ip
     *
     * @param host ip
     * @return
     */
    public static boolean isReachableByPing(String host) {
        String pattern = "((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))";
        boolean isMatch = Pattern.matches(pattern, host);
        if (!isMatch) {
            return isMatch;
        }
        try {
            String cmd = "";
            if (System.getProperty("os.name").startsWith("Windows")) {
                cmd = "cmd /C ping -n 1 " + host + " | find \"TTL\"";
            } else {
                cmd = "ping -c 1 " + host;
            }
            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();

            return myProcess.exitValue() == 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
