package io.lh.rpc.proxy.api;

import io.lh.rpc.proxy.api.object.ObjectProxy;
import io.lh.rpc.proxy.api.config.*;

/**
 * 这个可以看作是一个模版模式。
 * 就是一个基础实现类，用来在初始化方法中，通过proxyConfig类来完成ObjectProxy对象的创建，并进行属性初始化。
 * @param <T>
 */
public abstract class BaseProxyFactory<T> implements ProxyFactory {

    /**
     * 注意这里的修饰
     */
    protected ObjectProxy<T> objectProxy;

    /**
     * 这里 弄了多个构造器，要注意使用的时候！
     * @param proxyConfig
     * @param <T>
     */
    @Override
    public <T> void init(ProxyConfig<T> proxyConfig) {
        this.objectProxy = new ObjectProxy(
                proxyConfig.getClazz(),
                proxyConfig.getServiceVersion(),
                proxyConfig.getServiceGroup(),
                proxyConfig.getSerializationType(),
                proxyConfig.getTimeout(),
                proxyConfig.getConsumer(),
                proxyConfig.isAsync(),
                proxyConfig.isOneway(),
                proxyConfig.getRegistryService(),
                proxyConfig.isEnableResultCache(),
                proxyConfig.getResultCacheExpire());
    }
}
