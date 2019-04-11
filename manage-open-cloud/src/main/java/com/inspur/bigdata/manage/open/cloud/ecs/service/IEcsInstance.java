package com.inspur.bigdata.manage.open.cloud.ecs.service;

import java.util.Map;

public interface IEcsInstance {

    String getEcsInstancesByUser();

    String getEcsInstanceByAuditor();

    String checkEcsName(Map<String, Object> params);

    Map<String, String> applyInstance(Map<String, Object> params);

    Map<String, String> passInstance(Map<String, Object> params);

    Map<String, String> rejectInstance(Map<String, Object> params);

    Map<String, String> powerOperation(Map<String, Object> params);

    Map<String, String> applyChangeFormat(Map<String, Object> params);

    Map<String, String> auditChangeFormat(Map<String, Object> params);

    Map<String, String> resetPwd(Map<String, Object> params);

    Map<String, String> modifyKey(Map<String, Object> params);

    Map<String, String> deleteInstance(Map<String, Object> params);
}
