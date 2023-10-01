package io.lh.rpc.commom.exception;

/**
 * <p>
 *
 * </p>
 *
 * @author: Ling
 * @date: 2023年10月01日 16:07
 * @since 1.0.0
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = -909090L;


    public RpcException(final Throwable e) {
        super(e);
    }

    public RpcException(final String message) {
        super(message);
    }

    public RpcException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}

