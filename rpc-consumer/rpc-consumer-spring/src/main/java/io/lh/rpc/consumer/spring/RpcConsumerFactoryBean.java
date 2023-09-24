package io.lh.rpc.consumer.spring;

import io.lh.rpc.consumer.RpcClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * The type Rpc reference bean.
 * FactoryBean 实现
 * <p>
 * 重写FactoryBean接⼝的getObject()⽅法和getObjectType()⽅法。
 * 在getObject()⽅法中返回init()⽅法中通过RpcClient对象创建的代理对象；
 * 在getObjectType()⽅法中返回了代理对象的Class类型。
 * 在init()⽅法中，⾸先调⽤RpcClient类的构造⽅法创建了RpcClient对象，再通过RpcClient对象的
 * create()⽅法创建了代理对象，并将代理对象赋值给object
 * </p>
 */
@Slf4j
public class RpcConsumerFactoryBean implements FactoryBean<Object> {

    /**
     * 接口类型
     */
    private Class<?> interfaceClass;
    /**
     * 版本号
     */
    private String version;
    /**
     * 注册中心类型：zookeeper/nacos/apoll/etcd/eureka
     */
    private String registryType;

    /**
     * 负载均衡类型
     */
    private String loadBalanceType;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 注册中心地址
     */
    private String registryAddress;
    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 服务分组
     */
    private String group;
    /**
     * 是否异步
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;
    /**
     * 代理方式
     */
    private String proxy;
    /**
     * 生成的代理对象
     */
    private Object object;

    /**
     * 扫描空闲连接时间，默认60秒
     */
    private int scanNotActiveChannelInterval;

    /**
     * 心跳检测时间
     */
    private int heartbeatInterval;

    /**
     * The Retry interval.
     * 重试间隔时间
     */
    private int retryInterval = 1000;

    /**
     * The Retry times.
     */
    private int retryTimes = 3;

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    /**
     * Init.
     *
     * @throws Exception the exception
     */
    public void init() throws Exception {
        log.info("执行RpcConsumerFactoryBean的init()方法");
        log.info("interfaceClass={}", interfaceClass);
        // DemoService demoService = rpcClient.create(DemoService.class);
        RpcClient rpcClient = new RpcClient(
                version, group, timeout, serializationType, async, oneway, registryAddress, registryType, proxy, loadBalanceType, heartbeatInterval,
                scanNotActiveChannelInterval, retryInterval, retryTimes);
        this.object = rpcClient.create(interfaceClass);
    }

    /**
     * Gets interface class.
     *
     * @return the interface class
     */
    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    /**
     * Sets interface class.
     *
     * @param interfaceClass the interface class
     */
    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets registry type.
     *
     * @return the registry type
     */
    public String getRegistryType() {
        return registryType;
    }

    /**
     * Sets registry type.
     *
     * @param registryType the registry type
     */
    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    /**
     * Gets load balance type.
     *
     * @return the load balance type
     */
    public String getLoadBalanceType() {
        return loadBalanceType;
    }

    /**
     * Sets load balance type.
     *
     * @param loadBalanceType the load balance type
     */
    public void setLoadBalanceType(String loadBalanceType) {
        this.loadBalanceType = loadBalanceType;
    }

    /**
     * Gets serialization type.
     *
     * @return the serialization type
     */
    public String getSerializationType() {
        return serializationType;
    }

    /**
     * Sets serialization type.
     *
     * @param serializationType the serialization type
     */
    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    /**
     * Gets registry address.
     *
     * @return the registry address
     */
    public String getRegistryAddress() {
        return registryAddress;
    }

    /**
     * Sets registry address.
     *
     * @param registryAddress the registry address
     */
    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets group.
     *
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets group.
     *
     * @param group the group
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Is async boolean.
     *
     * @return the boolean
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * Sets async.
     *
     * @param async the async
     */
    public void setAsync(boolean async) {
        this.async = async;
    }

    /**
     * Is oneway boolean.
     *
     * @return the boolean
     */
    public boolean isOneway() {
        return oneway;
    }

    /**
     * Sets oneway.
     *
     * @param oneway the oneway
     */
    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    /**
     * Gets proxy.
     *
     * @return the proxy
     */
    public String getProxy() {
        return proxy;
    }

    /**
     * Sets proxy.
     *
     * @param proxy the proxy
     */
    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    /**
     * Sets object.
     *
     * @param object the object
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * Gets scan not active channel interval.
     *
     * @return the scan not active channel interval
     */
    public int getScanNotActiveChannelInterval() {
        return scanNotActiveChannelInterval;
    }

    /**
     * Sets scan not active channel interval.
     *
     * @param scanNotActiveChannelInterval the scan not active channel interval
     */
    public void setScanNotActiveChannelInterval(int scanNotActiveChannelInterval) {
        this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
    }

    /**
     * Gets heartbeat interval.
     *
     * @return the heartbeat interval
     */
    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    /**
     * Sets heartbeat interval.
     *
     * @param heartbeatInterval the heartbeat interval
     */
    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    /**
     * Gets retry interval.
     *
     * @return the retry interval
     */
    public int getRetryInterval() {
        return retryInterval;
    }

    /**
     * Sets retry interval.
     *
     * @param retryInterval the retry interval
     */
    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    /**
     * Gets retry times.
     *
     * @return the retry times
     */
    public int getRetryTimes() {
        return retryTimes;
    }

    /**
     * Sets retry times.
     *
     * @param retryTimes the retry times
     */
    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
