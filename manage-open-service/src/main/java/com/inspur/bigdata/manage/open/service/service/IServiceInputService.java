package com.inspur.bigdata.manage.open.service.service;

import com.inspur.bigdata.manage.open.service.data.ServiceInput;

import java.util.List;
import java.util.Map;

public interface IServiceInputService {

	List<ServiceInput> listByServiceId(String serviceId);
}
