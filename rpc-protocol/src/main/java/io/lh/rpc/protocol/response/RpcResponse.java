package io.lh.rpc.protocol.response;

import io.lh.rpc.protocol.base.RpcMessage;

/**
 * The type Rpc response.
 */
@SuppressWarnings("ALL")
public class RpcResponse extends RpcMessage {

    private static final long serialVersionUID = -1476396128296398868L;

    private String error;
    private Object result;

    private int code;

    public boolean isError() {
        return error != null;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public Object getResult() {
        return result;
    }

    /**
     * Sets result.
     *
     * @param result the result
     */
    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }
}
