package io.lh.rpc.proxy.jdk;

import io.lh.rpc.proxy.api.BaseProxyFactory;
import io.lh.rpc.proxy.api.ProxyFactory;
import io.lh.rpc.proxy.api.consumer.Consumer;
import io.lh.rpc.proxy.api.object.ObjectProxy;

import java.lang.reflect.Proxy;

/**
 * <p>描述：</p>
 * <p>版本：1.0.0</p>
 * <p>@author：lh</p>
 * <p>创建时间：2023/02/21</p>
 */
public class JdkProxyFactory <T> extends BaseProxyFactory<T> implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(
                tClass.getClassLoader(),
                new Class<?>[]{tClass},
                objectProxy
        );
    }
}
