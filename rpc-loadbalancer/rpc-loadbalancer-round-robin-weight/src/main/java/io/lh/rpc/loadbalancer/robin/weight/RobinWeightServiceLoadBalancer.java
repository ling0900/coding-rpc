package io.lh.rpc.loadbalancer.robin.weight;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Robin weight service load balancer.
 *
 * @author Ling
 * @param <T> the type parameter
 */
@SPIClass
public class RobinWeightServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {

    private static final Logger log = LoggerFactory.getLogger(RobinWeightServiceLoadBalancer.class);

    /**
     * The Atomic integer.
     */
    private volatile AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * Select t.
     *
     * @param servers  the servers
     * @param hashCode the hash code
     * @return the t
     */
    @Override
    public T select(List<T> servers, int hashCode, String ip) {
        log.info("基于-加权轮询算法-的负载均衡策略");

        if (CollectionUtils.isEmpty(servers)) return null;

        hashCode = Math.abs(hashCode);
        int count = hashCode % servers.size();

        count = count <= 0
                ? servers.size()
                : count ;

        int index = atomicInteger.incrementAndGet();

        if (index >= Integer.MAX_VALUE - 10000) {
            atomicInteger.set(0);
        }
        return servers.get(index % count);
    }
}
