package io.lh.rpc.loadbalancer.sourceip.hash.weight;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * The type Source ip hash weight service load balancer.
 *
 * @param <T> the type parameter
 */
@SPIClass
@Slf4j
public class SourceIpHashWeightServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {
    @Override
    public T select(List<T> servers, int hashCode, String ip) {
        log.info("ip 权重 负载均衡-----");

        if (servers == null || servers.isEmpty()) {
            return null;
        }
        if (StringUtils.isEmpty(ip)) {
            return servers.get(0);
        }
        int count = Math.abs(hashCode) % servers.size();
        if (count == 0) {
            count = servers.size();
        }
        int resultHashCode = Math.abs(ip.hashCode() +
                hashCode);
        return servers.get(resultHashCode % count);
    }
}
