package io.lh.rpc.loadbalancer.robin;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SPIClass
public class RobinServiceLoadBalancer<T> implements ServiceLoadBalancer {

    private final Logger logger = LoggerFactory.getLogger(RobinServiceLoadBalancer.class);
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    public Object select(List servers, int hashCode, String ip) {
        logger.warn("负载均衡算法 轮询");
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        int count = servers.size();
        int index = atomicInteger.incrementAndGet();
        if (index >= (Integer.MAX_VALUE - 10000)) {
            atomicInteger.set(0);
        }
        return servers.get(index % count);
    }
}
