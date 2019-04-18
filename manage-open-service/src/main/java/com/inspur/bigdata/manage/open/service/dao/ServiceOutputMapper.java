package com.inspur.bigdata.manage.open.service.dao;

import com.inspur.bigdata.manage.open.service.data.ServiceOutput;
import org.loushang.framework.mybatis.mapper.EntityMapper;

import java.util.List;

public interface ServiceOutputMapper extends EntityMapper<ServiceOutput> {
    void deleteByApiId(String apiId);
    List<ServiceOutput> selectByApiId(String apiI);
}
