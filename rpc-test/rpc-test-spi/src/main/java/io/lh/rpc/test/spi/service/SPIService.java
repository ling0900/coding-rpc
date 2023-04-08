package io.lh.rpc.test.spi.service;

import io.lh.rpc.spi.annotation.SPI;

/**
 * The interface Spi service.
 * spi服务的接口，后续只要实现它就行了
 */
@SPI("spiService")
public interface SPIService {
    /**
     * Hello string.
     *
     * @param saySomething the say something
     * @return the string
     */
    String hello(String saySomething);
}
