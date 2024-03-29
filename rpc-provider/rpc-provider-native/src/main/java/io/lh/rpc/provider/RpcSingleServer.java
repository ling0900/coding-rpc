package io.lh.rpc.provider;

import com.alibaba.fastjson.JSON;
import io.lh.rpc.provider.common.scanner.RpcServiceProviderScanner;
import io.lh.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Rpc single server.
 * 单个服务RPC
 * @author lh
 */
public class RpcSingleServer extends BaseServer {

    private final Logger LOGGER = LoggerFactory.getLogger(RpcSingleServer.class);

    /**
     * Instantiates a new Rpc single server.
     *
     * @param serviceAddress the service address
     * @param scanPackage    the scan package
     */
    public RpcSingleServer(String serviceAddress, String serverRegistryAddress,
                           String scanPackage, String reflectType,
                           String registryAddress, String registryType,
                           String registryLoadBalanceType, int heartbeatInterval,
                           int scanNotActiveChannelInterval,
                           boolean enableResCache, int resCacheExpire) {

        super(serviceAddress, serverRegistryAddress,
                registryAddress, registryType,
                reflectType, registryLoadBalanceType,
                heartbeatInterval, scanNotActiveChannelInterval,
                enableResCache, resCacheExpire);

        try {
            this.handlerMap = RpcServiceProviderScanner.
                    doScannerWithRpcServiceAnnotationFilterAndRegistryService(this.serverRegistryHost, this.serverRegistryPort, scanPackage, registryService);

            LOGGER.info("所有扫描到的服务有：\n{}", JSON.toJSONString(handlerMap));

        } catch (Exception e) {
            LOGGER.info("RpcSingleServer异常{}", e);
        }
    }
}
