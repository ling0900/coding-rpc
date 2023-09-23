package io.lh.rpc.demo.spring.annotation.provide.impl;

import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.demo.api.DemoService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RpcServiceProvider(interfaceClass = DemoService.class,
        interfaceClassName = "io.lh.rpc.demo.api.DemoService",
        version = "1.0.0",
        group = "lh",
        weight = 2
)
public class ProviderDemoServiceImpl implements DemoService {

    @Override
    public String helloDemo(String name) {
        log.info("hello--------------------", name);
        return "hello-------------注解方式----- " + name;
    }
}

