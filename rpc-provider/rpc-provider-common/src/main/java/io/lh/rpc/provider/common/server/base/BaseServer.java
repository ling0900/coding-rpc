package io.lh.rpc.provider.common.server.base;

import io.lh.rpc.codec.RpcDecoder;
import io.lh.rpc.codec.RpcEncoder;
import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.provider.common.handler.RpcServiceProviderHandler;
import io.lh.rpc.provider.common.manager.ProviderConnectionManager;
import io.lh.rpc.provider.common.server.api.Server;
import io.lh.rpc.registry.api.RegistryService;
import io.lh.rpc.registry.api.config.RegistryConfig;
import io.lh.rpc.spi.loader.ExtensionLoader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 * 版本：1.0.0
 * 创建时间：2023/02/12
 *
 * @author lh
 */
public class BaseServer implements Server {

    /**
     * The Logger.
     */
    private final Logger LOGGER = LoggerFactory.getLogger(BaseServer.class);

    /**
     * The Registry service.
     */
    protected RegistryService registryService;

    /**
     * The Reflect type.
     */
    private String reflectType;

    /**
     * The Host.
     */
    protected String host = "127.0.0.1";

    /**
     * The Port.
     */
    protected int port = 8888;

    /**
     * The Handler map.
     */
    protected Map<String, Object> handlerMap = new HashMap<>();

    /**
     * The Executor service.
     */
    private ScheduledExecutorService executorService;

    /**
     * The Heartbeat interval.
     */
    private int heartbeatInterval = 30000;

    /**
     * The Scan not active channel interval.
     */
    private int scanNotActiveChannelInterval = 60000;

    /**
     * Instantiates a new Base server.
     * 这个继承后，会被重写的。
     * @param serverAddress           the server address
     * @param registryAddress         the registry address
     * @param registryType            the registry type
     * @param reflectType             the reflect type
     * @param registryLoadBalanceType the registry load balance type
     */
    public BaseServer(String serverAddress, String registryAddress, String registryType, String reflectType,
                      String registryLoadBalanceType, int heartbeatInterval, int scanNotActiveChannelInterval) {

        // 先开始心跳
        if (heartbeatInterval > 0) this.heartbeatInterval = heartbeatInterval;
        if (scanNotActiveChannelInterval > 0) this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        this.startHeartbeat();

        if (! StringUtils.isEmpty(serverAddress)) {
            String[] serverArray = serverAddress.split(":");
            this.port = Integer.parseInt(serverArray[1]);
            this.host = serverArray[0];
        }
        this.reflectType = reflectType;

        this.registryService = this.getRegistryService(registryAddress, registryType, registryLoadBalanceType);
    }


    /**
     * Start netty server.
     */
    @Override
    public void startNettyServer() {

        // 创建对应的EventLoop线程池备用, 分bossGroup和workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(3);
        try {
            // 创建netty对应的入口核心类 ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置server的各项参数，以及应用处理器
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // tcp协议请求等待队列
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 将各channelHandler绑定到netty的上下文中
                        ch.pipeline()
                                .addLast(RpcConstants.CODEC_DECODER, new RpcDecoder())
                                .addLast(RpcConstants.CODEC_ENCODER, new RpcEncoder())
                                // 核心的地方
                                .addLast(RpcConstants.CODEC_SERVER_IDLE_HANDLER,
                                        // 多看这里的源码
                                        new IdleStateHandler(0, 0, heartbeatInterval, TimeUnit.MILLISECONDS))
                                .addLast(RpcConstants.CODEC_HANDLER, new
                                        RpcServiceProviderHandler(handlerMap, reflectType));
                    }
                }).childOption(ChannelOption.SO_KEEPALIVE, true);

            // sync() 保证执行完成所有任务
            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();

            LOGGER.info("生产者启动ok{}：{}", host, port);

            // 等待关闭信号，让业务线程去服务业务了
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            LOGGER.info("baseServer：{}", e);
        } finally {
            // 收到关闭信号后，优雅关闭server的线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
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
        RegistryService registryService = null;
        // SPI 机制去获取到注册中心
        registryService = ExtensionLoader.getExtension(RegistryService.class, registryType);
        try {
            // 初始化注册
            registryService.init(new RegistryConfig(registryAddress, registryType, registryLoadBalanceType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return registryService;
    }

    /**
     * Start heartbeat.
     */
    private void startHeartbeat() {
        executorService = Executors.newScheduledThreadPool(2);
        //扫描并处理所有不活跃的连接
        executorService.scheduleAtFixedRate(() -> {
            LOGGER.info("*************************scanNotActiveChannel*************************");
            ProviderConnectionManager.scanNotActiveChannel();
        }, 10, scanNotActiveChannelInterval, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(()->{
            LOGGER.info("*************************broadcastPingMessageFromConsumer*************************");
                    ProviderConnectionManager.broadcastPingMessageFromProvider();
        }, 3, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

}
