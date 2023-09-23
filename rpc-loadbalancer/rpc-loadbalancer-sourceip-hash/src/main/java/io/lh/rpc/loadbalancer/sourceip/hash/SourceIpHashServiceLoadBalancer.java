package io.lh.rpc.loadbalancer.sourceip.hash;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@SPIClass
@Slf4j
public class SourceIpHashServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {
    @Override
    public T select(List<T> servers, int hashCode, String ip) {
        log.info("ip 负载均衡");
        if (servers == null || servers.isEmpty()){
            return null;
        }

        if (StringUtils.isEmpty(ip)){
            return servers.get(0);
        }
        int newHashCode = Math.abs(ip.hashCode() + hashCode);
        return servers.get(newHashCode % servers.size());
    }
}
