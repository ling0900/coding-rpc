package io.lh.rpc.test.consumer;

import io.lh.rpc.consumer.RpcClient;
import io.lh.rpc.proxy.api.async.IAsyncObjectProxy;
import io.lh.rpc.proxy.api.future.RpcFuture;
import io.lh.rpc.test.api.DemoService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>描述：</p>
 * <p>版本：1.0.0</p>
 * <p>@author：lh</p>
 * <p>创建时间：2023/02/27</p>
 */
public class RpcConsumerNativeTest {

    /**
     *
     */
    private RpcClient rpcClient;

    /**
     * 代替main里面的
     */
    @Before
    public void initRpcClient(){
        rpcClient = new RpcClient("1.0.0",
                "lh", 3000, "jdk"
                ,false, false);
    }

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerNativeTest.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient("1.0.0",
                "lh", 300, "jdk", false, false);

        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("lh");
        LOGGER.info("封装后的返回数据" + result);
        rpcClient.shutdown();
    }

    /**
     * 异步测试
     * @throws Exception
     */
    @Test
    public void testAsyncInterfaceRpc() throws Exception {
        IAsyncObjectProxy demoService = rpcClient.createAsync(DemoService.class);
        // 直接调用方法就行了
        RpcFuture future = demoService.call("hello", "qwe");
        LOGGER.info("返回的数据===>>> " + future.get());
        rpcClient.shutdown();
    }
}
