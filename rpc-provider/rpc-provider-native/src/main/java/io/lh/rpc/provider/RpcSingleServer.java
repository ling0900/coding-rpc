package io.lh.rpc.provider;

import io.lh.rpc.commom.scanner.server.RpcServiceProviderScanner;
import io.lh.rpc.provider.common.server.base.BaseServer;

/**
 * The type Rpc single server.
 * 单个服务RPC
 */
public class RpcSingleServer extends BaseServer {

    /**
     * Instantiates a new Rpc single server.
     *
     * @param serviceAddress the service address
     * @param scanPackage    the scan package
     */
    public RpcSingleServer(String serviceAddress, String scanPackage) {

        super(serviceAddress);

        try {
            this.handlerMap = RpcServiceProviderScanner.doScannerWithRpcReferenceAnnotationFilter(scanPackage);
        } catch (Exception e) {
            System.out.println("00000000000~~~~~~~~~~~~~");
        }
    }
}
