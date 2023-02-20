package io.lh.rpc.proxy.api.callback;

/**
 * The interface Async rpc callback.
 *
 * @author lh
 */
public interface AsyncRpcCallback {

    /**
     * On success.
     * 成功后的回调方法
     * @param result the result
     */
    void onSuccess(Object result);

    /**
     * On exception.
     * 异常后的回调方法
     * @param e the e
     */
    void onException(Exception e);
}
