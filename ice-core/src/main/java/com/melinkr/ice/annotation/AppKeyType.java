package com.melinkr.ice.annotation;
/**
 * appKey枚举
 */
public enum AppKeyType {
    YES, NO;
    public static boolean isOpenAppKey(AppKeyType type) {
        if (NO == type) {
            return false;
        }
        return true;
    }
}