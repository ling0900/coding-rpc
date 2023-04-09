package io.lh.rpc.serialization.hessian2;

import io.lh.rpc.commom.exception.SerializerException;
import io.lh.rpc.serialization.api.Serialization;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPIClass
public class Hessian2Serialization implements Serialization {

    private final Logger logger = LoggerFactory.getLogger(Hessian2Serialization.class);

    @Override
    public <T> byte[] serialize(T obj) {
        logger.warn("执行 hessian2 序列化");

        if (obj == null) {
            throw new SerializerException("序列化出问题");
        }


        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        logger.warn("执行 hessian2 反序列化");

        return null;
    }
}
