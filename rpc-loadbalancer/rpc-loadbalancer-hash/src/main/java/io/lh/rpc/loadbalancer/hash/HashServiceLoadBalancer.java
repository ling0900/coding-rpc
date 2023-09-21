package io.lh.rpc.loadbalancer.hash;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * The type Hash service load balancer.
 *
 * @author Ling
 * @param <T> the type parameter
 */
@Slf4j
@SPIClass
public class HashServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {
    /**
     * Select t.
     *
     * @param servers  the servers
     * @param hashCode the hash code
     * @return the t
     */
    @Override
    public T select(List<T> servers, int hashCode) {
        log.info("hash算法的负载均衡");

        if (CollectionUtils.isEmpty(servers)) return null;

        int index = Math.abs(hashCode) % servers.size();

        return servers.get(index);
    }
}
