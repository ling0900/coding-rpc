package io.lh.rpc.protocol.response;

import io.lh.rpc.protocol.base.RpcMessage;

public class RpcResponse extends RpcMessage {

    private static final long serialVersionUID = -1476396128296398868L;

    private String error;
    private Object result;

    private int code;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
