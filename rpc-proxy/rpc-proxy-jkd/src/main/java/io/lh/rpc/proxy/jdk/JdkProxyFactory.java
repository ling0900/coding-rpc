package io.lh.rpc.proxy.jdk;

import io.lh.rpc.proxy.api.consumer.Consumer;
import io.lh.rpc.proxy.api.object.ObjectProxy;

import java.lang.reflect.Proxy;

/**
 * <p>描述：</p>
 * <p>版本：1.0.0</p>
 * <p>@author：lh</p>
 * <p>创建时间：2023/02/21</p>
 */
public class JdkProxyFactory <T> {

    /**
     *
     */
    private String serviceVersion;

    /**
     *
     */
    private String serviceGroup;

    /**
     *
     */
    private long timeout = 15000;

    /**
     *
     */
    private Consumer consumer;

    /**
     *
     */
    private String serializationType;

    /**
     *
     */
    private boolean async;

    /**
     *
     */
    private boolean oneway;

    /**
     * @param serviceVersion
     * @param serviceGroup
     * @param timeout
     * @param consumer
     * @param serializationType
     * @param async
     * @param oneway
     */
    public JdkProxyFactory(String serviceVersion, String serviceGroup, long timeout, String serializationType, Consumer consumer,
                           boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    /**
     * @param clazz
     * @return {@link T}
     */
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new ObjectProxy<T>(clazz, serviceVersion, serviceGroup, serializationType, timeout, consumer,
                        async, oneway));
    }
}
