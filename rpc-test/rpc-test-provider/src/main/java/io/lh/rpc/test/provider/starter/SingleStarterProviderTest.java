package io.lh.rpc.test.provider.starter;

import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * The type Single starter provider test.
 *
 * @author lh
 */
public class SingleStarterProviderTest {
    /**
     * Test starter.
     */
    @Test
    public void testStarter() {
        RpcSingleServer rpcSingleServer = new RpcSingleServer("127.0.0.1:27780",
                "io.lh.rpc.test", RpcConstants.REFLECT_TYPE_CGLIB, "8.130.65.0:2181", "zookeeper");
        rpcSingleServer.startNettyServer();

    }
}
