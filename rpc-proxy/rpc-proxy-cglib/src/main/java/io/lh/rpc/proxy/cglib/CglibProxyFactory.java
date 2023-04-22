package io.lh.rpc.proxy.cglib;

import io.lh.rpc.proxy.api.BaseProxyFactory;
import io.lh.rpc.proxy.api.ProxyFactory;
import io.lh.rpc.spi.annotation.SPIClass;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@SPIClass/*这个注解一定要有！*/
public class CglibProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {

    private final Logger logger = LoggerFactory.getLogger(CglibProxyFactory.class);
    private final Enhancer/*注意这里的所在的包*/ enhancer = new Enhancer();
    @Override
    public <T> T getProxy(Class<T> tClass) {
        logger.warn("进入cglib动态代理-------------------｜｜｜｜｜｜｜｜");
        enhancer.setInterfaces(new Class[]{tClass});
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return objectProxy.invoke(o, method, objects);
            }
        });
        return (T) enhancer.create();
    }
}
