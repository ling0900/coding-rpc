package io.lh.rpc.demo.docker.consumer.service.impl;

import io.lh.rpc.annotation.RpcServiceConsumer;
import io.lh.rpc.demo.api.DemoService;
import io.lh.rpc.demo.docker.consumer.service.IDockerConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DockerConsumerImpl implements IDockerConsumer {

    @RpcServiceConsumer(
            registryType = "zookeeper",
            registryAddress = "8.130.123.59:2181",
            loadBalanceType = "zkconsistenthash", version = "1.0.0",
            group = "lh", serializaitonType = "protostuff",
            proxy = "cglib", timeout = 30000,
            async = false, oneway = false
    )
    @Autowired
    private DemoService demoService;


    @Override
    public String helloDocker(String s) {
        String s1 = demoService.helloDemo(s);
        return "docker 消费者！" + s1;
    }
}
