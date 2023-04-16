package io.lh.rpc.serialization.kryo;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import io.lh.rpc.commom.exception.SerializerException;
import io.lh.rpc.serialization.api.Serialization;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@SPIClass
public class KryoSerialization implements Serialization {
    private final Logger logger = LoggerFactory.getLogger(KryoSerialization.class);
    @Override
    public <T> byte[] serialize(T obj) {
        logger.warn("execute kryo serialize...");
        if (obj == null){
            throw new SerializerException("serialize object is null");
        }
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(obj.getClass(), new JavaSerializer());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeClassAndObject(output, obj);
        output.flush();
        output.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new SerializerException(e.getMessage(), e);
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        logger.warn("execute kryo deserialize...");
        if (data == null){
            throw new SerializerException("deserialize data is null");
        }
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(cls, new JavaSerializer());
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Input input = new Input(bais);
        return (T) kryo.readClassAndObject(input);
    }
}

