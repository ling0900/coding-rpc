package io.lh.rpc.test.consumer.codec.handler;

import com.alibaba.fastjson.JSONObject;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.header.RpcHeaderFactory;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Rpc test consumer handler.
 *
 * @author lh
 */
public class RpcConsumerHandlerTest extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    private final Logger logger = LoggerFactory.getLogger(RpcConsumerHandlerTest.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        logger.info("发送数据开始=====");

        //直接在cmd窗口发送的话，很被动，所以这里来模拟以下发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
        // 设置header为jdk
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));

        RpcRequest request = new RpcRequest();
        request.setGroup("lh");
        request.setMethodName("hello");
        request.setClassName("io.lh.rpc.test.api.DemoService");
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setParameters(new Object[]{"lh"});
        // 这里很关键
        request.setMethodName("hello");
        request.setOneWay(false);

        protocol.setBody(request);
        request.setAsync(false);

        logger.info("消费者发送的数据{}", JSONObject.toJSONString(protocol));

        ctx.writeAndFlush(protocol);

        logger.info("发送数据完毕");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol) throws Exception {
        logger.info("消费者接收到的消息数据{}", JSONObject.toJSONString(protocol));
    }
}