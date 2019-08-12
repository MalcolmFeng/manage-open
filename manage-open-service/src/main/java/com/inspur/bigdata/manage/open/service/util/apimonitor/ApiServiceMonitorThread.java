package com.inspur.bigdata.manage.open.service.util.apimonitor;

import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;


public class ApiServiceMonitorThread extends Thread {
    private IServiceMonitorService monitorService;
    private ApiServiceMonitor apiServiceMonitor;
    private static final int Ncpu = Runtime.getRuntime().availableProcessors();

    public ApiServiceMonitorThread(IServiceMonitorService monitorService, ApiServiceMonitor apiServiceMonitor) {
        this.apiServiceMonitor = apiServiceMonitor;
        this.monitorService = monitorService;
    }

    @Override
    public void run() {
        System.out.println(currentThread().getName() + "正在执行");
        this.monitorService.insert(this.apiServiceMonitor);
    }

    public static int getNcpu() {
        return Ncpu;
    }
}
