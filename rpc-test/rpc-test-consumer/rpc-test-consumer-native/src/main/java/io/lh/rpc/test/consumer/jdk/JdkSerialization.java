package io.lh.rpc.test.consumer.jdk;

import io.lh.rpc.commom.exception.SerializerException;
import io.lh.rpc.serialization.api.Serialization;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * The type Jdk serialization.
 * ! 为何没有关闭流？？todo
 * @author lh
 */
@SPIClass
public class JdkSerialization implements Serialization {

    private static final Logger logger = LoggerFactory.getLogger(JdkSerialization.class);


    @Override
    public <T> byte[] serialize(T obj) {

        logger.info("执行jdk的序列化方式");

        if (obj == null) {
            throw new SerializerException("序列化对象 is null");
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(baos);
            objOut.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new SerializerException(e.getMessage(), e);
        }

    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {

        logger.info("执行jdk的反序列化方式");

        if (data == null) {
            throw new SerializerException("反序列化内容 is null");
        }

        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(is);
            return (T) in.readObject();
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }
}
