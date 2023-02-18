package io.lhrpc.consumer.common.future;

import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;


/**
 * The type Rpc future.
 *
 * @author lh
 */
public class RpcFuture extends CompletableFuture<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcFuture.class);

    private Sync sync;
    private RpcProtocol<RpcRequest> requestRpcProtocol;

    private RpcProtocol<RpcResponse> responseRpcProtocol;

    private long startTime;

    private long responseTimeThreshold = 500;

    /**
     * Instantiates a new Rpc future.
     *
     * @param requestRpcProtocol the request rpc protocol
     */
    public RpcFuture(RpcProtocol<RpcRequest> requestRpcProtocol) {
        this.sync = new Sync();
        this.requestRpcProtocol = requestRpcProtocol;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return super.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        if (this.requestRpcProtocol != null) {
            return this.responseRpcProtocol.getBody().getResult();
        } else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.responseRpcProtocol != null) {
                return this.responseRpcProtocol.getBody().getResult();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException(String.format("请求超时，请求ID是%s，请求className是%s，请求方法name是%s",
                    this.requestRpcProtocol.getHeader().getRequestId(),
                    this.requestRpcProtocol.getBody().getClassName(),
                    this.requestRpcProtocol.getBody().getMethodName()));
        }
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    /**
     * Done.
     *
     * @param responseRpcProtocol the response rpc protocol
     */
    public void done(RpcProtocol<RpcResponse> responseRpcProtocol) {
        this.responseRpcProtocol = responseRpcProtocol;
        sync.release(1);

        // 阈值
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            LOGGER.warn(String.format("服务响应时间太久,请求id是%s，响应时间是%s_ms",
                    responseRpcProtocol.getHeader().getRequestId(),
                    responseTime));
        }
    }

    /**
     * The type Sync.
     * 根据AQS实现一个内部类。
     */
    static class Sync extends AbstractQueuedSynchronizer {

        // 版本号
        private static final long serialVersionUID = -4119868057075984392L;

        // futrue的status
        private final int done = 1;
        private final int pending = 0;

        protected boolean tryAcquire(int acquires) {
            return getState() == done;
        }

        protected boolean tryRelease(int releases) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Is done boolean.
         *
         * @return the boolean
         */
        public boolean isDone() {
            getState();
            return getState() == done;
        }
    }
}
