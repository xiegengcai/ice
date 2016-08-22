package com.melinkr.ice.service;

import com.google.common.net.InetAddresses;
import com.melinkr.ice.IceErrorCode;
import com.melinkr.ice.InvokeService;
import com.melinkr.ice.config.IceServerConfig;
import com.melinkr.ice.response.IceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
@Service
public class InvokeServiceImpl implements InvokeService {
    @Autowired
    private IceServerConfig iceServerConfig;
    @Override
    public IceError invokeLimit(String appKey, String method, String version, String clientIP, String sessionId) {
        InetAddress clientInetAddress = InetAddresses.forString(clientIP);
        for (String ip : iceServerConfig.ipBlacklist()) {
            if (!StringUtils.hasText(ip)) {
                continue;
            }
            if (clientIP.equals(ip)) {
                return new IceError(IceErrorCode.FORBIDDEN);
            }
            InetAddress configInetAddress = InetAddresses.forString(ip);
            // 客户端IP是IPV4，配置的是IPV6
            if (clientInetAddress instanceof Inet4Address && configInetAddress instanceof Inet6Address) {
                if (clientIP.equals(InetAddresses.get6to4IPv4Address((Inet6Address) configInetAddress).getHostAddress())) {
                    return new IceError(IceErrorCode.FORBIDDEN);
                }
            }
            // 客户端IP是IPV6，配置的是IPV4
            if (clientInetAddress instanceof Inet6Address && configInetAddress instanceof Inet4Address) {
                if (ip.equals(InetAddresses.get6to4IPv4Address((Inet6Address) clientInetAddress).getHostAddress())) {
                    return new IceError(IceErrorCode.FORBIDDEN);
                }
            }
        }
        return null;
    }
}
