package io.lh.rpc.protocol.base;

import java.io.Serializable;

/**
 * The type Rpc message.
 */
@SuppressWarnings("ALL")
public class RpcMessage implements Serializable {

    private boolean oneWay;

    private boolean async;

    /**
     * Is one way boolean.
     *
     * @return the boolean
     */
    public boolean isOneWay() {
        return oneWay;
    }

    /**
     * Sets one way.
     *
     * @param oneWay the one way
     */
    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    /**
     * Is async boolean.
     *
     * @return the boolean
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * Sets async.
     *
     * @param async the async
     */
    public void setAsync(boolean async) {
        this.async = async;
    }
}
