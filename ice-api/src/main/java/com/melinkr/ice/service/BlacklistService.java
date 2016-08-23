package com.melinkr.ice.service;

import com.google.common.net.InetAddresses;
import com.melinkr.ice.IceErrorCode;
import com.melinkr.ice.response.IceError;
import org.springframework.util.StringUtils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Set;

/**
 * 黑名单服务接口
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/23.
 */
public interface BlacklistService {

    /**
     * 返回黑名单列表
     * @return
     */
    Set<String> blacklist();

    default boolean isForbidden(String clientIP) {
        InetAddress clientInetAddress = InetAddresses.forString(clientIP);
        for (String ip : blacklist()) {
            if (!StringUtils.hasText(ip)) {
                continue;
            }
            if (clientIP.equals(ip)) {
                return true;
            }
            InetAddress configInetAddress = InetAddresses.forString(ip);
            // 客户端IP是IPV4，配置的是IPV6
            if (clientInetAddress instanceof Inet4Address && configInetAddress instanceof Inet6Address) {
                if (clientIP.equals(InetAddresses.get6to4IPv4Address((Inet6Address) configInetAddress).getHostAddress())) {
                    return true;
                }
            }
            // 客户端IP是IPV6，配置的是IPV4
            if (clientInetAddress instanceof Inet6Address && configInetAddress instanceof Inet4Address) {
                if (ip.equals(InetAddresses.get6to4IPv4Address((Inet6Address) clientInetAddress).getHostAddress())) {
                    return true;
                }
            }
        }
        return false;
    }
}
