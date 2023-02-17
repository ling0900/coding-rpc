package io.lh.rpc.protocol;

import io.lh.rpc.protocol.header.RpcHeader;

import java.io.Serializable;

/**
 * The type Rpc protocol.
 *
 * @param <T> the type parameter
 */
public class RpcProtocol<T> implements Serializable {

    private RpcHeader header;

    private T body;

    /**
     * Gets header.
     *
     * @return the header
     */
    public RpcHeader getHeader() {
        return header;
    }

    /**
     * Sets header.
     *
     * @param header the header
     */
    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public T getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(T body) {
        this.body = body;
    }
}
