package com.inspur.bigdata.manage.open.service.dao;

import com.inspur.bigdata.manage.open.service.data.IpList;
import org.loushang.framework.mybatis.mapper.EntityMapper;

import java.util.List;
import java.util.Map;

public interface IpListMapper extends EntityMapper<IpList> {


    void deleteById(String id);

    /**
     * 模糊查询ipv4和ipv6
     *
     * @param map
     * @return
     */
    List<IpList> getIpList(Map map);

    int updateIpList(IpList ipList);
}
