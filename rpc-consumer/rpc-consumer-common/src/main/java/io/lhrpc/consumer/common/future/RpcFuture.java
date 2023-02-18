package io.lhrpc.consumer.common.future;

import io.lh.rpc.commom.threadpool.ClientThreadPool;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import io.lhrpc.consumer.common.callback.AsyncRpcCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;


/**
 * The type Rpc future.
 *
 * @author lh
 */
public class RpcFuture extends CompletableFuture<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcFuture.class);

    /**
     * 用来方回调接口类的
     */
    private List<AsyncRpcCallback> pendingCallbacks = new ArrayList<>();

    /**
     * 回调的时候用来上锁、下锁
     */
    private ReentrantLock lock = new ReentrantLock();

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

        // 调用回调方法
        invokeCallbacks();
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
     * 核心！
     */
    static class Sync extends AbstractQueuedSynchronizer {

        // 版本号
        private static final long serialVersionUID = -4119868057075984392L;

        // futrue 的status
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

    private void runCallback(final AsyncRpcCallback callback) {
        final RpcResponse response = this.responseRpcProtocol.getBody();
        ClientThreadPool.submit(()->{
            if (! response.isError()) {
                callback.onSuccess(response.getResult());
            } else {
                callback.onException(new RuntimeException("响应出错", new Throwable(response.getError())));
            }
        });
    }

    /**
     * Add callback rpc future.
     *
     * @param callback the callback
     * @return the rpc future
     */
    public RpcFuture addCallback(AsyncRpcCallback callback) {
        lock.lock();
        try {
            if (isDone()) {
                runCallback(callback);
            } else {
                this.pendingCallbacks.add(callback);
            }
        } finally {
            // 记得释放锁！
            lock.unlock();
        }
        return this;
    }

    private void invokeCallbacks() {
        lock.lock();
        try {
            for (final AsyncRpcCallback callback : pendingCallbacks) {
                runCallback(callback);
            }
        } finally {
            lock.unlock();
        }
    }
}
