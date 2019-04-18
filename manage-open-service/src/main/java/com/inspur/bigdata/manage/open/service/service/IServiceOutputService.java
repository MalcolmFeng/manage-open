package com.inspur.bigdata.manage.open.service.service;

import com.inspur.bigdata.manage.open.service.data.ServiceOutput;

import java.util.List;

public interface IServiceOutputService {
    List<ServiceOutput> selectByApiId(String apiI);
}
