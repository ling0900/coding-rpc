package io.lh.rpc.proxy.jdk;

import io.lh.rpc.proxy.api.BaseProxyFactory;
import io.lh.rpc.proxy.api.ProxyFactory;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * <p>描述：JDK代理的工厂模式</p>
 * <p>版本：1.0.0</p>
 * <p>@author：lh</p>
 * <p>创建时间：2023/02/21</p>
 * @param <T> the type parameter
 */
@SPIClass
public class JdkProxyFactory <T> extends BaseProxyFactory<T> implements ProxyFactory {

    private final Logger logger = LoggerFactory.getLogger(JdkProxyFactory.class);

    @Override
    public <T> T getProxy(Class<T> tClass) {
        logger.warn("进入基于JDK动态代理");
        return (T) Proxy.newProxyInstance(
                tClass.getClassLoader(),
                new Class<?>[]{tClass},
                objectProxy
        );
    }
}
