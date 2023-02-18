package io.lhrpc.consumer.common.context;

import io.lhrpc.consumer.common.future.RpcFuture;

/**
 * The type Rpc context.
 *
 * @author lh
 */
public class RpcContext {

    /**
     * 上下文
     */
    private RpcContext(){}

    /**
     * RpcContext的实例
     */
    private static final RpcContext AGENT = new RpcContext();

    /**
     * 存放future
     */
    private static final InheritableThreadLocal<RpcFuture> RPC_FUTURE_INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * Gets context.
     * RPC服务的上下文信息在这里
     *
     * @return the context
     */
    public static RpcContext getContext() {
        return AGENT;
    }

    /**
     * Sets rpc future.
     * ！上下文信息保存到了本地线程，且这个线程可以被子类继承获取到。
     *
     * @param rpcFuture the rpc future
     */
    public void setRpcFuture(RpcFuture rpcFuture) {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.set(rpcFuture);
    }

    /**
     * Gets rpc future.
     *
     * @return the rpc future
     */
    public RpcFuture getRpcFuture() {
        return RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get();
    }

    /**
     * Remove rpc future.
     */
    public void removeRpcFuture() {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.remove();
    }

}
