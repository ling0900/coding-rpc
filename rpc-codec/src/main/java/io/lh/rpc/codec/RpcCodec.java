package io.lh.rpc.codec;


import io.lh.rpc.serialization.api.Serialization;
import io.lh.rpc.spi.loader.ExtensionLoader;

/**
 * The interface Rpc codec.
 *
 * @author lh The interface Rpc codec.
 * @date 2023年2月16日
 */
public interface RpcCodec {
    /**
     * Gets serialization.
     * 利用spi机制，根据serializationType获取序列化 句柄。
     * @param serializationType the serialization type
     * @return the serialization实例
     */
    default Serialization getSerialization(String serializationType) {
        return ExtensionLoader.getExtension(Serialization.class, serializationType);
    }
}
