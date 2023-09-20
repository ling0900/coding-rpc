package io.lhrpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.enumeration.RpcStatus;
import io.lh.rpc.protocol.enumeration.RpcType;
import io.lh.rpc.protocol.header.RpcHeader;
import io.lh.rpc.protocol.header.RpcHeaderFactory;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import io.lh.rpc.proxy.api.future.RpcFuture;
import io.lhrpc.consumer.common.context.RpcContext;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
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

    /**
     * The Logger.
     */
    private final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerHandler.class);

    /**
     * key：请求ID， value：RpcResponse协议
     */
    private Map<Long, RpcFuture> pendingRpc = new ConcurrentHashMap<>();

    /**
     * The Channel.
     */
    private volatile Channel channel;

    /**
     * The Remote peer.
     */
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

    /**
     * Channel active.
     *
     * @param ctx the ctx
     * @throws Exception the exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            RpcHeader header = RpcHeaderFactory.getRequestHeader(RpcConstants.SERIALIZATION_PROTOSTUFF, RpcType.HEARTBEAT_FROM_CONSUMER.getType());
            RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
            RpcRequest request = new RpcRequest();
            request.setParameters(new Object[] {RpcConstants.HEARTBEAT_PING});
            requestRpcProtocol.setHeader(header);
            requestRpcProtocol.setBody(request);
            ctx.writeAndFlush(requestRpcProtocol);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * Channel registered.
     *
     * @param ctx the ctx
     * @throws Exception the exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    /**
     * Channel read 0.
     *
     * @param ctx      the ctx
     * @param protocol the protocol
     * @throws Exception the exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> protocol) throws Exception {

        LOGGER.info("服务消费者收到的数据{}", JSONObject.toJSONString(protocol));

        if (protocol == null) {
            return;
        }

        this.handlerMessage(protocol, ctx.channel());
    }

    /**
     * Handler message.
     *
     * @param protocol the protocol
     * @param channel  the channel
     */
    private void handlerMessage(RpcProtocol<RpcResponse> protocol,
                                Channel channel) {
        RpcHeader header = protocol.getHeader();
        // 消费者心跳
        if (header.getMsgType() == (byte) RpcType.HEARTBEAT_TO_CONSUMER.getType()) {
            // 处理心跳
            LOGGER.warn("consumer ❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤心跳❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
            this.handlerHeartBeatMessage(protocol, channel);
        } else if (header.getMsgType() == (byte) RpcType.HEARTBEAT_FROM_PROVIDER.getType()){
            this.handlerHeartbeatMessageFromProvider(protocol, channel);
        } else {
            // 处理相应消息
            this.handlerResponseMessage(protocol, header);
        }
    }

    /**
     * Handler heart beat message.
     *
     * @param protocol the protocol
     * @param channel  the channel
     */
    private void handlerHeartBeatMessage(RpcProtocol<RpcResponse> protocol
    , Channel channel) {
        // 无任何处理，打印日志为主
        LOGGER.warn("消费者收到{}服务者的心跳了{}", channel.remoteAddress(),
                protocol.getBody().getResult());
    }

    /**
     * Handler response message.
     *
     * @param protocol the protocol
     * @param header   the header
     */
    private void handlerResponseMessage(RpcProtocol<RpcResponse> protocol, RpcHeader header) {
        long requestId = header.getRequestId();
        RpcFuture rpcFuture = pendingRpc.remove(requestId);
        if (rpcFuture != null) {
            rpcFuture.done(protocol);
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

        if (async) {
            LOGGER.info("消费者异步发送消息");
            return this.sendRequestAsync(protocolMsg);
        } else if (oneway) {
            LOGGER.info("消费者单向发送消息");
            return this.sendRequestOneway(protocolMsg);
        } else {
            LOGGER.info("消费者同步发送消息");
            return this.sendRequestSync(protocolMsg);
        }
    }

    /**
     * Close.
     */
    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Gets rpc future.
     *
     * @param protocol the protocol
     * @return the rpc future
     */
    private RpcFuture getRpcFuture(RpcProtocol<RpcRequest> protocol) {
        RpcFuture rpcFuture = new RpcFuture(protocol);
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        pendingRpc.put(requestId, rpcFuture);
        return rpcFuture;
    }

    /**
     * 同步调用的方法
     *
     * @param protocol the protocol
     * @return the rpc future
     */
    private RpcFuture sendRequestSync(RpcProtocol<RpcRequest> protocol) {
        RpcFuture rpcFuture = this.getRpcFuture(protocol);
        channel.writeAndFlush(protocol);
        return rpcFuture;
    }

    /**
     * 异步调用的方法
     *
     * @param protocol the protocol
     * @return the rpc future
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
     *
     * @param protocol the protocol
     * @return the rpc future
     */
    private RpcFuture sendRequestOneway(RpcProtocol<RpcRequest> protocol) {
        channel.writeAndFlush(protocol);
        return null;
    }

    /**
     * Handler heartbeat message from provider.
     *
     * @param protocol the protocol
     * @param channel  the channel
     */
    private void
    handlerHeartbeatMessageFromProvider(RpcProtocol<RpcResponse> protocol, Channel channel) {
        RpcHeader header = protocol.getHeader();
        header.setMsgType((byte)
                RpcType.HEARTBEAT_TO_PROVIDER.getType());
        RpcProtocol<RpcRequest> requestRpcProtocol = new
                RpcProtocol<RpcRequest>();
        RpcRequest request = new RpcRequest();
        request.setParameters(new Object[]{RpcConstants.HEARTBEAT_PONG});
        header.setStatus((byte) RpcStatus.SUCCESS.getCode());
        requestRpcProtocol.setHeader(header);
        requestRpcProtocol.setBody(request);
        channel.writeAndFlush(requestRpcProtocol);
    }

}
