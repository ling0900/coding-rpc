package io.lh.rpc.loadbalancer.hash.weight;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * The type Hash weight service load balancer.
 *
 * @param <T> the type parameter
 * @author Ling
 */
@SPIClass
@Slf4j
public class HashWeightServiceLoadBalancer<T>  implements ServiceLoadBalancer<T> {

    /**
     * Select t.
     *
     * @param servers  the servers
     * @param hashCode the hash code
     * @return the t
     */
    @Override
    public T select(List<T> servers, int hashCode, String ip) {
        log.info("负载均衡：HashWeightServiceLoadBalancer -->");
        if (CollectionUtils.isEmpty(servers)) return null;
        hashCode = Math.abs(hashCode);
        int count = hashCode % servers.size();
        if (count <= 0) count = servers.size();
        return servers.get(hashCode % count);
    }
}
