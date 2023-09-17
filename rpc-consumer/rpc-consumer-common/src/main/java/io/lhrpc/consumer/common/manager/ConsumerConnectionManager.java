package io.lhrpc.consumer.common.manager;

import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.enumeration.RpcType;
import io.lh.rpc.protocol.header.RpcHeader;
import io.lh.rpc.protocol.header.RpcHeaderFactory;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lhrpc.consumer.common.cache.ConsumerChannelCache;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * The type Consumer connection manager.
 * @author lh
 */
public class ConsumerConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerConnectionManager.class);

    /**
     * Scan 不活跃的 channel.
     */
    public static void scanNotActityChannel() {
        Set<Channel> channels = ConsumerChannelCache.getChannelCache();
        // 先校验一下是否非空
        if (channels == null || channels.isEmpty()) {
            return;
        }

        channels.stream().forEach(channel -> {
            // 如果不活跃，移除掉这个channel
            if (! channel.isOpen() || ! channel.isActive()) {
                channel.close();
                ConsumerChannelCache.remove(channel);
            }
        });
    }

    /**
     * Broadcast ping message from consumer.
     * 发送ping消息，消费者
     */
    public static void broadcastPingMessageFromConsumer(){

        LOGGER.info("💗💗发送心跳广播信息💗💗");

        Set<Channel> channelCache = ConsumerChannelCache.getChannelCache();

        if (channelCache == null || channelCache.isEmpty()) return;
        // header信息
        RpcHeader header = RpcHeaderFactory.getRequestHeader(RpcConstants.SERIALIZATION_PROTOSTUFF,
                RpcType.HEARTBEAT_FROM_CONSUMER.getType());
        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<RpcRequest>();
        // request信息
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setParameters(new Object[]{RpcConstants.HEARTBEAT_PING});
        // protocol协议
        requestRpcProtocol.setHeader(header);
        requestRpcProtocol.setBody(rpcRequest);

        channelCache.stream().forEach((channel) -> {
            if (channel.isOpen() && channel.isActive()){
                LOGGER.info("发送心跳到=>service provider, " +
                        "provider 是: {}, 心跳信息是: {}", channel.remoteAddress(), RpcConstants.HEARTBEAT_PING);
                channel.writeAndFlush(requestRpcProtocol);
            }
        });
    }


}
