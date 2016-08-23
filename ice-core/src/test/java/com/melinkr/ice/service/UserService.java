package com.melinkr.ice.service;

import com.melinkr.ice.IceSession;
import com.melinkr.ice.annotation.ServiceMethod;
import com.melinkr.ice.annotation.SessionType;
import com.melinkr.ice.annotation.SignType;
import com.melinkr.ice.annotation.WebService;
import com.melinkr.ice.request.LoginRequest;
import com.melinkr.ice.utils.RequestUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/22.
 */
@WebService
public class UserService {

    @Resource
    private SessionService sessionService;

    @ServiceMethod(method = "user.login", version = "1.0", session = SessionType.NO, sign = SignType.YES)
    public Map<String,Object> login(LoginRequest request){
        String sessionId = RequestUtil.getUuid();
        Map<String,Object> result = new HashMap<>();
        result.put("accountName", request.getAccountName());
        result.put("password", request.getPassword());
        result.put("sessionId", sessionId);
        IceSession session = new IceSession();
        session.setAttribute("accountName", request.getAccountName());
        sessionService.setSession(sessionId, session);
        return result;
    }
}
