package io.lh.rpc.test.provider.starter;

import io.lh.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @author lh
 */
public class SingleStarterProviderTest {
    @Test
    public void testStarter() {
        RpcSingleServer rpcSingleServer = new RpcSingleServer("127.0.0.1:27780",
                "io.lh.rpc.test");
        rpcSingleServer.startNettyServer();

    }
}
