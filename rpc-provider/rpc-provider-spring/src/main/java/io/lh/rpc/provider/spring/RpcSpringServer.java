package io.lh.rpc.provider.spring;

import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.commom.helper.RpcServiceHelper;
import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.protocol.meta.ServiceMeta;
import io.lh.rpc.provider.common.server.base.BaseServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * The type Rpc spring server.
 */
@Slf4j
public class RpcSpringServer extends BaseServer implements ApplicationContextAware, InitializingBean {

    /**
     * Instantiates a new Base server.
     *
     * @param serverAddress                the server address
     * @param registryAddress              the registry address
     * @param registryType                 the registry type
     * @param reflectType                  the reflect type
     * @param registryLoadBalanceType      the registry load balance type
     * @param heartbeatInterval            the heartbeat interval
     * @param scanNotActiveChannelInterval the scan not active channel interval
     */
    public RpcSpringServer(String serverAddress, String registryAddress,
                           String registryType, String reflectType,
                           String registryLoadBalanceType, int heartbeatInterval, int scanNotActiveChannelInterval) {
        super(serverAddress, registryAddress, registryType, reflectType, registryLoadBalanceType, heartbeatInterval, scanNotActiveChannelInterval);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 识别注解 生产者，将注册到bean容器中的服务提供者拿出来，然后注册到我们自己的 registryService
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcServiceProvider.class);

        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcServiceProvider rpcService = serviceBean.getClass().getAnnotation(RpcServiceProvider.class);
                ServiceMeta serviceMeta = new ServiceMeta(this.getServiceName(rpcService), rpcService.version(), rpcService.group(), host, port, getWeight(rpcService.weight()));
                handlerMap.put(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup()), serviceBean);
                try {
                    registryService.register(serviceMeta);
                }catch (Exception e){
                    log.error("rpc server init spring exception:{}", e);
                }
            }
        }


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.startNettyServer();
    }

    /**
     * Gets weight.
     *
     * @param weight the weight
     * @return the weight
     */
    private int getWeight(int weight) {
        if (weight < RpcConstants.SERVICE_WEIGHT_MIN){
            weight = RpcConstants.SERVICE_WEIGHT_MIN;
        }
        if (weight > RpcConstants.SERVICE_WEIGHT_MAX){
            weight = RpcConstants.SERVICE_WEIGHT_MAX;
        }
        return weight;
    }

    /**
     * 获取serviceName
     *
     * @param rpcService the rpc service
     * @return the string
     */
    private String getServiceName(RpcServiceProvider rpcService){
        //优先使用interfaceClass
        Class clazz = rpcService.interfaceClass();
        if (clazz == void.class){
            return rpcService.interfaceClassName();
        }
        String serviceName = clazz.getName();
        if (serviceName == null || serviceName.trim().isEmpty()){
            serviceName = rpcService.interfaceClassName();
        }
        return serviceName;
    }

}
