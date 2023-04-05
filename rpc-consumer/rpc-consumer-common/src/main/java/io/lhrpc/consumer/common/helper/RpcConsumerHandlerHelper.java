package io.lhrpc.consumer.common.helper;

import io.lh.rpc.protocol.meta.ServiceMeta;
import io.lhrpc.consumer.common.handler.RpcConsumerHandler;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcConsumerHandlerHelper {
    private static Map<String, RpcConsumerHandler> rpcConsumerHandlerMap;

    static {
        // 这里的并发MAP的应用
        rpcConsumerHandlerMap = new ConcurrentHashMap<>();
    }

    // 元信息的重要性！
    public static String getKey(ServiceMeta key) {
        return key.getServiceAddr().concat("_").concat(String.valueOf(key.getServicePort()));
    }

    public static void put(ServiceMeta key, RpcConsumerHandler value) {
        rpcConsumerHandlerMap.put(getKey(key), value);
    }

    public static RpcConsumerHandler get(ServiceMeta key) {
        return rpcConsumerHandlerMap.get(getKey(key));
    }

    public static void closeRpcClientHandler()
    {
        // 不错的用法，工作中就很少见。
        Collection<RpcConsumerHandler> rpcConsumerHandlers = rpcConsumerHandlerMap.values();
        if (rpcConsumerHandlers != null) {
            rpcConsumerHandlers.stream().forEach((rpcConsumerHandler) ->{
                rpcConsumerHandler.close();
            });
        }
        // 调优的代码！
        rpcConsumerHandlerMap.clear();
    }

}
