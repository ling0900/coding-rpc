package io.lh.rpc.codec;


import io.lh.rpc.serialization.api.Serialization;
import io.lh.rpc.serialization.jdk.JdkSerialization;

public interface RpcCodec {
    default Serialization getJdkSerialization() {
        return new JdkSerialization();
    }
}
