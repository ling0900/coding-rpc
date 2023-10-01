package io.lh.rpc.spring.boot.provider.config;


import lombok.Data;

/**
 * The type Spring boot provider config.
 */
@Data
public final class SpringBootProviderConfig {

    /**
     * The Server address.
     */
    private String serverAddress;
    /**
     * The Registry address.
     * 注册中心的地址
     */
    private String registryAddress;

    /**
     * 这个是注册到注册中心的 服务地址
     */
    private String serverRegistryAddress;

    /**
     * The Registry type.
     */
    private String registryType;
    /**
     * The Registry load balance type.
     */
    private String registryLoadBalanceType;
    /**
     * The Reflect type.
     */
    private String reflectType;

    /**
     * The Heartbeat interval.
     */
    private int heartbeatInterval;

    /**
     * The Scan not active channel interval.
     */
    private int scanNotActiveChannelInterval;

    private boolean enableResultCache;

    private int resultCacheExpire;

    /**
     * Instantiates a new Spring boot provider config.
     */
    public SpringBootProviderConfig() {
    }

    /**
     * Instantiates a new Spring boot provider config.
     *
     * @param serverAddress                the server address
     * @param registryAddress              the registry address
     * @param registryType                 the registry type
     * @param registryLoadBalanceType      the registry load balance type
     * @param reflectType                  the reflect type
     * @param heartbeatInterval            the heartbeat interval
     * @param scanNotActiveChannelInterval the scan not active channel interval
     */
    public SpringBootProviderConfig(final String serverAddress, final String serverRegistryAddress, final String registryAddress, final String registryType,
                                    final String registryLoadBalanceType, final String reflectType, final int heartbeatInterval,
                                    int scanNotActiveChannelInterval, boolean enableResCache, int resultCacheExpire) {
        this.serverAddress = serverAddress;
        this.registryAddress = registryAddress;
        this.registryType = registryType;
        this.registryLoadBalanceType = registryLoadBalanceType;
        this.reflectType = reflectType;
        if (heartbeatInterval > 0){
            this.heartbeatInterval = heartbeatInterval;
        }
        this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        this.serverRegistryAddress = serverRegistryAddress;
        this.enableResultCache = enableResCache;
        this.resultCacheExpire = resultCacheExpire;
    }
}
