package io.lh.rpc.test.provider.testStarter;

import io.lh.rpc.provider.RpcSingleServer;
import org.junit.Test;

public class SingleTestStarterProvider {
    @Test
    public void testStarter() {
        RpcSingleServer rpcSingleServer = new RpcSingleServer("127.0.0.1:27660",
                "io.lh.rpc.test");
        rpcSingleServer.startNettyServer();

    }
}
