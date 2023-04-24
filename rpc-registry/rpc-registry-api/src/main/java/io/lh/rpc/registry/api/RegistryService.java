package io.lh.rpc.registry.api;

import io.lh.rpc.protocol.meta.ServiceMeta;
import io.lh.rpc.registry.api.config.RegistryConfig;
import io.lh.rpc.spi.annotation.SPI;

/**
 * 注册发现类
 */
@SPI
public interface RegistryService {

    /**
     * 服务注册
     * @param serviceMeta
     * @throws Exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务下线
     * @param serviceMeta
     * @throws Exception
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;


    /**
     * 服务发现
     * @param serviceName
     * @param invokerHashCode
     * @return 服务元数据
     * @throws Exception
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    void destroy() throws Exception;

    /**
     * 初始化方法，注意这里的方法体是否可以省略呢？
     * @param registryConfig
     * @throws Exception
     */
    default void init(RegistryConfig registryConfig) throws Exception {
    }
}
