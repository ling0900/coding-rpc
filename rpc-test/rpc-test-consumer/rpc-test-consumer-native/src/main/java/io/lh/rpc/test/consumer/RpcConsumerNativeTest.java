package io.lh.rpc.test.consumer;

import io.lh.rpc.constants.RpcConstants;
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
                "lh", 30000
                , RpcConstants.REFLECT_TYPE_JDK
                ,false, false
                , "8.130.65.0:2181", "zookeeper", "jdk",
                "robin");
    }

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerNativeTest.class);

    /**
     * Test interface rpc.
     * 用于测试基于zookeeper为注册中心的
     * @param args
     */
    @Test
    public void testInterfaceRpc() {
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("lh");
        LOGGER.info("封装后的返回数据" + result);
        rpcClient.shutdown();
    }

    /**
     * 异步测试 通过动态代理实现异步调用的测试类。
     * 这次commit为止，如果生产者那里用到的cglib，这里就会出现问题！测试时候。
     *
     * @throws Exception the exception
     */
    @Test
    public void testAsyncInterfaceRpc() throws Exception {
        // 这里的参数需要设置正确，否则会爆不存在的！
//        rpcClient = new RpcClient("1.0.0", "lh", 3000, "cglib", false, false);
        // 根据传入的接口，去服务提供者找对应的实现类（服务类）
        IAsyncObjectProxy demoService = rpcClient.createAsync(DemoService.class);
        // 直接调用方法就行了
        RpcFuture future = demoService.call("hello", "qwe");
        LOGGER.info("返回的数据===>>> " + future.get());
        rpcClient.shutdown();
    }

    public static void main(String[] args) {
    }
}
