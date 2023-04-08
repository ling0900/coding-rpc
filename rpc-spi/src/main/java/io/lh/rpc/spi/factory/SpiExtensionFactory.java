package io.lh.rpc.spi.factory;

import io.lh.rpc.spi.annotation.SPI;
import io.lh.rpc.spi.annotation.SPIClass;
import io.lh.rpc.spi.loader.ExtensionLoader;

import java.util.Optional;

/**
 * The type Spi extension factory.
 */
@SPIClass
public class SpiExtensionFactory implements IExtensionFactory{
    @Override
    public <T> T getExtension(String key, Class<T> clazz) {
        return Optional.ofNullable(clazz)
                .filter(Class::isInterface)
                .filter(cla -> cla.isAnnotationPresent(SPI.class))
                .map(ExtensionLoader::getExtensionLoader)
                .map(ExtensionLoader::getDefaultSpiClassInstance)
                .orElse(null);
    }
}


