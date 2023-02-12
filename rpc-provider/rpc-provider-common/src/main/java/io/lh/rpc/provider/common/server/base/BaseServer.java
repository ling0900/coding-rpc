package io.lh.rpc.provider.common.server.base;

import io.lh.rpc.provider.common.handler.RpcServiceProviderHandler;
import io.lh.rpc.provider.common.server.api.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * 版本：1.0.0
 * 作者：lh
 * 创建时间：2023/02/12
 */
public class BaseServer implements Server {

    /**
     *
     */
    protected String host = "127.0.0.1";

    protected String port = "27110";

    protected Map<String, Object> handlerMap = new HashMap<>();

    public BaseServer(String serviceAddress) {
        if (! StringUtils.isEmpty(serviceAddress)) {
            String[] ipAndPort = serviceAddress.split(":");
            this.host = ipAndPort[0];
            this.port = ipAndPort[1];
        }
    }

    @Override
    public void startNettyServer() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {

        bootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new RpcServiceProviderHandler(handlerMap));

                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(host, Integer.parseInt(port)).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
