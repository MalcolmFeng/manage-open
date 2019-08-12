package com.inspur.bigdata.manage.open.service.util.apimonitor;

import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;

public class ApiServiceMonitorUtil {

    public ApiServiceMonitorUtil() {
    }

    public static void insert(IServiceMonitorService monitorService, ApiServiceMonitor apiServiceMonitor) {
//        this.monitorService.insert(apiServiceMonitor);
        ApiServiceMonitorThread asmThread = new ApiServiceMonitorThread(monitorService, apiServiceMonitor);
        asmThread.run();
    }

    public static void insertByThreadPool(IServiceMonitorService monitorService, ApiServiceMonitor apiServiceMonitor, ExecutorService executorService) {
        executorService.submit(new ApiServiceMonitorThread(monitorService, apiServiceMonitor));
    }

    /**
     * 获取用户真实IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 根据request请求获取IP地址
     *
     * @param request
     * @return java.lang.String
     * @author wangshushu
     * @date 2019/8/8 9:01
     */
    public static String getClientIp(HttpServletRequest request) {
        String ipAddress = null;
        // ipAddress = this.getRequest().getRemoteAddr();
        ipAddress = request.getHeader("X-Real-Ip");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("x-forwarded-for");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
