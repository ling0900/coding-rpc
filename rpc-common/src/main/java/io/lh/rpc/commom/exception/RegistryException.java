package io.lh.rpc.commom.exception;

/**
 * <p>
 *
 * </p>
 *
 * @author: Ling
 * @date: 2023年10月01日 16:08
 * @since 1.0.0
 */
public class RegistryException extends RuntimeException {

    private static final long serialVersionUID = -90909323L;


    public RegistryException(final Throwable e) {
        super(e);
    }

    public RegistryException(final String message) {
        super(message);
    }

    public RegistryException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}

