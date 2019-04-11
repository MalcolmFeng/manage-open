package com.inspur.bigdata.manage.open.cloud.mcs.service;

import com.inspur.bigdata.manage.open.cloud.mcs.data.MaxComputeServiceInstance;

import java.util.List;
import java.util.Map;

public interface IMaxComputeServiceInstance {

	// 查询所有大数据计算服务实例
	List<MaxComputeServiceInstance> getMaxComputeServiceInstances(Map<String, String> params);

	// 查询所有大数据计算服务实例（按照申请时间降序排序）
	List<MaxComputeServiceInstance> getMaxComputeServiceInstancesOrderByApplyTimeDesc();

	// 保存一个大数据计算服务实例
	void saveMaxComputeServiceInstance(MaxComputeServiceInstance maxComputeServiceInstance);

	// 根据实例id查询大数据计算服务实例
	MaxComputeServiceInstance getMaxComputeServiceInstanceByInstanceId(String instanceId);

	// 更新大数据计算服务实例状态
	void updateMaxComputeServiceInstance(MaxComputeServiceInstance maxComputeServiceInstance);

	// 申请大数据计算服务实例
	Map<String, String> applyInstance(Map<String, Object> params);

	// 通过大数据计算服务实例
	Map<String, String> passInstance(Map<String, Object> params);

	// 批量通过大数据服务实例
	Map<String, String> passInstances(Map<String, Object> params);

	// 驳回大数据计算服务实例
	Map<String, String> rejectInstance(Map<String, Object> params);

	// 批量驳回大数据计算服务实例
	Map<String, String> rejectInstances(Map<String, Object> params);

	// 删除大数据计算服务实例
	Map<String, String> deleteInstance(Map<String, Object> params);

	// 批量删除大数据计算服务实例
	Map<String, String> deleteInstances(Map<String, Object> params);
    // 获取hdfs存储容量
	Map<String, Object> getStorageVolume(String instanceId, String userId);

}