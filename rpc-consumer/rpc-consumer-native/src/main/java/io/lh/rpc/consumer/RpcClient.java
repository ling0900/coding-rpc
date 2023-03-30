package io.lh.rpc.consumer;

import io.lh.rpc.proxy.api.ProxyFactory;
import io.lh.rpc.proxy.api.config.*;
import io.lh.rpc.proxy.api.async.IAsyncObjectProxy;
import io.lh.rpc.proxy.api.object.ObjectProxy;
import io.lh.rpc.proxy.jdk.JdkProxyFactory;
import io.lhrpc.consumer.common.RpcConsumer;

public class RpcClient {
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
    private long timeout;


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

    public RpcClient(String serviceVersion, String serviceGroup,
                     long timeout, String serializationType, boolean async, boolean oneway) {

        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    public <T> T create(Class<T> interfaceClass) {
        // 利用模版模式进行精简。
        ProxyFactory proxyFactory = new JdkProxyFactory<T>();
        proxyFactory.init(new ProxyConfig(interfaceClass, serviceVersion, serviceGroup,
                timeout, RpcConsumer.getConsumerInstance(), serializationType, async, oneway));
        return proxyFactory.getProxy(interfaceClass);
    }

    public void shutdown() {
        RpcConsumer.getConsumerInstance().close();
    }

    /**
     *
     * @param interfaceClass
     * @return
     * @param <T>
     */
    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass, serviceVersion, serviceGroup, serializationType,
                timeout, RpcConsumer.getConsumerInstance(), async, oneway);
    }
}
