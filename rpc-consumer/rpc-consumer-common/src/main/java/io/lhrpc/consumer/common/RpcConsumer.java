package io.lhrpc.consumer.common;

import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lhrpc.consumer.common.future.RpcFuture;
import io.lhrpc.consumer.common.handler.RpcConsumerHandler;
import io.lhrpc.consumer.common.initializer.RpcConsumerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Rpc consumer.
 * 非常核心的消费者类
 * 对使用者屏蔽了netty的链接的代码细节。
 *
 * @author lh
 */
public class RpcConsumer {
    private final Logger LOGGER = LoggerFactory.getLogger(RpcConsumer.class);

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    private static volatile RpcConsumer consumerInstance;

    private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();

    private RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcConsumerInitializer());
    }

    /**
     * Gets consumer instance.
     * 单例模式
     *
     * @return the consumer instance
     */
    public static RpcConsumer getConsumerInstance() {
        if (consumerInstance == null) {
            synchronized (RpcConsumer.class) {
                if (consumerInstance == null) {
                    consumerInstance = new RpcConsumer();
                }
            }
        }
        return consumerInstance;
    }

    /**
     * Close.优雅关闭
     */
    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

    /**
     * Send request msg.
     *
     * @param requestRpcProtocol the request rpc protocol
     */
    public RpcFuture sendRequestMsg(RpcProtocol<RpcRequest> requestRpcProtocol) throws InterruptedException {

        // 地址
        String serviceAddress = "127.0.0.1";
        int port = 27780;

        String remoteServiceKey = serviceAddress.concat("-").concat(String.valueOf(port));
        RpcConsumerHandler rpcConsumerHandler = handlerMap.get(remoteServiceKey);

        // 判断是否存在，map
        if (rpcConsumerHandler == null) {
            rpcConsumerHandler = getRpcConsumerHandler(serviceAddress, port);
            handlerMap.put(remoteServiceKey, rpcConsumerHandler);
        } else if (! rpcConsumerHandler.getChannel().isActive()) {
            rpcConsumerHandler.close();
            // 从新获取
            rpcConsumerHandler = getRpcConsumerHandler(serviceAddress, port);
            handlerMap.put(remoteServiceKey, rpcConsumerHandler);
        }

        return rpcConsumerHandler.sendRequestMessage(requestRpcProtocol);
    }

    private RpcConsumerHandler getRpcConsumerHandler(String serviceAddress, int port) throws InterruptedException{
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
}