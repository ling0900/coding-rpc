package io.lh.rpc.registyr.zookeeper;

import io.lh.rpc.commom.helper.RpcServiceHelper;
import io.lh.rpc.protocol.meta.ServiceMeta;
import io.lh.rpc.registry.api.RegistryService;
import io.lh.rpc.registry.api.config.RegistryConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * 服务注册发现类  基于 zookeeper实现
 */
public class ZookeeperRegistryService implements RegistryService {

    /**
     * 重试的间隔时间
     */
    public static final int BASE_SLEEP_TIME_MS = 1000;

    /**
     * 重试的次数，注意这里的重试都是在初始化 CuratorFramework时候用到的
     */
    public static final int MAX_RETRIES = 3;

    /**
     * zookeeper的跟路径
     */
    public static final String ZK_BASE_PATH = "/coding_rpc";

    /**
     * 服务发现的方法！
     */
    private ServiceDiscovery<ServiceMeta> serviceDiscovery;

    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceMetaServiceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup()))
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceMetaServiceInstance);
    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceMetaServiceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(serviceMeta.getServiceName())
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.unregisterService(serviceMetaServiceInstance);
    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        ServiceInstance<ServiceMeta> instance = this.selectOneServiceInstance((List<ServiceInstance<ServiceMeta>>) serviceInstances);
        if (instance != null) {
            return instance.getPayload();
        }
        return null;
    }

    @Override
    public void destroy() throws Exception {
        serviceDiscovery.close();
    }

    @Override
    public void init(RegistryConfig registryConfig) throws Exception {
        CuratorFramework curatorFrameworkClient = CuratorFrameworkFactory.newClient(registryConfig.getRegistryAddr(),
                new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        curatorFrameworkClient.start();
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder
                .builder(ServiceMeta.class)
                .client(curatorFrameworkClient)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH).build();
        this.serviceDiscovery.start();
    }

    private ServiceInstance<ServiceMeta> selectOneServiceInstance(List<ServiceInstance<ServiceMeta>> serviceInstances) {
        if (serviceInstances == null || serviceInstances.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(serviceInstances.size());
        return serviceInstances.get(index);
    }
}
