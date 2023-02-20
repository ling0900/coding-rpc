package io.lh.rpc.proxy.api.consumer;

import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.proxy.api.future.RpcFuture;

/**
 * 描述：
 * 版本：1.0.0
 * @author ：lh
 * 创建时间：2023/02/20
 */
public interface Consumer {

    /**
     * 消费者发送request请求
     * @param protocol
     * @return {@link RpcFuture}
     * @throws Exception
     */
    RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception;
}
