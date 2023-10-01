package io.lh.rpc.consumer.spring;

import io.lh.rpc.consumer.RpcClient;
import lombok.Data;
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
@Data
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

    private RpcClient rpcClient;

    private boolean enableResultCache;

    private int resultCacheExpire;

    private boolean enableDirectServer;

    private String directServerUrl;

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
     */
    @SuppressWarnings("unchecked")
    public void init() {
        log.info("执行RpcConsumerFactoryBean的init()方法");
        log.info("interfaceClass={}", interfaceClass);
        // DemoService demoService = rpcClient.create(DemoService.class);
        rpcClient = new RpcClient(
                version, group, timeout, serializationType, async, oneway, registryAddress, registryType, proxy, loadBalanceType, heartbeatInterval,
                scanNotActiveChannelInterval, retryInterval, retryTimes, enableResultCache, resultCacheExpire, enableDirectServer, directServerUrl);
        this.object = rpcClient.create(interfaceClass);
    }

}
