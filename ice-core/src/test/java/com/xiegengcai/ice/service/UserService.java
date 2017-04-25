package com.xiegengcai.ice.service;

import com.xiegengcai.ice.IceSession;
import com.xiegengcai.ice.annotation.HttpAction;
import com.xiegengcai.ice.annotation.ServiceMethod;
import com.xiegengcai.ice.annotation.SignType;
import com.xiegengcai.ice.annotation.WebService;
import com.xiegengcai.ice.request.LoginRequest;
import com.xiegengcai.ice.utils.RequestUtil;

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

    @ServiceMethod(method = "user.login", version = "1.0", sign = SignType.YES, httpAction = HttpAction.POST)
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
