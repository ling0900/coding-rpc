package io.lh.rpc.demo.spring.boot.consumer.service.impl;

import io.lh.rpc.annotation.RpcServiceConsumer;
import io.lh.rpc.demo.api.DemoService;
import io.lh.rpc.demo.spring.boot.consumer.service.DemoConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DemoConsumerImpl implements DemoConsumer {

    @RpcServiceConsumer(
            registryAddress = "8.130.123.59:2181",
            loadBalanceType = "random", group = "lh",
            proxy = "cglib", timeout = 30000
    )
    @Autowired
    private DemoService demoService;

    @Override
    public String consumer(String consumer) {
        return demoService.helloDemo("consumer demo ok~");
    }
}
