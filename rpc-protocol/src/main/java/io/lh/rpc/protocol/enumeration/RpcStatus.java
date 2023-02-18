package io.lh.rpc.protocol.enumeration;

/**
 * The enum Rpc status.
 *
 * @author lh
 */
public enum RpcStatus {

    /**
     * Success rpc status.
     * 服务调用成功的状态
     */
    SUCCESS(0),
    /**
     * Fail rpc status.
     */
    FAIL(1);

    private final int code;

    RpcStatus(int code) {
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }
}
