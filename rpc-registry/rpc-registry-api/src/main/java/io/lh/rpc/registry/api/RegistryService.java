package io.lh.rpc.registry.api;

import io.lh.rpc.protocol.meta.ServiceMeta;
import io.lh.rpc.registry.api.config.RegistryConfig;
import io.lh.rpc.spi.annotation.SPI;

import java.util.List;

/**
 * 注册发现类
 *
 * @author menglinghao
 */
@SPI
public interface RegistryService {

    /**
     * 服务注册
     *
     * @param serviceMeta the service meta
     * @throws Exception the exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务下线
     *
     * @param serviceMeta the service meta
     * @throws Exception the exception
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;


    /**
     * 服务发现
     *
     * @param serviceName     the service name
     * @param invokerHashCode the invoker hash code
     * @param ip              the ip 来源ip
     * @return 服务元数据 service meta
     * @throws Exception the exception
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode, String ip) throws Exception;

    /**
     * 服务销毁
     *
     * @throws Exception the exception
     */
    void destroy() throws Exception;

    /**
     * 服务初始化方法，注意这里的方法体是否可以省略呢？
     *
     * @param registryConfig the registry config
     * @throws Exception the exception
     */
    default void init(RegistryConfig registryConfig) throws Exception {
    }

    /**
     * 服务选择，从服务列表选择具体服务
     *
     */
    ServiceMeta select(List<ServiceMeta> serviceMetaList, int invokerHashCode, String sourceIp);
}
