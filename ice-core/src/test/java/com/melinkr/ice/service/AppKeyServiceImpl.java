package com.melinkr.ice.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
@Service
public class AppKeyServiceImpl implements AppKeyService {
    private Map<String, String> appKeyDataMap;
    @PostConstruct
    public void init() {
        appKeyDataMap = new HashMap<>();
        appKeyDataMap.put("1", "F661DC8AC32D448FAB31C68787497A64");
    }
    @Override
    public String getSecretByAppKey(String appKey) {
        return appKeyDataMap.get(appKey);
    }

    @Override
    public void setSecretByAppKey(String appKey, String secret) {
        appKeyDataMap.put(appKey, secret);
    }
}
