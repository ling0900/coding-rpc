package io.lh.rpc.codec;


import io.lh.rpc.serialization.api.Serialization;
import io.lh.rpc.serialization.jdk.JdkSerialization;

/**
 * @date 2023年2月16日
 * @author lh
 * The interface Rpc codec.
 */
public interface RpcCodec {
    /**
     * Gets jdk serialization.
     *
     * @return the jdk serialization
     */
    default Serialization getJdkSerialization() {
        return new JdkSerialization();
    }
}
