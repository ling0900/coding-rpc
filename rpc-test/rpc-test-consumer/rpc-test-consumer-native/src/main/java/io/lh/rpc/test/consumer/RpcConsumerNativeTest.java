package io.lh.rpc.test.consumer;

import io.lh.rpc.consumer.RpcClient;
import io.lh.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcConsumerNativeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerNativeTest.class);

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient("1.0.0",
                "lh", 300, "jdk", false, false);

        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("lh");
        LOGGER.info("封装后的返回数据" + result);
        rpcClient.shutdown();
    }
}
