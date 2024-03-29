package io.lh.rpc.consumer;

import io.lh.rpc.proxy.api.ProxyFactory;
import io.lh.rpc.proxy.api.async.IAsyncObjectProxy;
import io.lh.rpc.proxy.api.config.ProxyConfig;
import io.lh.rpc.proxy.api.object.ObjectProxy;
import io.lh.rpc.registry.api.RegistryService;
import io.lh.rpc.registry.api.config.RegistryConfig;
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
     * The Service version.
     */
    private String serviceVersion;

    /**
     * The Service group.
     */
    private String serviceGroup;

    /**
     * The Timeout.
     */
    private long timeout;


    /**
     * The Serialization type.
     */
    private String serializationType;

    /**
     * The Async.
     */
    private boolean async;

    /**
     * The Oneway.
     */
    private boolean oneway;

    /**
     * 注册服务
     */
    private RegistryService registryService;

    /**
     * The Registry address.
     */
    private String registryAddress;

    /**
     * The Registry type.
     */
    private String registryType;

    /**
     * The Proxy.
     */
    private String proxy;

    /**
     * The Heartbeat interval.
     */
    private int heartbeatInterval;

    /**
     * The Scan not active channel interval.
     */
    private int scanNotActiveChannelInterval;

    /**
     * The Retry interval.
     */
    private int retryInterval = 1000;

    /**
     * The Retry times.
     */
    private int retryTimes = 3;

    private boolean enableResultCache;

    private int resultCacheExpire;

    // 直连服务
    private boolean enableDirectServer;

    private String directServerUrl;


    /**
     * Instantiates a new Rpc client.
     *
     * @param serviceVersion               the service version
     * @param serviceGroup                 the service group
     * @param timeout                      the timeout
     * @param serializationType            the serialization type
     * @param async                        the async
     * @param oneway                       the oneway
     * @param registryAddress              the registry address
     * @param registryType                 the registry type
     * @param proxy                        the proxy
     * @param registryLoadBalanceType      the registry load balance type
     * @param heartbeatInterval            the heartbeat interval
     * @param scanNotActiveChannelInterval the scan not active channel interval
     * @param retryInterval                the retry interval
     * @param retryTimes                   the retry times
     */
    public RpcClient(String serviceVersion, String serviceGroup,
                     long timeout, String serializationType, boolean async,
                     boolean oneway, String registryAddress, String registryType, String proxy,
                     String registryLoadBalanceType, int heartbeatInterval, int scanNotActiveChannelInterval,
                     int retryInterval, int retryTimes, boolean enableResultCache, int resultCacheExpire,
                     boolean enableDirectServer, String directServerUrl) {

        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.proxy = proxy;
        this.heartbeatInterval = heartbeatInterval;
        this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        this.retryInterval = retryInterval;
        this.retryTimes = retryTimes;
        this.enableResultCache = enableResultCache;
        this.resultCacheExpire = resultCacheExpire;
        this.enableDirectServer = enableDirectServer;
        this.directServerUrl = directServerUrl;
        this.registryService = this.getRegistryService(registryAddress, registryType, registryLoadBalanceType);
    }

    /**
     * 创建一个代理类！利用工厂+模版模式！
     *
     * @param <T>            the type parameter
     * @param interfaceClass the interface class
     * @return t t
     */
    public <T> T create(Class<T> interfaceClass) {
        // 利用模版模式进行精简。
        // 实例化
        ProxyFactory proxyFactory = ExtensionLoader.getExtension(ProxyFactory.class, proxy);

        // 进行初始化
        proxyFactory.init(new ProxyConfig(interfaceClass, serviceVersion, serviceGroup, timeout,
                RpcConsumer.getInstance()
                        .setHeartbeatInterval(heartbeatInterval)
                        .setHeartbeatInterval(heartbeatInterval)
                        .setRetryInterval(retryInterval)
                        .setDirectServerUrl(directServerUrl)
                        .setEnableDirectServer(enableDirectServer)
                        .setRetryTimes(retryTimes)
                        .setScanNotActiveChannelInterval(scanNotActiveChannelInterval),
                serializationType, async, oneway, registryService, enableResultCache, resultCacheExpire));
        // 工厂返回对应的实例
        return proxyFactory.getProxy(interfaceClass);
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        RpcConsumer.getConsumerInstance(heartbeatInterval, scanNotActiveChannelInterval, retryInterval, retryTimes)
                .close();
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
                timeout, RpcConsumer.getConsumerInstance(heartbeatInterval, scanNotActiveChannelInterval, retryInterval, retryTimes)
                , async, oneway, registryService, enableResultCache, resultCacheExpire);
    }

    /**
     * Gets registry service.
     *
     * @param registryAddress         the registry address
     * @param registryType            the registry type
     * @param registryLoadBalanceType the registry load balance type
     * @return the registry service
     */
    private RegistryService getRegistryService(String registryAddress, String registryType, String registryLoadBalanceType) {
        if (StringUtils.isEmpty(registryType)) {
            throw new IllegalArgumentException("注册类型为  null");
        }

        // 这里整合了spi拓展机制。
        RegistryService registryService = ExtensionLoader.getExtension(RegistryService.class, registryType);

        try {
            registryService.init(new RegistryConfig(registryAddress, registryType, registryLoadBalanceType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return registryService;
    }
}
