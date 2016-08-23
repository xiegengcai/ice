package com.melinkr.ice.service;

import com.melinkr.ice.IceErrorCode;
import com.melinkr.ice.response.IceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
@Service
public class InvokeServiceImpl implements InvokeService {
    @Autowired
    private BlacklistService blacklistService;
    @Override
    public IceError invokeLimit(String appKey, String method, String version, String clientIP, String sessionId) {
        if (blacklistService.isForbidden(clientIP)) {
            return new IceError(IceErrorCode.FORBIDDEN);
        }
        return null;
    }
}
