package com.inspur.bigdata.manage.open.cloud.ecs.service;

import java.util.List;
import java.util.Map;

public interface IEcsKeypair {

    Map<String, String> createKey(Map<String, String> params);

    String getKey(Map<String, String> params);

    List<String> getKeys();

    Map<String, String> deleteKey(Map<String, String> params);

    Map<String, String> deleteKeys(Map<String, String> params);

    byte[] downloadPrivateKey();
}
