package io.lh.rpc.testconsumer.codec.handler;

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
 */
public class RpcTestConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    private final Logger logger = LoggerFactory.getLogger(RpcTestConsumerHandler.class);

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
        request.setClassName("io.lh.rpc.test.DemoService");
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setParameters(new Object[]{"lh"});
        request.setOneWay(false);

        protocol.setBody(request);
        request.setAsync(false);

        logger.info("消费者发送的数据》》》》》》》》》》》》{}", JSONObject.toJSONString(protocol));

        ctx.writeAndFlush(protocol);

        logger.info("发送数据完毕++++++");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol) throws Exception {
        logger.info("消费者接收到++++++++++++{}", JSONObject.toJSONString(protocol));
    }
}