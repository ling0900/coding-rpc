package io.lh.rpc.loadbalancer.random;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import java.util.List;
import java.util.Random;

/**
 * The type Random service load balancer.
 * 一个负载均衡类（工具类）
 * @param <T> the type parameter
 */
public class RandomServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {

    private final Logger logger = LoggerFactory.getLogger(RandomServiceLoadBalancer.class);
    @Override
    public T select(List<T> servers, int hashCode) {
        logger.info("负载均衡策略（随机算法）");
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
