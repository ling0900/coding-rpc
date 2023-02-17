package io.lh.rpc.protocol.header;

import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.protocol.enumeration.RpcType;

/**
 * The type Rpc header factory.
 * @author lh
 */
public class RpcHeaderFactory {
    /**
     * Gets request header.
     *
     * @param serializationType the serialization type
     * @return the request header
     */
    public static RpcHeader getRequestHeader(String serializationType) {
        RpcHeader rpcHeader = new RpcHeader();

        //TODO
        long requestId = 0L;
        rpcHeader.setRequestId(requestId);
        // 这里出了一个bug，之前没有写，因为依赖问题偷懒了。。
        rpcHeader.setMagicNum(RpcConstants.MAGIC);
        //todo 思考地方
        rpcHeader.setMsgType((byte) RpcType.REQUEST.getType());
        rpcHeader.setStatus((byte) 0x1);
        rpcHeader.setSerializationType(serializationType);
        return rpcHeader;
    }
}
