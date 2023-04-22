package io.lh.rpc.proxy.bytebuddy;

import io.lh.rpc.spi.annotation.SPIClass;
import io.lh.rpc.proxy.api.BaseProxyFactory;
import io.lh.rpc.proxy.api.ProxyFactory;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

@SPIClass
public class ByteBuddyProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory{

    private final Logger logger = LoggerFactory.getLogger(ByteBuddyProxyFactory.class);
    @Override
    public <T> T getProxy(Class<T> tClass) {

        logger.warn("进入动态代理，基于 bytebuddy方式的");

        try {
            return (T) new ByteBuddy()
                    .subclass(Object.class)
                    .implement(tClass)
                    .intercept(InvocationHandlerAdapter.of(objectProxy))
                    .make()
                    .load(ByteBuddyProxyFactory.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("bytebuddy 动态代理出现问题了");
        }
        return null;
    }
}
