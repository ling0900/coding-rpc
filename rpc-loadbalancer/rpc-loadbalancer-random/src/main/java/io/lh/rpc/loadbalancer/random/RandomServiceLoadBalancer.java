package io.lh.rpc.loadbalancer.random;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import java.util.List;
import java.util.Random;

/**
 * The type Random service load balancer.
 * 一个负载均衡类（工具类）
 * @author lh
 * @param <T> the type parameter
 */
@SPIClass
public class RandomServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {

    private final Logger logger = LoggerFactory.getLogger(RandomServiceLoadBalancer.class);
    @Override
    public T select(List<T> servers, int hashCode) {
        logger.info("本次服务调用采用的负载均衡策略是：负载均衡策略（随机算法）");
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
