package io.lh.rpc.registry.api.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 注册中心的配置类
 * @author menglinghao
 */
@Data
public class RegistryConfig implements Serializable {

    private static final long serialVersionUID = 5284424099245107400L;

    // 注册地址
    private String registryAddr;

    // 注册类型
    private String registryType;

    // 负载均衡类型
    private String registryLoadBalanceType;

    public RegistryConfig(String registryAddr, String registryType, String registryLoadBalanceType) {
        this.registryLoadBalanceType = registryLoadBalanceType;
        this.registryAddr = registryAddr;
        this.registryType = registryType;
    }
}
