package io.lh.rpc.demo.spring.boot.provider.impl;

import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.demo.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RpcServiceProvider(interfaceClass = DemoService.class, interfaceClassName = "io.lh.rpc.demo.api.DemoService",
        version = "1.0.0", group = "lh", weight = 2)
public class ProviderDemoServiceImpl implements DemoService {

    Logger log = LoggerFactory.getLogger(ProviderDemoServiceImpl.class);
    @Override
    public String helloDemo(String name) {

        log.info("stater方式{}", name);
        return name;
    }
}


