package io.lhrpc.consumer.common.callback;

/**
 * The interface Async rpc callback.
 *
 * @author lh
 */
public interface AsyncRpcCallback {

    /**
     * On success.
     *
     * @param result the result
     */
    void onSuccess(Object result);

    /**
     * On exception.
     *
     * @param e the e
     */
    void onException(Exception e);
}
