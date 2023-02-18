package io.lhrpc.consumer.common.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.net.SocketAddress;

/**
 * The type Rpc consumer handler.
 *
 * @author lh
 */
public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerHandler.class);

    private volatile Channel channel;

    private SocketAddress remotePeer;

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Sets channel.
     *
     * @param channel the channel
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * Gets remote peer.
     *
     * @return the remote peer
     */
    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    /**
     * Sets remote peer.
     *
     * @param remotePeer the remote peer
     */
    public void setRemotePeer(SocketAddress remotePeer) {
        this.remotePeer = remotePeer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) throws Exception {
        LOGGER.info("服务消费者收到的数据{}", JSONObject.toJSONString(msg));
    }

    /**
     * Send request message.
     *
     * @param protocolMsg the protocol msg
     */
    public void sendRequestMessage(RpcProtocol<RpcRequest> protocolMsg) {
        LOGGER.info("消费者发送的数据>>>>>>>>>>>>{}", JSONObject.toJSONString(protocolMsg));
        channel.writeAndFlush(protocolMsg);
    }

    /**
     * Close.
     */
    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }
}
