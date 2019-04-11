package com.inspur.bigdata.manage.open.cloud.mcs.dao;

import com.inspur.bigdata.manage.open.cloud.mcs.data.MaxComputeServiceInstance;

import java.util.List;
import java.util.Map;

public interface MaxComputeServiceInstanceMapper {

	// 查询所有大数据计算服务实例
	List<MaxComputeServiceInstance> getMaxComputeServiceInstances(Map<String, String> params);

	// 查询所有大数据计算服务实例（按照申请时间降序排序）
	List<MaxComputeServiceInstance> getMaxComputeServiceInstancesOrderByApplyTimeDesc(Map<String, String> params);

	// 保存一个大数据计算服务实例
	void saveMaxComputeServiceInstance(MaxComputeServiceInstance maxComputeServiceInstance);

	// 根据实例id查询大数据计算服务实例
	MaxComputeServiceInstance getMaxComputeServiceInstanceByInstanceId(String instanceId);

	// 更新大数据计算服务实例状态
	void updateMaxComputeServiceInstance(MaxComputeServiceInstance maxComputeServiceInstance);

	// 获取所有正在运行的大数据计算服务实例
	List<MaxComputeServiceInstance> getRunningInstances();

}