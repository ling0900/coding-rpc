package io.lh.rpc.codec;

import io.lh.rpc.commom.utils.SerializationUtil;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.header.RpcHeader;
import io.lh.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * The type Rpc encoder.
 * @author lh
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> implements RpcCodec{
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg,
                          ByteBuf byteBuf) throws Exception {
        RpcHeader header = msg.getHeader();
        byteBuf.writeShort(header.getMagicNum());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());
        String serializationType = header.getSerializationType();

        // todo
        Serialization serialization = getSerialization(serializationType);
        byteBuf
                .writeBytes(SerializationUtil.paddingString(serializationType)
                .getBytes("UTF-8"));
        byte[] data = serialization.serialize(msg.getBody());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);

    }
}
