package io.lh.rpc.protocol.header;

import io.lh.rpc.protocol.enumeration.RpcType;

public class RpcHeaderFactory {
    public static RpcHeader getRequestHeader(String serializationType) {
        RpcHeader rpcHeader = new RpcHeader();

        long requestId = 0l;//TODO
        rpcHeader.setRequestId(requestId);
//        rpcHeader.setMagicNum();
        rpcHeader.setMsgType((byte) RpcType.REQUEST.getType());//todo 思考地方
        rpcHeader.setStatus((byte) 0x1);
        rpcHeader.setSerializationType(serializationType);
        return rpcHeader;
    }
}
