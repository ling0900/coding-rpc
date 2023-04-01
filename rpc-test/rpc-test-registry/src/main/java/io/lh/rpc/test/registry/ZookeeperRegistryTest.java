package io.lh.rpc.test.registry;

import io.lh.rpc.protocol.meta.ServiceMeta;
import io.lh.rpc.registry.api.RegistryService;
import io.lh.rpc.registry.api.config.RegistryConfig;
import io.lh.rpc.registyr.zookeeper.ZookeeperRegistryService;
import org.junit.Before;
import org.junit.Test;

public class ZookeeperRegistryTest {
    private RegistryService registryService;
    private ServiceMeta serviceMeta;


    @Before
    public void init() throws Exception {

        // zookeeper注册的地址
        RegistryConfig registryConfig = new RegistryConfig("127.0.0.1:2188", "zookeeper");
        this.registryService = new ZookeeperRegistryService();
        this.registryService.init(registryConfig);
        // todo
        this.serviceMeta = new ServiceMeta(ZookeeperRegistryTest.class.getName(), "1.0.0", "127.0.0.1", 8080, "lh");
    }

    @Test
    public void testRegister() throws Exception {
        this.registryService.register(serviceMeta);
    }

    @Test
    public void testUnRegister() throws Exception {
        this.registryService.unRegister(serviceMeta);
    }

    @Test
    public void discovery() throws Exception {
        this.registryService.discovery(RegistryService.class.getName(), "lh".hashCode());
    }

    @Test
    public void destroy() throws Exception {
        this.registryService.destroy();
    }
}
