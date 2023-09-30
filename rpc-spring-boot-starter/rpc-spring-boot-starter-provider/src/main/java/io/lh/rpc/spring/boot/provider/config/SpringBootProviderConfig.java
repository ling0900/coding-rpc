package io.lh.rpc.spring.boot.provider.config;


/**
 * The type Spring boot provider config.
 */
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
                                    int scanNotActiveChannelInterval) {
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
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public String getRegistryLoadBalanceType() {
        return registryLoadBalanceType;
    }

    public void setRegistryLoadBalanceType(String registryLoadBalanceType) {
        this.registryLoadBalanceType = registryLoadBalanceType;
    }

    public String getReflectType() {
        return reflectType;
    }

    public void setReflectType(String reflectType) {
        this.reflectType = reflectType;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public int getScanNotActiveChannelInterval() {
        return scanNotActiveChannelInterval;
    }

    public void setScanNotActiveChannelInterval(int scanNotActiveChannelInterval) {
        this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
    }

    public String getServerRegistryAddress() {
        return serverRegistryAddress;
    }

    public void setServerRegistryAddress(String serverRegistryAddress) {
        this.serverRegistryAddress = serverRegistryAddress;
    }
}
