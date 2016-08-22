package com.melinkr.ice.service;

import com.melinkr.ice.Session;
import com.melinkr.ice.SessionService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
@Service
public class SessionServiceImpl implements SessionService {
    //生产环境 可以放在缓存或者 非关系型数据库中
    private Map<String,Session> sessionDataMap = new ConcurrentHashMap<>();


    @Override
    public Session getSession(String sessionId) {
        return sessionDataMap.get(sessionId);
    }


    @Override
    public void setSession(String sessionId, Session session) {
        sessionDataMap.put(sessionId, session);
    }
}
