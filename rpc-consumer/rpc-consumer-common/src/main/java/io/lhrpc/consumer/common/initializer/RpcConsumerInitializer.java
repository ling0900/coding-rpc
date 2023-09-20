package io.lhrpc.consumer.common.initializer;

import io.lh.rpc.codec.RpcDecoder;
import io.lh.rpc.codec.RpcEncoder;
import io.lh.rpc.constants.RpcConstants;
import io.lhrpc.consumer.common.handler.RpcConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * The type Rpc consumer initializer.
 * 初始化管道的类
 *
 * @author lh
 */
public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {

    private int heartbeatInterval;

    public RpcConsumerInitializer(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        // 链式
        channelPipeline.addLast(RpcConstants.CODEC_ENCODER, new RpcEncoder())
                .addLast(RpcConstants.CODEC_DECODER, new RpcDecoder())
                .addLast(RpcConstants.CODEC_CLIENT_IDLE_HANDLER, new IdleStateHandler(heartbeatInterval, 0, 0, TimeUnit.MICROSECONDS))
                .addLast(RpcConstants.CODEC_HANDLER, new RpcConsumerHandler());
    }
}
