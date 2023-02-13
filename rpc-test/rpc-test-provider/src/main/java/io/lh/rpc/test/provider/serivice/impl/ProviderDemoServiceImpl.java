package io.lh.rpc.test.provider.serivice.impl;

import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.test.api.TestService;
import io.lh.rpc.test.provider.serivice.TestDemoService;

@RpcServiceProvider(interfaceClassName = "io.lh.rpc.test.provider.serivice.TestDemoService",version = "1.0.0", group = "lh")
public class ProviderDemoServiceImpl implements TestDemoService {
}
