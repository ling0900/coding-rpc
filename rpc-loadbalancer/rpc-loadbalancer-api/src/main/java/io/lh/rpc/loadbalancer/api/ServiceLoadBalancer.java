package io.lh.rpc.loadbalancer.api;

import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.spi.annotation.SPI;

import java.util.List;

/**
 * The interface Service load balancer.
 *
 * @author lh
 * @param <T> the type parameter
 */
@SPI(RpcConstants.SERVICE_LOAD_BALANCER_RANDOM)
public interface ServiceLoadBalancer<T> {

    /**
     * 负载均衡：选择服务节点
     * 服务列表是一个list，所以做负载均衡的时候，本质上是对index做选择，然后取出来对应位置的服务即可。
     * @param servers  the servers 服务列表
     * @param hashCode the hash code
     * @return the t
     */
    T select(List<T> servers, int hashCode);
}
