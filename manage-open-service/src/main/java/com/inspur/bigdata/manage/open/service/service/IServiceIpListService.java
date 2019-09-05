package com.inspur.bigdata.manage.open.service.service;

import com.inspur.bigdata.manage.open.service.data.IpList;

import java.util.List;
import java.util.Map;

public interface IServiceIpListService {
    List<IpList> queryIpList(Map map);

    List<IpList> getIpList(Map map);

    void addIpList(IpList ipList);

    void updateIpList(IpList ipList);

    void deleteIpListById(String id);
}
