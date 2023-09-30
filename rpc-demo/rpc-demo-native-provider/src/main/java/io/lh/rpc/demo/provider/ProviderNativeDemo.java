package io.lh.rpc.demo.provider;

import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.provider.RpcSingleServer;
import org.junit.Before;
import org.junit.Test;

public class ProviderNativeDemo {

    private String zk_registryAddress;

    @Before
    public void buildParas() {
        this.zk_registryAddress = "8.130.123.59:2181";
    }

    @Test
    public void startRpcSingleServer() {
        RpcSingleServer rpcSingleServer = new RpcSingleServer(
                "127.0.0.1:27880",
                "127.0.0.1:27880",
                "io.lh.rpc.demo",
                RpcConstants.REFLECT_TYPE_JDK,
                "8.130.123.59:2181",
                "zookeeper", "random",
                3000, 6000);
        rpcSingleServer.startNettyServer();

    }

}
