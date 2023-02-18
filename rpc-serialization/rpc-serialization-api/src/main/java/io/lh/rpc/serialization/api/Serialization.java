package io.lh.rpc.serialization.api;

/**
 * The interface Serialization.
 * @author lh
 */
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
