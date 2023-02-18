package io.lhrpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.header.RpcHeader;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import io.lhrpc.consumer.common.context.RpcContext;
import io.lhrpc.consumer.common.future.RpcFuture;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Rpc consumer handler.
 *
 * @author lh
 */
public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerHandler.class);

    /**
     * key：请求ID， value：RpcResponse协议
     */
    private Map<Long, RpcFuture> pendingRpc = new ConcurrentHashMap<>();

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
        if (msg == null) {
            return;
        }
        RpcHeader header = msg.getHeader();
        long requestId = header.getRequestId();
        RpcFuture rpcFuture = pendingRpc.remove(requestId);
        if (rpcFuture != null) {
            rpcFuture.done(msg);
        }
    }

    /**
     * Send request message.
     *
     * @param protocolMsg the protocol msg
     * @param async       the async
     * @param oneway      the oneway
     * @return the rpc future
     */
    public RpcFuture sendRequestMessage(RpcProtocol<RpcRequest> protocolMsg, boolean async, boolean oneway) {

        LOGGER.info("消费者发送的数据>>>>>>>>>>>>{}", JSONObject.toJSONString(protocolMsg));

        return oneway ? this.sendRequestOneway(protocolMsg) : async ? sendRequestAsync(protocolMsg) : this.sendRequestSync(protocolMsg);
    }

    /**
     * Close.
     */
    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    private RpcFuture getRpcFuture(RpcProtocol<RpcRequest> protocol) {
        RpcFuture rpcFuture = new RpcFuture(protocol);
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        pendingRpc.put(requestId, rpcFuture);
        return rpcFuture;
    }

    /**
     * 同步调用的方法
     */
    private RpcFuture sendRequestSync(RpcProtocol<RpcRequest> protocol) {
        RpcFuture rpcFuture = this.getRpcFuture(protocol);
        channel.writeAndFlush(protocol);
        return rpcFuture;
    }

    /**
     * 异步调用的方法
     */
    private RpcFuture sendRequestAsync(RpcProtocol<RpcRequest> protocol) {
        RpcFuture rpcFuture = this.getRpcFuture(protocol);
        // rpcFuture放到上下文中，实现异步。调用者，后期通过这个上线文可以获得到返回的数据！核心
        RpcContext.getContext().setRpcFuture(rpcFuture);
        channel.writeAndFlush(protocol);
        // 异步调用的，所以返回null。
        return null;
    }

    /**
     * 单向调用的方法
     */
    private RpcFuture sendRequestOneway(RpcProtocol<RpcRequest> protocol) {
        channel.writeAndFlush(protocol);
        return null;
    }
}
