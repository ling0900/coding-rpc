package io.lh.rpc.demo.consumer;

import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.consumer.RpcClient;
import io.lh.rpc.demo.api.DemoService;
import io.lh.rpc.proxy.api.async.IAsyncObjectProxy;
import io.lh.rpc.proxy.api.future.RpcFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class ConsumerNativeDemo {
    private RpcClient rpcClient;

    @Before
    public void initRpcClient(){
        rpcClient = new RpcClient("1.0.0",
                "lh", 3000
                , RpcConstants.REFLECT_TYPE_JDK
                ,false, false
                , "8.130.123.59:2181", "zookeeper", "jdk",
                "hash", 30000, 60000, 1000, 3,
                false, 10000);
    }


    @Test
    public void testInterfaceRpc() throws InterruptedException {
        DemoService demoService = rpcClient.create(DemoService.class);

        for (int i = 0; i < 4; i++) {
            String result = demoService.helloDemo("00000000");
            log.info("第{}次，返回的结果数据===>>>{} ", i+1, result);
        }


        //rpcClient.shutdown();

        // 阻塞模拟
        while (true){
            Thread.sleep(1000);
        }
    }

    @Test
    public void testAsyncInterfaceRpc() throws Exception {
        IAsyncObjectProxy demoService = rpcClient.createAsync(DemoService.class);
        RpcFuture future = demoService.call("hello", "00000000");
        log.info("返回的结果数据===>>> " + future.get());
        rpcClient.shutdown();
    }

}
