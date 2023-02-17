package io.lh.rpc.testconsumer.codec;

import io.lh.rpc.testconsumer.codec.init.RpcTestConInitialize;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RpcConsumerTest {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcTestConInitialize());
            bootstrap.connect("127.0.0.1", 27780).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            Thread.sleep(1230);
            eventLoopGroup.shutdownGracefully();
        }
    }
}
