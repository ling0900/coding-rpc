package io.lh.rpc.test.provider.serivice.impl;

import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.test.api.TestService;

@RpcServiceProvider(interfaceClassName = "io.lh.rpc.test.api.TestService")
public class ProviderDemoServiceImpl implements TestService {
    @Override
    public String test(String para) {
        System.out.println("-----------test---------");
        return null;
    }
}
