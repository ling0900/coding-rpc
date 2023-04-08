package io.lh.rpc.test.spi.service;

import io.lh.rpc.spi.annotation.SPI;

@SPI("spiService")
public interface SPIService {
    String hello(String saySomething);
}
