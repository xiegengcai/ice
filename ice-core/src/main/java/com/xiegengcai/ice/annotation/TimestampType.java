package com.xiegengcai.ice.annotation;

/**
 * <pre>
 *   时间戳枚举
 * </pre>
 *
 * @version 1.0
 */
public enum TimestampType {
    YES, NO;
    public static boolean isOpenTimestamp(TimestampType type) {
        if (NO == type) {
            return false;
        } else {
            return true;
        }
    }
}
