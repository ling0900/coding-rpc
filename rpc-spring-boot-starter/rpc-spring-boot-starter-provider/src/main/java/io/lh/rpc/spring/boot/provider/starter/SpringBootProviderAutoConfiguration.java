package io.lh.rpc.spring.boot.provider.starter;

import io.lh.rpc.provider.spring.RpcSpringServer;
import io.lh.rpc.spring.boot.provider.config.SpringBootProviderConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Spring boot provider auto configuration.
 */
@Configuration
public class SpringBootProviderAutoConfiguration {

    /**
     * Spring boot provider config spring boot provider config.
     *
     * @return the spring boot provider config
     */
    @Bean
    @ConfigurationProperties(prefix = "rpc.config.provider")
    public SpringBootProviderConfig springBootProviderConfig(){
        return new SpringBootProviderConfig();
    }

    /**
     * Rpc spring server rpc spring server.
     *
     * @param springBootProviderConfig the spring boot provider config
     * @return the rpc spring server
     */
    @Bean
    public RpcSpringServer rpcSpringServer(final SpringBootProviderConfig springBootProviderConfig){
        // 参数的顺序要注意！
        return new RpcSpringServer(
                springBootProviderConfig.getServerAddress(),
                springBootProviderConfig.getServerRegistryAddress(),
                springBootProviderConfig.getRegistryAddress(),
                springBootProviderConfig.getRegistryType(),
                springBootProviderConfig.getReflectType(),
                springBootProviderConfig.getRegistryLoadBalanceType(),
                springBootProviderConfig.getHeartbeatInterval(),
                springBootProviderConfig.getScanNotActiveChannelInterval(),
                springBootProviderConfig.isEnableResultCache(),
                springBootProviderConfig.getResultCacheExpire());
    }

}
