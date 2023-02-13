package io.lh.rpc.protocol.base;

import java.io.Serializable;

public class RpcMessage implements Serializable {

    private boolean oneWay;

    private boolean async;

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
