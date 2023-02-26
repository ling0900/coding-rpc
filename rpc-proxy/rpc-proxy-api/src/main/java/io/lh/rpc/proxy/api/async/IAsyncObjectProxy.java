package io.lh.rpc.proxy.api.async;

import io.lh.rpc.proxy.api.future.RpcFuture;

/**
 * <p>描述：</p>
 * <p>版本：1.0.0</p>
 * <p>@author：lh</p>
 * <p>创建时间：2023/02/27</p>
 */
public interface IAsyncObjectProxy {
    /**
     * 调用方法，远程异步，消费者端。
     * @param funName
     * @param args
     * @return {@link RpcFuture}
     */
    RpcFuture call(String funName, Object ... args);
}
