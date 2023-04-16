package io.lh.rpc.serialization.hessian2;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import io.lh.rpc.commom.exception.SerializerException;
import io.lh.rpc.serialization.api.Serialization;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SPIClass
public class Hessian2Serialization implements Serialization {

    private final Logger logger = LoggerFactory.getLogger(Hessian2Serialization.class);

    @Override
    public <T> byte[] serialize(T obj) {
        logger.warn("执行 hessian2 序列化");

        if (obj == null) {
            throw new SerializerException("序列化出问题");
        }

        byte[] result = new byte[0];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);

        try {
            hessian2Output.startMessage();
            hessian2Output.writeObject(obj);
            hessian2Output.flush();
            hessian2Output.completeMessage();
            result = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("序列化有问题{}", e);
            throw new RuntimeException(e);
        } finally {
            if (hessian2Output != null) {
                try {
                    hessian2Output.close();
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        logger.warn("执行 hessian2 反序列化");

        if (data == null) {
            throw new SerializerException("data is null");
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
        T object = null;

        try {
            hessian2Input.startMessage();
            object = (T) hessian2Input.readObject();
            hessian2Input.completeMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != hessian2Input) {
                try {
                    hessian2Input.close();
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return object;
    }
}
