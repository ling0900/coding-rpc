package io.lh.rpc.loadbalancer.hash;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;


@SPIClass
public class HashServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {

    private static final Logger log = LoggerFactory.getLogger(HashServiceLoadBalancer.class);

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
