package io.lh.rpc.spring.boot.consumer.starter;

import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.consumer.RpcClient;
import io.lh.rpc.consumer.spring.RpcConsumerFactoryBean;
import io.lh.rpc.consumer.spring.context.RpcConsumerSpringContext;
import io.lh.rpc.spring.boot.consumer.config.SpringBootConsumerConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Spring boot consumer auto configuration.
 */
@Configuration
@EnableConfigurationProperties
public class SpringBootConsumerAutoConfiguration {

    /**
     * Spring boot consumer config spring boot consumer config.
     *
     * @return the spring boot consumer config
     */
    @Bean
    @ConfigurationProperties(prefix = "rpc.config.consumer")
    public SpringBootConsumerConfig springBootConsumerConfig(){
        return new SpringBootConsumerConfig();
    }

    /**
     * Rpc client rpc client.
     *
     * @param springBootConsumerConfig the spring boot consumer config
     * @return the rpc client
     */
    @Bean
    public RpcClient rpcClient(final SpringBootConsumerConfig springBootConsumerConfig){
        return parseRpcClient(springBootConsumerConfig);
    }

    private RpcClient parseRpcClient(final SpringBootConsumerConfig config) throws BeansException {
        RpcConsumerFactoryBean bean = getRpcReferenceBean(config);
        bean.init();
        return  bean.getRpcClient();
    }

    /**
     * 先从Spring IOC容器中获取消费者的bean：
     * --若存在，部分属性字段为空，则使用config字段进行填充
     * --若不存在，则使用config构建该bean
     */
    private RpcConsumerFactoryBean getRpcReferenceBean(final SpringBootConsumerConfig springBootConsumerConfig){

        ApplicationContext context = RpcConsumerSpringContext.getInstance().getContext();
        RpcConsumerFactoryBean referenceBean = context.getBean(RpcConsumerFactoryBean.class);

        if (StringUtils.isEmpty(referenceBean.getGroup())
                || (RpcConstants.RPC_COMMON_DEFAULT_GROUP.equals(referenceBean.getGroup()) && !StringUtils.isEmpty(springBootConsumerConfig.getGroup()))){
            referenceBean.setGroup(springBootConsumerConfig.getGroup());
        }
        if (StringUtils.isEmpty(referenceBean.getVersion())
                || (RpcConstants.RPC_COMMON_DEFAULT_VERSION.equals(referenceBean.getVersion()) && !StringUtils.isEmpty(springBootConsumerConfig.getVersion()))){
            referenceBean.setVersion(springBootConsumerConfig.getVersion());
        }
        if (StringUtils.isEmpty(referenceBean.getRegistryType())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_REGISTRYTYPE.equals(referenceBean.getRegistryType()) && !StringUtils.isEmpty(springBootConsumerConfig.getRegistryType()))){
            referenceBean.setRegistryType(springBootConsumerConfig.getRegistryType());
        }
        if (StringUtils.isEmpty(referenceBean.getLoadBalanceType())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_LOADBALANCETYPE.equals(referenceBean.getLoadBalanceType()) && !StringUtils.isEmpty(springBootConsumerConfig.getLoadBalanceType()))){
            referenceBean.setLoadBalanceType(springBootConsumerConfig.getLoadBalanceType());
        }
        if (StringUtils.isEmpty(referenceBean.getSerializationType())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_SERIALIZATIONTYPE.equals(referenceBean.getSerializationType()) && !StringUtils.isEmpty(springBootConsumerConfig.getSerializationType()))){
            referenceBean.setSerializationType(springBootConsumerConfig.getSerializationType());
        }
        if (StringUtils.isEmpty(referenceBean.getRegistryAddress())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_REGISTRYADDRESS.equals(referenceBean.getRegistryAddress()) && !StringUtils.isEmpty(springBootConsumerConfig.getRegistryAddress()))){
            referenceBean.setRegistryAddress(springBootConsumerConfig.getRegistryAddress());
        }
        if (referenceBean.getTimeout() <= 0
                || (RpcConstants.RPC_REFERENCE_DEFAULT_TIMEOUT == referenceBean.getTimeout() && springBootConsumerConfig.getTimeout() > 0)){
            referenceBean.setTimeout(springBootConsumerConfig.getTimeout());
        }
        if (!referenceBean.isAsync()){
            referenceBean.setAsync(springBootConsumerConfig().isAsync());
        }
        if (!referenceBean.isOneway()){
            referenceBean.setOneway(springBootConsumerConfig().isOneway());
        }
        if (StringUtils.isEmpty(referenceBean.getProxy())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_PROXY.equals(referenceBean.getProxy()) && !StringUtils.isEmpty(springBootConsumerConfig.getProxy()) )){
            referenceBean.setProxy(springBootConsumerConfig.getProxy());
        }
        if (referenceBean.getHeartbeatInterval() <= 0
                || (RpcConstants.RPC_COMMON_DEFAULT_HEARTBEATINTERVAL == referenceBean.getHeartbeatInterval() && springBootConsumerConfig.getHeartbeatInterval() > 0 )){
            referenceBean.setHeartbeatInterval(springBootConsumerConfig.getHeartbeatInterval());
        }
        if (referenceBean.getRetryInterval() <= 0
                || (RpcConstants.RPC_REFERENCE_DEFAULT_RETRYINTERVAL == referenceBean.getRetryInterval() && springBootConsumerConfig.getRetryInterval() > 0)){
            referenceBean.setRetryInterval(springBootConsumerConfig.getRetryInterval());
        }
        if (referenceBean.getRetryTimes() <= 0
                || (RpcConstants.RPC_REFERENCE_DEFAULT_RETRYTIMES == referenceBean.getRetryTimes() && springBootConsumerConfig.getRetryTimes() > 0)){
            referenceBean.setRetryTimes(springBootConsumerConfig.getRetryTimes());
        }
        if (referenceBean.getScanNotActiveChannelInterval() <= 0
                || (RpcConstants.RPC_COMMON_DEFAULT_SCANNOTACTIVECHANNELINTERVAL == referenceBean.getScanNotActiveChannelInterval() && springBootConsumerConfig.getScanNotActiveChannelInterval() > 0)){
            referenceBean.setScanNotActiveChannelInterval(springBootConsumerConfig().getScanNotActiveChannelInterval());
        }

        return referenceBean;
    }
}
