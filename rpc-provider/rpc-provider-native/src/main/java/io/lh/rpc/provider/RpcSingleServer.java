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
    public RpcSingleServer(String serviceAddress, String scanPackage, String reflectType,
                           String registryAddress, String registryType,
                           String registryLoadBalanceType, int heartbeatInterval, int
                                   scanNotActiveChannelInterval) {

        super(serviceAddress, registryAddress, registryType, reflectType, registryLoadBalanceType,
                heartbeatInterval, scanNotActiveChannelInterval);

        try {
            this.handlerMap = RpcServiceProviderScanner.
                    doScannerWithRpcServiceAnnotationFilterAndRegistryService(this.host, this.port, scanPackage, registryService);

            LOGGER.info("所有扫描到的服务有：\n{}", JSON.toJSONString(handlerMap));

        } catch (Exception e) {
            LOGGER.info("RpcSingleServer异常{}", e);
        }
    }
}
