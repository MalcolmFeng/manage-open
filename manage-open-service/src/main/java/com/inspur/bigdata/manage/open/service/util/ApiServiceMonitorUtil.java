package com.inspur.bigdata.manage.open.service.util;

import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiServiceMonitorUtil {

    public ApiServiceMonitorUtil() {
    }

    public static void insert(IServiceMonitorService monitorService, ApiServiceMonitor apiServiceMonitor) {
//        this.monitorService.insert(apiServiceMonitor);
        ApiServiceMonitorThread asmThread = new ApiServiceMonitorThread(monitorService, apiServiceMonitor);
        asmThread.run();
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
}
