package io.lh.rpc.serialization.api;

import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.spi.annotation.SPI;

/**
 * The interface Serialization.
 * 通过SPI注解，实现默认的序列化方式为  JDK
 * @author lh
 */
@SPI(RpcConstants.SERIALIZATION_JDK)
public interface Serialization {

    /**
     * Serialize byte [ ]. 序列化
     *
     * @param <T> the type parameter
     * @param obj the obj
     * @return the byte [ ]
     */
    <T> byte[] serialize(T obj);

    /**
     * Deserialize t. 反序列化
     *
     * @param <T>   the type parameter
     * @param data  the data
     * @param clazz the clazz
     * @return the t
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
