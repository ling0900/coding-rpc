package io.lh.rpc.protocol;

import io.lh.rpc.protocol.header.RpcHeader;

import java.io.Serializable;

public class RpcProtocol<T> implements Serializable {

    private RpcHeader header;

    private T body;

    public RpcHeader getHeader() {
        return header;
    }

    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
