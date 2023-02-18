package io.lh.rpc.test.consumer.codec.init;

import io.lh.rpc.codec.RpcDecoder;
import io.lh.rpc.codec.RpcEncoder;
import io.lh.rpc.test.consumer.codec.handler.RpcConsumerHandlerTest;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * The type Rpc test con initialize.
 */
@SuppressWarnings("ALL")
public class RpcTestConInitialize extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(new RpcEncoder());
        channelPipeline.addLast(new RpcDecoder());
        channelPipeline.addLast(new RpcConsumerHandlerTest());
    }
}
