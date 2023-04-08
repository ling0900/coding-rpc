package io.lh.rpc.spi.factry;

import io.lh.rpc.spi.annotation.SPI;

/**
 * The interface Extension factory.
 */
@SPI("spi")
public interface IExtensionFactory {
    /**
     * Gets extension.
     * 获取拓展类对象的接口
     *
     * @param <T>   the type parameter
     * @param key   the key
     * @param clazz the clazz
     * @return the extension clas
     */
    <T> T getExtension(String key, Class<T> clazz);
}
