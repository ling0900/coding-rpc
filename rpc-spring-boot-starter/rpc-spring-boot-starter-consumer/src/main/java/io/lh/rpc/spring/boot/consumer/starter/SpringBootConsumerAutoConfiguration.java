package io.lh.rpc.spring.boot.consumer.starter;

import io.lh.rpc.consumer.RpcClient;
import io.lh.rpc.spring.boot.consumer.config.SpringBootConsumerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
        return new RpcClient(
                springBootConsumerConfig.getVersion(),
                springBootConsumerConfig.getGroup(),
                springBootConsumerConfig.getTimeout(),
                springBootConsumerConfig.getSerializationType(),
                springBootConsumerConfig.isAsync(),
                springBootConsumerConfig.isOneway(),
                springBootConsumerConfig.getRegistryAddress(),
                springBootConsumerConfig.getRegistryType(),
                springBootConsumerConfig.getProxy(),
                springBootConsumerConfig.getLoadBalanceType(),
                springBootConsumerConfig.getHeartbeatInterval(),
                springBootConsumerConfig.getScanNotActiveChannelInterval(),
                springBootConsumerConfig.getRetryInterval(),
                springBootConsumerConfig.getRetryTimes());
    }
}
