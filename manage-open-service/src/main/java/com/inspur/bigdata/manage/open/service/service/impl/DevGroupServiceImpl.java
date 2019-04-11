package com.inspur.bigdata.manage.open.service.service.impl;

import com.inspur.bigdata.manage.open.service.dao.DevGroupMapper;
import com.inspur.bigdata.manage.open.service.data.DevGroup;
import com.inspur.bigdata.manage.open.service.data.ServiceGroup;
import com.inspur.bigdata.manage.open.service.service.IDevGroupService;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

/**
 * Created by songlili on 2019/2/21.
 */
@Service("devGroupService")
public class DevGroupServiceImpl implements IDevGroupService{
    @Autowired
    private DevGroupMapper devGroupMapper;
    @Override
    public int insert(DevGroup group) {
        String groupId = UUIDGenerator.getUUID();
        group.setId(groupId);
        group.setContext(groupId);
        return devGroupMapper.insert(group);
    }

    @Override
    public void deletebyId(String id) {

         devGroupMapper.deletebyId(id);
    }

    @Override
    public void update(DevGroup group) {

          devGroupMapper.update(group);
    }

    @Override
    public DevGroup getById(String id) {
        return devGroupMapper.getById(id);
    }

    @Override
    public List<DevGroup> getGroupList(Map<String, Object> param) {
        return devGroupMapper.getGroupList(param);
    }

    @Override
    public boolean isRegisted(String userId,String name) {
        DevGroup devGroup=devGroupMapper.isRegisted(userId,name);
        return null == devGroup ? false : true;
    }
    @Override
    public boolean isExistedContext(String context) {
        DevGroup devGroup=devGroupMapper.isExistedContext(context);
        return null == devGroup ? false : true;
    }
}
