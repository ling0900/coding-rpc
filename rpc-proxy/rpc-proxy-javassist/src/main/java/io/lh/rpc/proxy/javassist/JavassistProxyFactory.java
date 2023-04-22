package io.lh.rpc.proxy.javassist;

import io.lh.rpc.proxy.api.BaseProxyFactory;
import io.lh.rpc.proxy.api.ProxyFactory;
import io.lh.rpc.spi.annotation.SPIClass;
import javassist.util.proxy.MethodHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * The type Javassist proxy factory.
 *
 * @param <T> the type parameter
 */
@SPIClass
public class JavassistProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {

    private final Logger logger = LoggerFactory.getLogger(JavassistProxyFactory.class);
    private javassist.util.proxy.ProxyFactory javassitProxyFactory = new javassist.util.proxy.ProxyFactory();


    @Override
    public <T> T getProxy(Class<T> tClass) {
        try {

            logger.warn("javassist 动态代理方式");

            javassitProxyFactory.setInterfaces(new Class[]{tClass});
            // 设置代理类父类
            javassitProxyFactory.setHandler(new MethodHandler() {
                @Override
                public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {

                    return objectProxy.invoke(self, thisMethod, args);

                }
            });
            // 字节码技术动态创建子类实例
            return (T) javassitProxyFactory.createClass().newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
