package com.inspur.bigdata.manage.open.service.controller;

import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.service.IServiceMonitorService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * api服务监控表
 *
 * @author zhuzilian
 * @date 2019/08/05
 */
@Controller
@RequestMapping("/dev/ApiServiceMonitor")
public class ServiceMonitorController {
    private static final Log log = LogFactory.getLog(ServiceMonitorController.class);

    @Autowired
    private IServiceMonitorService serviceMonitorService;

    /**
     * [新增]
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/insert")
    public int insert(ApiServiceMonitor apiServiceMonitor) {
        return serviceMonitorService.insert(apiServiceMonitor);
    }

    /**
     * [刪除]
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/delete")
    public int delete(String id) {
        return serviceMonitorService.delete(id);
    }

    /**
     * [更新]
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/update")
    public int update(ApiServiceMonitor apiServiceMonitor) {
        return serviceMonitorService.update(apiServiceMonitor);
    }

    /**
     * [查询] 根据主键 id 查询
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/load")
    public ApiServiceMonitor load(@RequestBody String id) {
        return serviceMonitorService.load(id);
    }

    /**
     * [查询] 根据参数查询
     *
     * @date 2019/08/05
     **/
    @RequestMapping("/query")
    public List<ApiServiceMonitor> query(@RequestBody Map map) {
        return serviceMonitorService.query(map);
    }
}