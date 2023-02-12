package io.lh.rpc.test.scanner;

import io.lh.rpc.commom.scanner.ClassScanner;
import io.lh.rpc.commom.scanner.server.RpcServiceProviderScanner;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScannerTest {

    @Test
    public void testScannerClassNameList() throws IOException {
        List<String> classNameList = ClassScanner.getClassNameList("io.lh.rpc.test.scanner");
        classNameList.stream().forEach((c)->{
            System.out.println(c);
        });
    }

    @Test
    public void testScannerClassNameListByRpcService() {
        Map<String, Objects> stringObjectsMap = RpcServiceProviderScanner.doScannerWithRpcReferenceAnnotationFilter("io.lh.rpc.test.scanner");


    }

}
