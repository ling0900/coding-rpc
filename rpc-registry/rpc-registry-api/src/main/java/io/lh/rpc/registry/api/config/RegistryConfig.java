package io.lh.rpc.registry.api.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 注册中心的配置类
 */
@Data
public class RegistryConfig implements Serializable {

    private static final long serialVersionUID = 5284424099245107400L;

    private String registryAddr;

    private String registryType;

    private String registryLoadBalanceType;

    public RegistryConfig(String registryAddr, String registryType, String registryLoadBalanceType) {
        this.registryLoadBalanceType = registryLoadBalanceType;
        this.registryAddr = registryAddr;
        this.registryType = registryType;
    }
}
