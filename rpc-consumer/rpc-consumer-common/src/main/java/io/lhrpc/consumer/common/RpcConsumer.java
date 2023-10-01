package io.lhrpc.consumer.common;

import io.lh.rpc.commom.exception.RpcException;
import io.lh.rpc.commom.helper.RpcServiceHelper;
import io.lh.rpc.commom.threadpool.ClientThreadPool;
import io.lh.rpc.commom.utils.IpUtils;
import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.constants.RpcConstantsCache;
import io.lh.rpc.loadbalancer.context.ConnectionsContext;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.meta.ServiceMeta;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.proxy.api.consumer.Consumer;
import io.lh.rpc.proxy.api.future.RpcFuture;
import io.lh.rpc.registry.api.RegistryService;
import io.lhrpc.consumer.common.handler.RpcConsumerHandler;
import io.lhrpc.consumer.common.helper.RpcConsumerHandlerHelper;
import io.lhrpc.consumer.common.initializer.RpcConsumerInitializer;
import io.lhrpc.consumer.common.manager.ConsumerConnectionManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The type Rpc consumer.
 * 非常核心的消费者类
 * 对使用者屏蔽了netty的链接的代码细节。
 *
 * @author lh
 */
public class RpcConsumer implements Consumer {

    /**
     * The Retry interval.
     * 重试间隔时间
     */
    private int retryInterval = 1000;

    /**
     * The Retry times.
     * 重试次数
     */
    private int retryTimes = 2;

    /**
     * The Current connect retry times.
     * 当前的重试次数。
     */
    private volatile int currentConnectRetryTimes = 3;

    /**
     * 定时任务，来定时发送心跳的
     * 后来做了增强的心跳，取消了用这种定时任务实现的，采用了更好的方式。
     */
    private ScheduledExecutorService executorService;

    /**
     * The Logger.
     */
    private final Logger LOGGER = LoggerFactory.getLogger(RpcConsumer.class);

    /**
     * The Bootstrap.
     */
    private final Bootstrap bootstrap;
    /**
     * The Event loop group.
     */
    private final EventLoopGroup eventLoopGroup;

    /**
     * The constant consumerInstance.
     */
    private static volatile RpcConsumer consumerInstance;
    private static volatile RpcConsumer instance;

    /**
     * The constant handlerMap.
     */
    private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();

    /**
     * The Heartbeat interval.
     */
    private int heartbeatInterval = 30000;

    /**
     * The Scan not active channel interval.
     * 扫描并移除空闲连接时间，默认60秒
     */
    private int scanNotActiveChannelInterval = 60000;

    private String localIp = "";

    // 直连服务
    private boolean enableDirectServer = false;

    // 直连服务的地址
    private String directServerUrl;


