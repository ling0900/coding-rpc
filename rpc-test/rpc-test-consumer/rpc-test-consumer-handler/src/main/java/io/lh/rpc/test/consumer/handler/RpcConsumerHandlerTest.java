package io.lh.rpc.test.consumer.handler;

import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.header.RpcHeaderFactory;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lhrpc.consumer.common.RpcConsumer;
import io.lhrpc.consumer.common.handler.RpcConsumerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Rpc consumer handler test.
 * @author lh
 */
public class RpcConsumerHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(RpcConsumerHandler.class);

    public static void main(String[] args) throws InterruptedException {
        RpcConsumer consumer = RpcConsumer.getConsumerInstance();
        Object o = consumer.sendRequestMsg(getRpcRequestProtocol());

        logger.info("返回到消费者的数据{}", o.toString());

        consumer.close();
    }

    private static RpcProtocol<RpcRequest> getRpcRequestProtocol() {
        // 数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();

        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));

        RpcRequest request = new RpcRequest();

        request.setClassName("io.lh.rpc.test.api.DemoService");
        request.setGroup("lh");
        request.setMethodName("hello");
        request.setParameterTypes(new Class[]{String.class});
        request.setParameters(new Object[]{"发送的数据消费者"});
        request.setOneWay(false);
        request.setAsync(false);
        request.setVersion("1.0.0");
        protocol.setBody(request);

        return protocol;
    }
}
