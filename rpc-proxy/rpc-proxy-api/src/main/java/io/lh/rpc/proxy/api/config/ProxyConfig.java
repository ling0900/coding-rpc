package io.lh.rpc.proxy.api.config;

import io.lh.rpc.proxy.api.consumer.Consumer;

import java.io.Serializable;

/**
 * 代理配置类
 * 注意这个名字⚠️
 *
 * @author lh
 */
public class ProxyConfig<T> implements Serializable {

    private static final long serialVersionUID = 5676742886687617529L;

    /**
     * 接口的Class实例
     */
    private Class<T> clazz;

    /**
     * 版本号
     */
    private String serviceVersion;

    /**
     * 服务组
     */
    private String serviceGroup;

    /**
     * 超时 时间
     */
    private long timeout;

    /**
     * 消费者 【接口】
     */
    private Consumer consumer;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 同步与否
     */
    private boolean async;

    private boolean oneway;

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public ProxyConfig(Class<T> clazz, String serviceVersion, String serviceGroup, long timeout, Consumer consumer, String serializationType, boolean async, boolean oneway) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }
}
