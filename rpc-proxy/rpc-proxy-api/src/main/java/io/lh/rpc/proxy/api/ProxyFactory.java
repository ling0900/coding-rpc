package io.lh.rpc.proxy.api;
import io.lh.rpc.proxy.api.config.*;
import io.lh.rpc.spi.annotation.SPI;

/**
 * 工厂模式
 * 一般起名字就是BeanFactory的形式。
 * 有了工厂，就需要实现它。
 */
@SPI
public interface ProxyFactory {

    /**
     * 获取代理对象
     */
    <T> T getProxy(Class<T> tClass);

    /**
     * 默认的初始化方法
     */
    <T> void init(ProxyConfig<T> proxyConfig);
}
