package io.lh.rpc.serialization.jdk;



import io.lh.rpc.commom.exception.SerializerException;
import io.lh.rpc.serialization.api.Serialization;

import java.io.*;
import java.lang.annotation.Annotation;

/**
 * The type Jdk serialization.
 * ! 为何没有关闭流？？todo
 */
public class JdkSerialization implements Serialization {


    @Override
    public <T> byte[] serialize(T obj) {

        if (obj == null) throw new SerializerException("序列化对象 is null");

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

        if (data == null) throw new SerializerException("反序列化内容 is null");

        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(is);
            return (T) in.readObject();
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }
}
