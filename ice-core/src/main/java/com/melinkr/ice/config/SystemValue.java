package com.melinkr.ice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public class SystemValue {
    private static Logger logger = LoggerFactory.getLogger(SystemValue.class);

    public static final String KEY_PREV = "ice.system.";

    public static String getConfig(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (StringUtils.hasText(value)) {
            return value;
        }
        return defaultValue;
    }

    public static int getConfig(String key, int defaultValue) {
        String value = System.getProperty(key);
        int intValue = defaultValue;
        if (StringUtils.hasText(value)) {
            try {
                intValue = Integer.parseInt(value);
            } catch (Exception e) {
                logger.warn("arg:" + key
                        + " has parse error,it will be setted default value:"
                        + defaultValue);
            }
        }
        return intValue;
    }

    public static long getConfig(String key, long defaultValue) {
        String value = System.getProperty(key);
        long intValue = defaultValue;
        if (StringUtils.hasText(value)) {
            try {
                intValue = Long.parseLong(value);
            } catch (Exception e) {
                logger.warn("arg:" + key
                        + " has parse error,it will be setted default value:"
                        + defaultValue);
            }
        }
        return intValue;
    }

    public static boolean getConfig(String key, boolean defaultValue) {
        String value = System.getProperty(key);
        if (StringUtils.hasText(value)) {
            return Boolean.parseBoolean(value.trim());
        }
        return defaultValue;
    }

    /**
     * 系统是否开启签名验证
     */
    public static boolean isOpenSign() {
        return getConfig(KEY_PREV + "openSign", Boolean.FALSE);
    }

    /**
     * 系统是否开启签名验证
     */
    public static boolean isOpenAppKey() {
        return isOpenSign();
    }

    /**
     * 系统是否开会话
     */
    public static boolean isOpenSession() {
        return getConfig(KEY_PREV + "openSession", Boolean.FALSE);
    }

    /**
     * 系统是否开启时间戳验证
     */
    public static boolean isOpenTimestamp() {
        return getConfig(KEY_PREV + "openTimestamp", Boolean.FALSE);
    }

    /**
     * 请求时间与服务收到时间允许最大误差。单位：毫秒
     */
    public static long getTimestampValue() {
        return TimeUnit.SECONDS.toMillis(getConfig(KEY_PREV + "timestampValue", 60));
    }

    /**
     * 最大超时时间60s,单位秒
     */
    public static int getMaxTimeout() {
        return getConfig(KEY_PREV + "maxTimeout", 60);
    }

    /**
     * 缺省超时时间 30s,单位秒
     */
    public static int getDefaultTimeout() {
        return getConfig(KEY_PREV + "defaultTimeout", 30);
    }

}
