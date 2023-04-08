package io.lh.rpc.loadbalancer.api;

import java.util.List;

/**
 * The interface Service load balancer.
 *
 * @param <T> the type parameter
 */
public interface ServiceLoadBalancer<T> {

    /**
     * 负载均衡 选择 服务节点
     *
     * @param servers  the servers 服务列表
     * @param hashCode the hash code
     * @return the t
     */
    T select(List<T> servers, int hashCode);
}
