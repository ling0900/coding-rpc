package io.lh.rpc.test.scanner.consumer;

import io.lh.rpc.annotation.RpcServiceConsumer;
import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.test.scanner.service.DemoService;

@SuppressWarnings("ALL")
public class ConsumerdemoServiceImpl implements DemoService {
    @RpcServiceConsumer
    public String s;
}
