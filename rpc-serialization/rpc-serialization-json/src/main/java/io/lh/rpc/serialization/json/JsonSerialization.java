package io.lh.rpc.serialization.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.lh.rpc.commom.exception.SerializerException;
import io.lh.rpc.serialization.api.Serialization;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * The type Json serialization.
 */
@SPIClass
public class JsonSerialization implements Serialization {

    private final Logger logger = LoggerFactory.getLogger(JsonSerialization.class);

    private static final ObjectMapper oMapper = new ObjectMapper();

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        oMapper.setDateFormat(dateFormat);
        oMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        oMapper.enable(SerializationFeature.INDENT_OUTPUT);
        oMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        oMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
        oMapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
        oMapper.disable(SerializationFeature.CLOSE_CLOSEABLE);
        oMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        oMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        oMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
    }

    @Override
    public <T> byte[] serialize(T obj) {
        logger.warn("执行 json 序列化操作");
        if (obj == null) {
            logger.error("需要序列化的为null");
            throw new SerializerException("需要序列化的为null");
        }

        byte[] bytes = new byte[0];
        try {
            bytes = oMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化操作出现一异常:{}", e);
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        logger.warn("执行 json 反序列化操作");
        if (data == null) {
            logger.error("需要反序列化的为null");
            throw new SerializerException("需要反序列化的为null");
        }

        T o = null;
        try {
            o = oMapper.readValue(data, clazz);
        } catch (IOException e) {
            logger.error("json反序列化操作出现一异常:{}", e);
            throw new RuntimeException(e);
        }
        return o;
    }
}
