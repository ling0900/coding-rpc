package io.lh.rpc.proxy.api.config;

import io.lh.rpc.proxy.api.consumer.Consumer;
import io.lh.rpc.registry.api.RegistryService;
import lombok.Data;

import java.io.Serializable;

/**
 * 代理配置类
 * 注意这个名字⚠️
 *
 * @author lh
 */
@Data
public class ProxyConfig<T> implements Serializable {

    private static final long serialVersionUID = 5676742886687617529L;

    /**
     * 接口的Class实例
     */
    private Class<T> clazz;

    /**
     * 版本号
     */
    private String serviceVersion;

    /**
     * 服务组
     */
    private String serviceGroup;

    /**
     * 超时 时间
     */
    private long timeout;

    /**
     * 消费者 【接口】
     */
    private Consumer consumer;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 同步与否
     */
    private boolean async;

    private boolean oneway;

    private RegistryService registryService;

    private boolean enableResultCache;

    private int resultCacheExpire;

    public ProxyConfig(Class<T> clazz, String serviceVersion, String serviceGroup,
                       long timeout, Consumer consumer, String serializationType,
                       boolean async, boolean oneway,
                       RegistryService registryService, boolean enableResultCache, int resCacheExpire) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.registryService = registryService;
        this.enableResultCache = enableResultCache;
        this.resultCacheExpire = resCacheExpire;
    }
}
