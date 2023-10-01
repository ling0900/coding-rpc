package io.lh.rpc.loadbalancer.helpler;

import io.lh.rpc.protocol.meta.ServiceMeta;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * <p>
 *
 * </p>
 *
 * @author: Ling
 * @date: 2023年10月01日 16:22
 * @since 1.0.0
 */
public class ServiceLoadBalancerHelper {
    private static volatile List<ServiceMeta> cacheServiceMeta = new CopyOnWriteArrayList<>();

    public static List<ServiceMeta> getServiceMetaList(List<ServiceInstance<ServiceMeta>> serviceInstances){
        if (serviceInstances == null || serviceInstances.isEmpty() || cacheServiceMeta.size() == serviceInstances.size()){
            return cacheServiceMeta;
        }
        //先清空cacheServiceMeta中的数据
        cacheServiceMeta.clear();
        serviceInstances.stream().forEach((serviceMetaServiceInstance) -> {
            cacheServiceMeta.add(serviceMetaServiceInstance.getPayload());
        });
        return cacheServiceMeta;
    }

}
