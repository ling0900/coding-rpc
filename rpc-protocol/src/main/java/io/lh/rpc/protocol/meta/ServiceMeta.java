package io.lh.rpc.protocol.meta;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务元数据类
 *
 * @author lh
 */
@Data
public class ServiceMeta implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 9072753631008847950L;

    /**
     * The Service name.
     */
    private String serviceName;

    /**
     * The Service version.
     */
    private String serviceVersion;

    /**
     * The Service addr.
     */
    private String serviceAddr;

    /**
     * The Service port.
     */
    private int servicePort;

    /**
     * The Service group.
     */
    private String serviceGroup;

    /**
     * 思考一下这个可以不写吗？
     */
    public ServiceMeta(){}

    /**
     * Instantiates a new Service meta.
     *
     * @param serviceName    the service name
     * @param serviceVersion the service version
     * @param serviceAddr    the service addr
     * @param servicePort    the service port
     * @param serviceGroup   the service group
     */
    public ServiceMeta(String serviceName, String serviceVersion, String serviceAddr, int servicePort, String serviceGroup) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.serviceAddr = serviceAddr;
        this.servicePort = servicePort;
        this.serviceGroup = serviceGroup;
    }
}
