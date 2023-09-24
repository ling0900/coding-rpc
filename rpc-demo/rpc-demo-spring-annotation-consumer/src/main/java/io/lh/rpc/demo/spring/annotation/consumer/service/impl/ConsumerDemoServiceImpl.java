package io.lh.rpc.demo.spring.annotation.consumer.service.impl;

import io.lh.rpc.annotation.RpcServiceConsumer;
import io.lh.rpc.demo.api.DemoService;
import io.lh.rpc.demo.spring.annotation.consumer.service.ConsumerDemoService;
import org.springframework.stereotype.Service;


@Service
public class ConsumerDemoServiceImpl implements ConsumerDemoService {

    @RpcServiceConsumer(
            registryAddress = "8.130.123.59:2181",
            loadBalanceType = "random", group = "lh",
            proxy = "cglib", timeout = 30000
    )
    private DemoService demoService;

    @Override
    public String consumerHello(String s) {

//        DemoService demoService = rpcClient.create(DemoService.class);

        return demoService.helloDemo(s + "hello990");
    }
}
