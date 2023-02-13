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

    protected String host = "127.0.0.1";

    protected int port = 8888;

    protected Map<String, Object> handlerMap = new HashMap<>();

    public BaseServer(String serviceAddress) {
        if (! StringUtils.isEmpty(serviceAddress)) {
            String[] ipAndPort = serviceAddress.split(":");
            this.host = ipAndPort[0];
            this.port = Integer.parseInt(ipAndPort[1]);
        }
    }

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
                    .option(ChannelOption.SO_BACKLOG, 128) // tcp协议请求等待队列
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 将各channelHandler绑定到netty的上下文中
                        ch.pipeline()
                                .addLast("encoder", new StringDecoder())
                                .addLast("decoder", new StringEncoder())
                                .addLast(new RpcServiceProviderHandler(handlerMap));
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            // sync() 保证执行完成所有任务
            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
            // 等待关闭信号，让业务线程去服务业务了
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            System.out.println("启动异常");
        } finally {
            // 收到关闭信号后，优雅关闭server的线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
