package io.lh.rpc.test.spi.service.impl;

import io.lh.rpc.spi.annotation.SPIClass;
import io.lh.rpc.test.spi.service.SPIService;

@SPIClass
public class SPIServiceImpl implements SPIService {
    @Override
    public String hello(String saySomething) {
        return "SPI helo:" + saySomething;
    }
}
