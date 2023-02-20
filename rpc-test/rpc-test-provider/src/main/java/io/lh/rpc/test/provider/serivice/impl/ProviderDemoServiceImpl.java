package io.lh.rpc.test.provider.serivice.impl;

import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Provider demo service.
 *
 * @author lh
 */
@RpcServiceProvider(interfaceClass = DemoService.class,
                    interfaceClassName = "io.lh.rpc.test.api.DemoService",
                    version = "1.0.0",
                    group = "lh")
public class ProviderDemoServiceImpl implements DemoService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProviderDemoServiceImpl.class);

    @Override
    public String hello(String serviceName) {
        LOGGER.info("调用hello方法, {}", serviceName);
        return "进入hello" + serviceName;
    }
}
