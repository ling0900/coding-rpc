package io.lh.rpc.spring.boot.consumer.config;

import lombok.Data;

/**
 * The type Spring boot consumer config.
 */
@Data
public final class SpringBootConsumerConfig {

    /**
     * The Registry address.
     */
    private String registryAddress;
    /**
     * The Registry type.
     */
    private String registryType;
    /**
     * The Load balance type.
     */
    private String loadBalanceType;
    /**
     * The Proxy.
     */
    private String proxy;
    /**
     * The Version.
     */
    private String version;
    /**
     * The Group.
     */
    private String group;
    /**
     * The Serialization type.
     */
    private String serializationType;
    /**
     * The Timeout.
     */
    private int timeout;
    /**
     * The Async.
     */
    private boolean async;

    /**
     * The Oneway.
     */
    private boolean oneway;

    /**
     * The Heartbeat interval.
     */
    private int heartbeatInterval;

    /**
     * The Scan not active channel interval.
     */
    private int scanNotActiveChannelInterval;

    /**
     * The Retry interval.
     */
    private int retryInterval = 1000;

    /**
     * The Retry times.
     */
    private int retryTimes = 3;

    /**
     * Instantiates a new Spring boot consumer config.
     */
    public SpringBootConsumerConfig() {
    }


    /**
     * Instantiates a new Spring boot consumer config.
     *
     * @param registryAddress              the registry address
     * @param registryType                 the registry type
     * @param loadBalanceType              the load balance type
     * @param proxy                        the proxy
     * @param version                      the version
     * @param group                        the group
     * @param serializationType            the serialization type
     * @param timeout                      the timeout
     * @param async                        the async
     * @param oneway                       the oneway
     * @param heartbeatInterval            the heartbeat interval
     * @param scanNotActiveChannelInterval the scan not active channel interval
     * @param retryInterval                the retry interval
     * @param retryTimes                   the retry times
     */
    public SpringBootConsumerConfig(final String registryAddress, final String registryType, final String loadBalanceType,
                                    final String proxy, final String version, final String group, final String serializationType,
                                    final int timeout, final boolean async, final boolean oneway, final int heartbeatInterval,
                                    final int scanNotActiveChannelInterval, final int retryInterval, final int retryTimes) {
        this.registryAddress = registryAddress;
        this.registryType = registryType;
        this.loadBalanceType = loadBalanceType;
        this.proxy = proxy;
        this.version = version;
        this.group = group;
        this.serializationType = serializationType;
        this.timeout = timeout;
        this.async = async;
        this.oneway = oneway;
        if (heartbeatInterval > 0){
            this.heartbeatInterval = heartbeatInterval;
        }
        this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        this.retryInterval = retryInterval;
        this.retryTimes = retryTimes;
    }

}
