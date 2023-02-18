package io.lh.rpc.test.consumer.handler;

import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.header.RpcHeaderFactory;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lhrpc.consumer.common.RpcConsumer;

/**
 * The type Rpc consumer handler test.
 * @author lh
 */
public class RpcConsumerHandlerTest {
    public static void main(String[] args) throws InterruptedException {
        RpcConsumer consumer = RpcConsumer.getConsumerInstance();
        consumer.sendRequestMsg(getRpcRequestProtocol());

        Thread.sleep(2000);
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
