package io.lh.rpc.protocol.enumeration;

/**
 * The enum Rpc type.
 */
public enum RpcType {
    /**
     * Heartbeat rpc type.
     */
    HEARTBEAT(3),
    /**
     * Request rpc type.
     */
    REQUEST(1),
    /**
     * Response rpc type.
     */
    RESPONSE(2);

    private final int type;

    RpcType(int type) {
        this.type = type;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Finde by type rpc type.
     *
     * @param type the type
     * @return the rpc type
     */
    public static RpcType findeByType(int type) {
        for (RpcType rpcType : RpcType.values()) {
            if (rpcType.getType() == type) {
                return rpcType;
            }
        }
        return null;
    }
}
