package io.lh.rpc.codec;

import io.lh.rpc.commom.utils.SerializationUtil;
import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.enumeration.RpcType;
import io.lh.rpc.protocol.header.RpcHeader;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import io.lh.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @Date 2023年2月16日
 * @author lh
 * The type Rpc decoder.
 */
public class RpcDecoder extends ByteToMessageDecoder implements RpcCodec {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf inByteBuf, List<Object> outList) {

        if (inByteBuf.readableBytes() < RpcConstants.HEADER_TOTAL_LEN) {
            return;
        }

        inByteBuf.markReaderIndex();

        short magicNum = inByteBuf.readShort();
        if (magicNum != RpcConstants.MAGIC) {
            throw new IllegalArgumentException("魔数非法" + magicNum);
        }

        // 注意这里的顺序
        byte msgType = inByteBuf.readByte();
        byte status = inByteBuf.readByte();
        long requestId = inByteBuf.readLong();

        ByteBuf serialTypeByteBuf = inByteBuf.readBytes(SerializationUtil.MAX_SERIALIZATION_TYPE_COUNR);
        String serialTypeByte = SerializationUtil.subString0(serialTypeByteBuf.toString(CharsetUtil.UTF_8));

        int dataLength = inByteBuf.readInt();
        if (inByteBuf.readableBytes() < dataLength) {
            inByteBuf.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLength];
        inByteBuf.readBytes(data);

        RpcType rpcType = RpcType.findByType(msgType);
        if (rpcType == null) {
            return;
        }

        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setMagicNum(magicNum);
        rpcHeader.setStatus(status);
        rpcHeader.setMsgType(msgType);
        rpcHeader.setRequestId(requestId);
        rpcHeader.setSerializationType(serialTypeByte);
        rpcHeader.setMsgLen(dataLength);

        //这个调用很关键，这是SPI拓展中的一个比较核心的方法。
        Serialization serialization = getSerialization(serialTypeByte);
        // switch经常配合枚举类使用，看起来美观。
        switch (rpcType) {
            default:
            case REQUEST:
                RpcRequest deserializeRequest = serialization.deserialize(data, RpcRequest.class);
                if (deserializeRequest != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setHeader(rpcHeader);
                    protocol.setBody(deserializeRequest);
                    outList.add(protocol);
                }
                break;
            case RESPONSE:
                RpcResponse rpcResponse = serialization.deserialize(data, RpcResponse.class);
                if (rpcResponse != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setHeader(rpcHeader);
                    protocol.setBody(rpcResponse);
                    outList.add(protocol);
                }
                break;
            case HEARTBEAT_FROM_CONSUMER:
            case HEARTBEAT_FROM_PROVIDER:
            case HEARTBEAT_TO_CONSUMER:
            case HEARTBEAT_TO_PROVIDER:
                break;
        }


    }
}
