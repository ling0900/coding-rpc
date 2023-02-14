package io.lh.rpc.protocol.header;

import java.io.Serializable;

public class RpcHeader implements Serializable {

    private static final long serialVersionUID = 5008396687284215527L;

    private short magicNum;

    private byte msgType;

    private byte status;
    private long requestId;

    private String serializationType;

    private int msgLen;

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public short getMagicNum() {
        return magicNum;
    }

    public void setMagicNum(short magicNum) {
        this.magicNum = magicNum;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public int getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }
}
