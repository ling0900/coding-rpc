package io.lh.rpc.commom.utils;


import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 * The type Ip utils.
 */
@Slf4j
public class IpUtils {

    /**
     * Gets local inet address.
     *
     * @return the local inet address
     */
    public static InetAddress getLocalInetAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (Exception e) {
            log.error("获取 local ip address throws exception: {}",
                    e);
        }
        return null;
    }

    /**
     * Gets local address.
     *
     * @return the local address
     */
    public static String getLocalAddress() {
        return getLocalInetAddress().toString();
    }

    /**
     * Gets local host name.
     *
     * @return the local host name
     */
    public static String getLocalHostName() {
        return getLocalInetAddress().getHostName();
    }

    /**
     * Gets local host ip.
     *
     * @return the local host ip
     */
    public static String getLocalHostIp() {
        return getLocalInetAddress().getHostAddress();
    }
}
