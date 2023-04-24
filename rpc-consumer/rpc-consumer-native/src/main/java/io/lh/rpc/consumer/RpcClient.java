package io.lh.rpc.consumer;

import io.lh.rpc.proxy.api.ProxyFactory;
import io.lh.rpc.proxy.api.config.*;
import io.lh.rpc.proxy.api.async.IAsyncObjectProxy;
import io.lh.rpc.proxy.api.object.ObjectProxy;
import io.lh.rpc.proxy.jdk.JdkProxyFactory;
import io.lh.rpc.registry.api.RegistryService;
import io.lh.rpc.registry.api.config.RegistryConfig;
import io.lh.rpc.registyr.zookeeper.ZookeeperRegistryService;
import io.lh.rpc.spi.loader.ExtensionLoader;
import io.lhrpc.consumer.common.RpcConsumer;
import lombok.Data;
import org.springframework.util.StringUtils;


/**
 * The type Rpc client.
 */
@Data
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

    /**
     * 注册服务
     */
    private RegistryService registryService;

    private String registryAddress;

    private String registryType;

    private String proxy;


    /**
     * Instantiates a new Rpc client.
     *
     * @param serviceVersion    the service version
     * @param serviceGroup      the service group
     * @param timeout           the timeout
     * @param serializationType the serialization type
     * @param async             the async
     * @param oneway            the oneway
     * @param registryAddress   the registry address
     * @param registryType      the registry type
     */
    public RpcClient(String serviceVersion, String serviceGroup,
                     long timeout, String serializationType, boolean async,
                     boolean oneway, String registryAddress, String registryType, String proxy,
                     String registryLoadBalanceType) {

        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.proxy = proxy;
        this.registryService = this.getRegistryService(registryAddress, registryType, registryLoadBalanceType);
    }

    /**
     * 创建一个代理类！利用工厂+模版模式！
     *
     * @param <T>            the type parameter
     * @param interfaceClass the interface class
     * @return t
     */
    public <T> T create(Class<T> interfaceClass) {
        // 利用模版模式进行精简。
        // 实例化
        ProxyFactory proxyFactory = ExtensionLoader.getExtension(ProxyFactory.class, proxy);

        // 进行初始化
        proxyFactory.init(new ProxyConfig(interfaceClass, serviceVersion, serviceGroup,
                timeout, RpcConsumer.getConsumerInstance(), serializationType, async, oneway, registryService));
        // 工厂返回对应的实例
        return proxyFactory.getProxy(interfaceClass);
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        RpcConsumer.getConsumerInstance().close();
    }

    /**
     * 创建一个异步的动态代理对象
     *
     * @param <T>            the type parameter
     * @param interfaceClass the interface class
     * @return async object proxy
     */
    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass, serviceVersion, serviceGroup, serializationType,
                timeout, RpcConsumer.getConsumerInstance(), async, oneway, registryService);
    }

    private RegistryService getRegistryService(String registryAddress, String registryType, String registryLoadBalanceType) {
        if (StringUtils.isEmpty(registryType)) {
            throw new IllegalArgumentException("注册类型为  null");
        }
        // 不够完善的
        RegistryService registryService = ExtensionLoader.getExtension(RegistryService.class, registryType);

        try {
            registryService.init(new RegistryConfig(registryAddress, registryType, registryLoadBalanceType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return registryService;
    }
}
