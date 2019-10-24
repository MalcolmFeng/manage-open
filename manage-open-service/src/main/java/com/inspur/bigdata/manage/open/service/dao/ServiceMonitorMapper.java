package com.inspur.bigdata.manage.open.service.dao;

import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import org.loushang.framework.mybatis.mapper.EntityMapper;

import java.util.List;
import java.util.Map;


/**
 * api服务监控表
 *
 * @author zhuzilian
 * @date 2019/08/05
 */
public interface ServiceMonitorMapper extends EntityMapper<ApiServiceMonitor> {
    /**
     * [新增]
     *
     * @author zhuzilian
     * @date 2019/08/05
     **/
    int insert(ApiServiceMonitor apiServiceMonitor);

    /**
     * [刪除]
     *
     * @author zhuzilian
     * @date 2019/08/05
     **/
    int deleteById(String id);

    /**
     * [更新]
     *
     * @author zhuzilian
     * @date 2019/08/05
     **/
    int update(ApiServiceMonitor apiServiceMonitor);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author zhuzilian
     * @date 2019/08/05
     **/
    ApiServiceMonitor load(String id);

    @Override
    List<ApiServiceMonitor> query(Map param);

    List<Map<String, String>> getAllCount();

    List<Map<String, String>> getDayCount(Map<String, String> map);

    List<Map<String, String>> getTopApiCount(Map<String, Object> map);

    List<Map<String, String>> getTopIpCount(Map<String, Object> map);

    List<Map<String, Object>> queryNotSuccessNearby(Map<String, Object> paramsMap);
}
