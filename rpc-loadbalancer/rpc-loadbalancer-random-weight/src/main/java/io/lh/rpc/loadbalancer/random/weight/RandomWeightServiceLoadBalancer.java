package io.lh.rpc.loadbalancer.random.weight;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * The type Random weight service load balancer.
 *
 * @author Ling
 * @param <T> the type parameter
 */
@SPIClass
@Slf4j
public class RandomWeightServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {
    /**
     * Select t.
     *
     * @param servers  the servers
     * @param hashCode the hash code
     * @return the t
     */
    @Override
    public T select(List<T> servers, int hashCode) {
        log.info("基于加权随机算法的负载均衡策略～～～～");
        if (CollectionUtils.isEmpty(servers)) {
            return null;
        }

        hashCode = Math.abs(hashCode);

        int count = hashCode % servers.size();
        if (count <= 1) {
            count = servers.size();
        }

        int index = new Random().nextInt(count);

        return servers.get(index);
    }
}
