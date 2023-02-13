package io.lh.rpc.provider;

import io.lh.rpc.commom.scanner.server.RpcServiceProviderScanner;
import io.lh.rpc.provider.common.server.base.BaseServer;

public class RpcSingleServer extends BaseServer {
    public RpcSingleServer(String serviceAddress, String scanPackage) {

        super(serviceAddress);

        try {
            this.handlerMap = RpcServiceProviderScanner.doScannerWithRpcReferenceAnnotationFilter(scanPackage);
        } catch (Exception e) {
            System.out.println("00000000000~~~~~~~~~~~~~");
        }
    }
}
