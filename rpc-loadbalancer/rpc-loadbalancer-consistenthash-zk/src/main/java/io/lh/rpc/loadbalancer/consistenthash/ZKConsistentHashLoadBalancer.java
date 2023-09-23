package io.lh.rpc.loadbalancer.consistenthash;

import io.lh.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.lh.rpc.protocol.meta.ServiceMeta;
import io.lh.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The type Zk consistent hash load balancer.
 *
 *
 */
@Slf4j
@SPIClass
public class ZKConsistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>> {
    /**
     * The constant VIRTUAL_NODE_SIZE.
     * 实现一致性hash算法中用到的环的大小
     */
    private final static int VIRTUAL_NODE_SIZE = 10;

    /**
     * The constant VIRTUAL_NODE_SPLIT.
     */
    private final static String VIRTUAL_NODE_SPLIT = "#";

    @Override
    public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, int hashCode, String ip) {
        log.info("基于Zk实现⼀致性Hash算法的负载均衡策略");
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = makeConsistentHashRing(servers);
        return allocateNode(ring, hashCode);
    }

    /**
     * Allocate node service instance.
     *
     * @param ring     the ring
     * @param hashCode the hash code
     * @return the service instance
     */
    private ServiceInstance<ServiceMeta> allocateNode(TreeMap<Integer, ServiceInstance<ServiceMeta>> ring, int hashCode) {
        Map.Entry<Integer, ServiceInstance<ServiceMeta>> entry = ring.ceilingEntry(hashCode);
        if (entry == null) {
            entry = ring.firstEntry();
        }
        if (entry == null) {
            throw new RuntimeException("not discover useful service, please register service in registry center.");
        }
        return entry.getValue();
    }

    /**
     * Make consistent hash ring tree map.
     *
     * @param servers the servers
     * @return the tree map
     */
    private TreeMap<Integer, ServiceInstance<ServiceMeta>> makeConsistentHashRing(List<ServiceInstance<ServiceMeta>> servers) {
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = new TreeMap<>();
        // 首先从外层是实际节点的数量，如果虚拟了10个节点，这样会放在
        for (ServiceInstance<ServiceMeta> instance : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                log.info("hashRingCode=" + (buildServiceInstanceKey(instance) + VIRTUAL_NODE_SPLIT + i).hashCode());
                ring.put((buildServiceInstanceKey(instance) + VIRTUAL_NODE_SPLIT + i).hashCode()
                        , instance);
            }
        }
        return ring;
    }

    /**
     * Build service instance key string.
     *
     * @param instance the instance
     * @return the string
     */
    private String buildServiceInstanceKey(ServiceInstance<ServiceMeta> instance) {
        ServiceMeta payload = instance.getPayload();
        return String.join(":", payload.getServiceAddr(), String.valueOf(payload.getServicePort()));
    }

}