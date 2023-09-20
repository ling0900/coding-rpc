package io.lh.rpc.test.consumer.handler;

import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.header.RpcHeaderFactory;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.proxy.api.callback.AsyncRpcCallback;
import io.lh.rpc.proxy.api.future.RpcFuture;
import io.lhrpc.consumer.common.RpcConsumer;
import io.lhrpc.consumer.common.handler.RpcConsumerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * The type Rpc consumer handler test.
 *
 * @author lh
 */
public class RpcConsumerHandlerTest {

    /**
     * The constant logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RpcConsumerHandler.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {

        RpcConsumer consumer = RpcConsumer.getConsumerInstance(1,1,1,1);
        RpcFuture rpcFuture = consumer.sendRequestMsg(getRpcRequestProtocol());

        try {
            rpcFuture.addCallback(new AsyncRpcCallback() {
                @Override
                public void onSuccess(Object result) {
                    logger.info("onSuccess服务消费者获取到的数据{}", result);
                }

                @Override
                public void onException(Exception e) {
                    logger.info("异常{}",e);
                }
            });
        } catch (Exception e) {

        }


        Thread.sleep(200);

        consumer.close();
    }

    /**
     * Gets rpc request protocol.
     *
     * @return the rpc request protocol
     */
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
        request.setAsync(true);
        request.setVersion("1.0.0");
        protocol.setBody(request);

        return protocol;
    }
}
