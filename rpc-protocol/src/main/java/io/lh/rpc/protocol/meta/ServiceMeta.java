package io.lh.rpc.protocol.meta;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务元数据类
 * @author lh
 */
@Data
public class ServiceMeta implements Serializable {

    private static final long serialVersionUID = 9072753631008847950L;

    private String serviceName;

    private String serviceVersion;

    private String serviceAddr;

    private int servicePort;

    private String serviceGroup;

    /**
     * 思考一下这个可以不写吗？
     */
    public ServiceMeta(){}

    public ServiceMeta(String serviceName, String serviceVersion, String serviceAddr, int servicePort, String serviceGroup) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.serviceAddr = serviceAddr;
        this.servicePort = servicePort;
        this.serviceGroup = serviceGroup;
    }
}
