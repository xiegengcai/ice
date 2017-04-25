package com.xiegengcai.ice.annotation;

/**
 * <pre>
 * 功能说明：是否需求会话检查
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public enum SessionType {
    YES, NO;

    public static boolean isNeedInSession(SessionType type) {
        if (YES == type) {
            return true;
        }
        return false;
    }
}
