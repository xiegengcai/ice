package com.melinkr.ice;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public interface SessionService {
    /**
     * 获取会话
     * @param  sessionId
     */
    Session getSession(String sessionId);

    /**
     * 设置session
     * @param  sessionId
     * @param session
     */
    void setSession(String sessionId ,Session session);
}