    private RpcConsumer() {
        localIp = IpUtils.getLocalHostIp();
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new RpcConsumerInitializer(heartbeatInterval));
        // 启动心跳
        this.startHeartBeat();
    }

    public RpcConsumer setHeartbeatInterval(int heartbeatInterval) {
        if (heartbeatInterval > 0){
            this.heartbeatInterval = heartbeatInterval;
        }
        return this;
    }

    public RpcConsumer setScanNotActiveChannelInterval(int scanNotActiveChannelInterval) {
        if (scanNotActiveChannelInterval > 0){
            this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        }
        return this;
    }

    public RpcConsumer setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval <= 0 ? RpcConstants.DEFAULT_RETRY_INTERVAL : retryInterval;
        return this;
    }

    public RpcConsumer setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes <= 0 ? RpcConstants.DEFAULT_RETRY_TIMES : retryTimes;
        return this;
    }


    /**
     * Instantiates a new Rpc consumer.
     *
     * @param heartbeatInterval            the heartbeat interval
     * @param scanNotActiveChannelInterval the scan not active channel interval
     * @param retryInterval                the retry interval
     * @param retryTimes                   the retry times
     */
    private RpcConsumer(int heartbeatInterval, int scanNotActiveChannelInterval, int retryInterval, int retryTimes) {
        // 启动心跳～～这里可以优化的，线程池那里！
        LOGGER.info("开始调用heartBeat方法======");
        if (heartbeatInterval > 0) this.heartbeatInterval = heartbeatInterval;
        if (scanNotActiveChannelInterval > 0) this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        this.retryInterval = retryInterval <= 0 ?
                RpcConstants.DEFAULT_RETRY_INTERVAL : retryInterval;
        this.retryTimes = retryTimes <= 0 ?
                RpcConstants.DEFAULT_RETRY_TIMES : retryTimes;
        this.startHeartBeat();

        bootstrap = new Bootstrap();
        // 这里只有一个
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcConsumerInitializer(heartbeatInterval));
    }

    /**
     * 单例模式
     * 双重检测的单例模式
     *
     * @param heartbeatInterval            the heartbeat interval
     * @param scanNotActiveChannelInterval the scan not active channel interval
     * @param retryInterval                the retry interval
     * @param retryTimes                   the retry times
     * @return the consumer instance
     */
    public static RpcConsumer getConsumerInstance(int heartbeatInterval, int scanNotActiveChannelInterval, int retryInterval, int retryTimes) {
        if (consumerInstance == null) {
            synchronized (RpcConsumer.class) {
                if (consumerInstance == null) {
                    consumerInstance = new RpcConsumer(heartbeatInterval,
                            scanNotActiveChannelInterval, retryInterval, retryTimes);
                }
            }
        }
        return consumerInstance;
    }

    /**
     * Close.优雅关闭
     */
    public void close() {
        RpcConsumerHandlerHelper.closeRpcClientHandler();
        eventLoopGroup.shutdownGracefully();
        // 关闭线程池，当netty服务关闭后
        ClientThreadPool.shutdown();
        executorService.shutdown();
    }

    /**
     * Send request msg.
     *
     * @param requestRpcProtocol the request rpc protocol
     * @return the rpc future
     * @throws InterruptedException the interrupted exception
     */
    public RpcFuture sendRequestMsg(RpcProtocol<RpcRequest> requestRpcProtocol) throws InterruptedException {

        // 地址 这个是写死的，其实需要动态。
        String serviceAddress = "127.0.0.1";
        int port = 27780;

        String remoteServiceKey = serviceAddress.concat("-").concat(String.valueOf(port));
        RpcConsumerHandler rpcConsumerHandler = handlerMap.get(remoteServiceKey);

        // 判断是否存在，map
        if (rpcConsumerHandler == null) {
            rpcConsumerHandler = getRpcConsumerHandler(serviceAddress, port);
            handlerMap.put(remoteServiceKey, rpcConsumerHandler);
        } else if (!rpcConsumerHandler.getChannel().isActive()) {
            rpcConsumerHandler.close();
            // 从新获取
            rpcConsumerHandler = getRpcConsumerHandler(serviceAddress, port);
            handlerMap.put(remoteServiceKey, rpcConsumerHandler);
        }

        RpcRequest request = requestRpcProtocol.getBody();
        return rpcConsumerHandler.sendRequestMessage(requestRpcProtocol, request.isAsync(), request.isOneWay());
    }

    /**
     * Gets rpc consumer handler.
     *
     * @param serviceAddress the service address
     * @param port           the port
     * @return the rpc consumer handler
     * @throws InterruptedException the interrupted exception
     */
    private RpcConsumerHandler getRpcConsumerHandler(String serviceAddress, int port) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();
        channelFuture.addListener((ChannelFutureListener) listener -> {
            if (channelFuture.isSuccess()) {
                LOGGER.info("链接成功到{}：{}", serviceAddress, port);
            } else {
                LOGGER.info("链接fail到{}：{}", serviceAddress, port);
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
    }

    /**
     * Send request rpc future.
     *
     * @param protocol        the protocol
     * @param registryService the registry service
     * @return the rpc future
     * @throws Exception the exception
     */
    @Override
    public RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, RegistryService registryService) throws Exception {
        LOGGER.info("consumer sendRequest ====>");

        RpcRequest request = protocol.getBody();
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getVersion(), request.getGroup());
        Object[] parameters = request.getParameters();
        int invokerHashCode = (parameters == null || parameters.length < 0) ? serviceKey.hashCode() : parameters[0].hashCode();

        RpcFuture rpcFuture = null;
        RpcConsumerHandler consumerHandler = getRpcConsumerHandlerWithRetry(registryService, serviceKey, invokerHashCode);

        // consumerHandler
        if (consumerHandler != null) {
            rpcFuture = consumerHandler.sendRequestMessage(protocol, request.isAsync(), request.isOneWay());
        }
        return rpcFuture;
    }

    public RpcConsumer setEnableDirectServer(boolean enableDirectServer) {
        this.enableDirectServer = enableDirectServer;
        return this;
    }
    public RpcConsumer setDirectServerUrl(String directServerUrl) {
        this.directServerUrl = directServerUrl;
        return this;
    }

    /**
     * Start heart beat.
     */
    private void startHeartBeat() {

        LOGGER.info("进入心跳的方法内部");
        executorService = Executors.newScheduledThreadPool(2);

        executorService.scheduleAtFixedRate(() -> {
                    LOGGER.info("=====扫描不活跃的channel======");
                    ConsumerConnectionManager.scanNotActityChannel();
                }, 10, scanNotActiveChannelInterval, TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(() -> {
                    LOGGER.info("=======消费者发送心跳=======");
                    ConsumerConnectionManager.broadcastPingMessageFromConsumer();
                },
                3, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    public static RpcConsumer getInstance() {
        if (instance == null) {
            synchronized (RpcConsumer.class) {
                if (instance == null) {
                    instance = new RpcConsumer();
                }
            }
        }
        return instance;
    }


    /**
     * Gets rpc consumer handler with retry.
     *
     * @param serviceMeta the service meta
     * @return the rpc consumer handler with retry
     * @throws InterruptedException the interrupted exception
     */
    private RpcConsumerHandler getRpcConsumerHandlerWithRetry(ServiceMeta serviceMeta) throws InterruptedException{
        LOGGER.info("服务消费者连接服务提供者...");
        RpcConsumerHandler handler = null;
        try {
            handler = this.getRpcConsumerHandlerWithCache(serviceMeta);
        }catch (Exception e){
            //连接异常
            if (e instanceof ConnectException){
                //启动重试机制
                if (handler == null) {
                    if (currentConnectRetryTimes < retryTimes){
                        currentConnectRetryTimes++;
                        LOGGER.info("服务消费者连接服务提供者第【{}】次重试...", currentConnectRetryTimes);
                        handler = this.getRpcConsumerHandlerWithRetry(serviceMeta);
                        Thread.sleep(retryInterval);
                    }
                }
            }
        }
        return handler;
    }

    /**
     * Gets rpc consumer handler with cache.
     *
     * @param serviceMeta the service meta
     * @return the rpc consumer handler with cache
     * @throws InterruptedException the interrupted exception
     */
    private RpcConsumerHandler getRpcConsumerHandlerWithCache(ServiceMeta serviceMeta) throws InterruptedException{
        RpcConsumerHandler handler = RpcConsumerHandlerHelper.get(serviceMeta);
        // 缓存中无RpcClientHandler
        if (handler == null){
            handler = getRpcConsumerHandler(serviceMeta);
            RpcConsumerHandlerHelper.put(serviceMeta, handler);
         // 缓存中存在RpcClientHandler，但不活跃
        }else if (!handler.getChannel().isActive()){
            handler.close();
            handler = getRpcConsumerHandler(serviceMeta);
            RpcConsumerHandlerHelper.put(serviceMeta, handler);
        }
        return handler;
    }

    /**
     * Gets rpc consumer handler.
     * 优雅的重试机制
     * @param serviceMeta the service meta
     * @return the rpc consumer handler
     * @throws InterruptedException the interrupted exception
     */
    private RpcConsumerHandler getRpcConsumerHandler(ServiceMeta serviceMeta) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(serviceMeta.getServiceAddr(), serviceMeta.getServicePort()).sync();
        channelFuture.addListener((ChannelFutureListener) listener -> {
            if (channelFuture.isSuccess()) {
                LOGGER.info("connect rpc server {} on port {} success.", serviceMeta.getServiceAddr(), serviceMeta.getServicePort());
                // 添加连接信息，在服务消费者端记录每个服务提供者实例的连接次数
                ConnectionsContext.add(serviceMeta);
                // 连接成功，将当前连接重试次数设置为0
                currentConnectRetryTimes = 0;
            } else {
                LOGGER.error("connect rpc server {} on port {} failed.", serviceMeta.getServiceAddr(), serviceMeta.getServicePort());
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
    }

    private ServiceMeta getDirectServiceMeta(){
        if (StringUtils.isEmpty(directServerUrl)){
            throw new RpcException("direct server url is null ...");
        }
        if (!directServerUrl.contains(RpcConstants.IP_PORT_SPLIT)){
            throw new RpcException("direct server url not contains : ");
        }
        LOGGER.info("服务消费者直连服务提供者===>>> {}", directServerUrl);
        ServiceMeta serviceMeta = new ServiceMeta();
        String[] directServerUrlArray =
                directServerUrl.split(RpcConstants.IP_PORT_SPLIT);
        serviceMeta.setServiceAddr(directServerUrlArray[0]);
        serviceMeta.setServicePort(Integer.parseInt(directServerUrlArray[1]));
        return serviceMeta;
    }

    /**
     * Gets service meta.
     * 重试获取服务提供者元数据
     * @param registryService the registry service
     * @param serviceKey      the service key
     * @param invokerHashCode the invoker hash code
     * @return the service meta
     * @throws Exception the exception
     */
    private ServiceMeta getServiceMetaWithRetry(RegistryService registryService, String serviceKey, int invokerHashCode) throws Exception {
        // 首次获取元信息，获取不到就要进行重试
        LOGGER.info("获取生产者的元数据信息");
        ServiceMeta serviceMeta = registryService.discovery(serviceKey, invokerHashCode, localIp);

        if (serviceMeta == null) {
            for (int i = 0; i <= retryTimes; i++) {
                LOGGER.info("第{}次重试获取服务提供者", i + 1);
                serviceMeta = registryService.discovery(serviceKey, invokerHashCode, localIp);
                if (serviceMeta != null) break;
                Thread.sleep(retryInterval);
            }
        }

        return serviceMeta;
    }


    // 直连服务提供者或者结合重试获取服务元数据信息
    private ServiceMeta getDirectServiceMetaOrWithRetry(RegistryService registryService, String serviceKey, int invokerHashCode) throws Exception {
        ServiceMeta serviceMeta = null;
        if (enableDirectServer){
            serviceMeta = this.getServiceMeta(directServerUrl,registryService, invokerHashCode);
        }else {
            serviceMeta = this.getServiceMetaWithRetry(registryService, serviceKey, invokerHashCode);
        }
        return serviceMeta;
    }

    // 获取发送消息的handler
    private RpcConsumerHandler getRpcConsumerHandler(RegistryService registryService, String serviceKey, int invokerHashCode) throws Exception {
        ServiceMeta serviceMeta = this.getDirectServiceMetaOrWithRetry(registryService, serviceKey, invokerHashCode);
        RpcConsumerHandler handler = null;
        if (serviceMeta != null){
            handler = getRpcConsumerHandlerWithRetry(serviceMeta);
        }
        return handler;
    }

    private RpcConsumerHandler getRpcConsumerHandlerWithRetry(RegistryService registryService, String serviceKey, int invokerHashCode) throws Exception{
        LOGGER.info("获取服务消费者处理器...");
        RpcConsumerHandler handler = getRpcConsumerHandler(registryService, serviceKey, invokerHashCode);
        // 启动重试机制
        if (handler == null){
            for (int i = 1; i <= retryTimes; i++){
                LOGGER.info("获取服务消费者处理器第【{}】次重试...", i);
                handler = getRpcConsumerHandler(registryService, serviceKey,
                        invokerHashCode);
                if (handler != null){
                    break;
                }
                Thread.sleep(retryInterval);
            }
        }
        return handler;
    }

    private ServiceMeta getDirectServiceMeta(String directServerUrl) {
        ServiceMeta serviceMeta = new ServiceMeta();
        String[] directServerUrlArray =
                directServerUrl.split(RpcConstants.IP_PORT_SPLIT);
        serviceMeta.setServiceAddr(directServerUrlArray[0].trim());
        serviceMeta.setServicePort(Integer.parseInt(directServerUrlArray[1].trim()));
        return serviceMeta;
    }

    private ServiceMeta getDirectServiceMetaWithCheck(String directServerUrl){
        if (StringUtils.isEmpty(directServerUrl)){
            throw new RpcException("direct server url is null ...");
        }
        if (!directServerUrl.contains(RpcConstants.IP_PORT_SPLIT)){
            throw new RpcException("direct server url not contains : ");
        }
        return this.getDirectServiceMeta(directServerUrl);
    }

    private List<ServiceMeta> getMultiServiceMeta(String directServerUrl){
        List<ServiceMeta> serviceMetaList = new ArrayList<>();
        String[] directServerUrlArray =
                directServerUrl.split(RpcConstants.RPC_MULTI_DIRECT_SERVERS_SEPARATOR);
        if (directServerUrlArray != null && directServerUrlArray.length > 0){
            for (String directUrl : directServerUrlArray){
                serviceMetaList.add(getDirectServiceMeta(directUrl));
            }
        }
        return serviceMetaList;
    }

    private ServiceMeta getServiceMeta(String directServerUrl, RegistryService registryService, int invokerHashCode){
        LOGGER.info("服务消费者直连服务提供者...");
        //只配置了一个服务提供者地址
        if
        (!directServerUrl.contains(RpcConstants.RPC_MULTI_DIRECT_SERVERS_SEPARATOR)){
            return getDirectServiceMetaWithCheck(directServerUrl);
        }
        // 配置了多个服务提供者地址
        return registryService.select(this.getMultiServiceMeta(directServerUrl),
                invokerHashCode, localIp);
    }

}
