package io.lh.rpc.protocol.header;

import java.io.Serializable;

/**
 * The type Rpc header.
 */
@SuppressWarnings("ALL")
public class RpcHeader implements Serializable {

    private static final long serialVersionUID = 5008396687284215527L;

    private short magicNum;

    private byte msgType;

    private byte status;
    private long requestId;

    private String serializationType;

    private int msgLen;

    /**
     * Gets status.
     *
     * @return the status
     */
    public byte getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(byte status) {
        this.status = status;
    }

    /**
     * Gets magic num.
     *
     * @return the magic num
     */
    public short getMagicNum() {
        return magicNum;
    }

    /**
     * Sets magic num.
     *
     * @param magicNum the magic num
     */
    public void setMagicNum(short magicNum) {
        this.magicNum = magicNum;
    }

    /**
     * Gets msg type.
     *
     * @return the msg type
     */
    public byte getMsgType() {
        return msgType;
    }

    /**
     * Sets msg type.
     *
     * @param msgType the msg type
     */
    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    /**
     * Gets request id.
     *
     * @return the request id
     */
    public long getRequestId() {
        return requestId;
    }

    /**
     * Sets request id.
     *
     * @param requestId the request id
     */
    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets serialization type.
     *
     * @return the serialization type
     */
    public String getSerializationType() {
        return serializationType;
    }

    /**
     * Sets serialization type.
     *
     * @param serializationType the serialization type
     */
    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    /**
     * Gets msg len.
     *
     * @return the msg len
     */
    public int getMsgLen() {
        return msgLen;
    }

    /**
     * Sets msg len.
     *
     * @param msgLen the msg len
     */
    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }
}
