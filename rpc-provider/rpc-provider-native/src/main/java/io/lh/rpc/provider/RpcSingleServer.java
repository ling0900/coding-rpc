package io.lh.rpc.provider;

import io.lh.rpc.commom.scanner.server.RpcServiceProviderScanner;
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
    public RpcSingleServer(String serviceAddress, String scanPackage, String reflectType) {

        super(serviceAddress, reflectType);

        try {
            this.handlerMap = RpcServiceProviderScanner.doScannerWithRpcReferenceAnnotationFilter(scanPackage);
        } catch (Exception e) {
            LOGGER.info("RpcSingleServer异常{}", e);
        }
    }
}
