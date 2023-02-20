package io.lh.rpc.consumer;

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

    public RpcClient(String serviceVersion, String serviceGroup, long timeout, String serializationType, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    public <T> T create(Class<T> interfaceClass) {
        // 开发中，这里的参数错一个，都会导致出现奇怪的问题的！
        JdkProxyFactory<T> jdkProxyFactory = new JdkProxyFactory<>(serviceVersion, serviceGroup, timeout, serializationType,
                RpcConsumer.getConsumerInstance(), async, oneway);
        return jdkProxyFactory.getProxy(interfaceClass);
    }

    public void shutdown() {
        RpcConsumer.getConsumerInstance().close();
    }
}
