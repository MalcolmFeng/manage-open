package com.inspur.bigdata.manage.open.service.service.impl;

import com.inspur.bigdata.manage.open.service.dao.IpListMapper;
import com.inspur.bigdata.manage.open.service.data.IpList;
import com.inspur.bigdata.manage.open.service.service.IServiceIpListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("IServiceIpListService")
@Transactional("mybatisTransactionManager")
public class IpListServiceImpl implements IServiceIpListService {
    @Autowired
    private IpListMapper ipListMapper;

    public List<IpList> queryIpList(Map map) {
        return ipListMapper.query(map);
    }

    public List<IpList> getIpList(Map map) {
        return ipListMapper.getIpList(map);
    }

    public void addIpList(IpList ipList) {
        ipListMapper.insert(ipList);
    }

    public void updateIpList(IpList ipList) {
        ipListMapper.updateIpList(ipList);
    }

    public void deleteIpListById(String id) {
        ipListMapper.deleteById(id);
    }
}
