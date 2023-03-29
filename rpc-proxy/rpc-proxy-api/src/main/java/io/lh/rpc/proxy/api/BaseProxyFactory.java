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

    @Override
    public <T> void init(ProxyConfig<T> proxyConfig) {
    }
}
