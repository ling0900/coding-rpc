package io.lhrpc.consumer.common.initializer;

import io.lh.rpc.codec.RpcDecoder;
import io.lh.rpc.codec.RpcEncoder;
import io.lhrpc.consumer.common.handler.RpcConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * The type Rpc consumer initializer.
 * 初始化管道的类
 *
 * @author lh
 */
public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        // 链式
        channelPipeline.addLast(new RpcEncoder())
                .addLast(new RpcDecoder())
                .addLast(new RpcConsumerHandler());
    }
}
