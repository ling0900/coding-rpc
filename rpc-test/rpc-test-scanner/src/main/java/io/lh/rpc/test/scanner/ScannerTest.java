package io.lh.rpc.test.scanner;

import io.lh.rpc.commom.scanner.ClassScanner;
import io.lh.rpc.commom.scanner.server.RpcServiceProviderScanner;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The type Scanner test.
 * @author lh
 */
public class ScannerTest {

    /**
     * Test scanner class name list.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testScannerClassNameList() throws IOException {
        List<String> classNameList = ClassScanner.getClassNameList("io.lh.rpc.test.scanner");
        classNameList.stream().forEach((c)->{
            System.out.println(c);
        });
    }

    /**
     * Test scanner class name list by rpc service.
     */
    @Test
    public void testScannerClassNameListByRpcService() {
        Map<String, Object> stringObjectsMap = RpcServiceProviderScanner.doScannerWithRpcReferenceAnnotationFilter("io.lh.rpc.test.scanner");


    }

}
