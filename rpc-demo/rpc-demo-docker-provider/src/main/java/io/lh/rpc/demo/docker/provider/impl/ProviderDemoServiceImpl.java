package io.lh.rpc.demo.docker.provider.impl;

import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.demo.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RpcServiceProvider(
        interfaceClass = DemoService.class,
        interfaceClassName = "io.lh.rpc.demo.api.DemoService",
        version = "1.0.0", group = "lh", weight = 2)
public class ProviderDemoServiceImpl implements DemoService {
    private final Logger logger = LoggerFactory.getLogger(ProviderDemoServiceImpl.class);
    @Override
    public String helloDemo(String name) {
        logger.info("基于docker 生产者内部实现类", name);

        return "基于docker 生产者内部实现类" + name;
    }
}
