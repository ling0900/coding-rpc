package io.lh.rpc.serialization.fst;

import io.lh.rpc.commom.exception.SerializerException;
import io.lh.rpc.serialization.api.Serialization;
import io.lh.rpc.spi.annotation.SPIClass;
import org.nustaq.serialization.FSTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPIClass
public class FstSerialization implements Serialization {

    private final Logger logger = LoggerFactory.getLogger(FstSerialization.class);

    @Override
    public <T> byte[] serialize(T obj) {
        logger.warn("执行 fst 序列化");

        if (obj == null) {
            throw new SerializerException("序列化出问题");
        }
        return FSTConfiguration.getDefaultConfiguration().asByteArray(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        logger.warn("执行 fst 反序列化");

        if (data == null) {
            throw new SerializerException("data is null");
        }

        return (T) FSTConfiguration.getDefaultConfiguration().asObject(data);
    }
}
