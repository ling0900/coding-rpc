package io.lh.rpc.test.provider.starter;

import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * The type Single starter provider test.
 * 不同的test代表不同的测试。
 *
 * @author lh
 */
public class SingleStarterProviderTest {
    /**
     * Test starter.
     * 用来测试zk注册中心心跳💗💗💗💗💗💗💗💗
     */
    @Test
    public void testStarterJdk() {
        RpcSingleServer rpcSingleServer = new RpcSingleServer(
                "127.0.0.1:27788",
                "io.lh.rpc.test",
                RpcConstants.REFLECT_TYPE_JDK,
                "8.130.123.59:2181",
                "zookeeper", "random",
                3000, 6000);
        rpcSingleServer.startNettyServer();

    }

    /**
     * Test starter.
     * 这个体现的是动态设置好注册中心，然后实现的注册的。
     */
    @Test
    public void testStarter() {
        RpcSingleServer rpcSingleServer = new RpcSingleServer(
                "127.0.0.1:27788",
                "io.lh.rpc.test",
                RpcConstants.REFLECT_TYPE_CGLIB,
                "8.130.65.0:2181",
                "zookeeper", "random",
                3000, 6000);
        rpcSingleServer.startNettyServer();

    }
}
