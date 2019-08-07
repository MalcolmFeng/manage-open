package com.inspur.bigdata.manage.open.service.util;

import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;


public class ApiServiceMonitorThread extends Thread {
    private IServiceMonitorService monitorService;
    private ApiServiceMonitor apiServiceMonitor;

    public ApiServiceMonitorThread(IServiceMonitorService monitorService, ApiServiceMonitor apiServiceMonitor) {
        this.apiServiceMonitor = apiServiceMonitor;
        this.monitorService = monitorService;
    }

    @Override
    public void run() {
        this.monitorService.insert(this.apiServiceMonitor);
    }
}
