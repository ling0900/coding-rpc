package io.lh.rpc.provider.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.enumeration.RpcType;
import io.lh.rpc.protocol.header.RpcHeader;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * 描述：服务提供者的消息处理工具类
 * 版本：1.0.0
 * 作者：lh
 * 创建时间：2023/02/12
 */
public class RpcServiceProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private final Map<String, Object> hadlerMap;

    public RpcServiceProviderHandler(Map<String, Object> hadlerMap) {
        this.hadlerMap = hadlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> protocol) throws Exception {


        System.out.println("=====收到的是" + JSONObject.toJSONString(protocol));

        for (Map.Entry<String, Object> entry : hadlerMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        RpcHeader header = protocol.getHeader();
        RpcRequest request = protocol.getBody();

        header.setMsgType((byte) RpcType.RESPONSE.getType());

        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
        RpcResponse response = new RpcResponse();

        response.setResult("交互ok");
        response.setAsync(request.isAsync());
        response.setOneWay(request.isOneWay());
        responseRpcProtocol.setHeader(header);
        responseRpcProtocol.setBody(response);

        channelHandlerContext.writeAndFlush(responseRpcProtocol);

    }
}
