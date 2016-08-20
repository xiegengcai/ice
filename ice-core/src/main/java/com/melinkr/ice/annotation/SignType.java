package com.melinkr.ice.annotation;

/**
 * <pre>
 * 是否需求进行签名校验,用于方法名上
 * </pre>
 *
 * @version 1.0
 */
public enum SignType {

    YES, NO;

    public static boolean isNoSign(SignType type) {
        if (NO == type) {
            return false;
        }
        return true;
    }
}
