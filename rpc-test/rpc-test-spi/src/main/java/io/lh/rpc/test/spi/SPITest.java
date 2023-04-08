package io.lh.rpc.test.spi;

import io.lh.rpc.test.spi.service.SPIService;
import io.lh.rpc.spi.loader.ExtensionLoader;
import org.junit.Test;

/**
 * The type Spi test.
 */
public class SPITest {
    /**
     * Test spi loader.
     * spiService=io.lh.rpc.test.spi.service.impl.SPIServiceImpl
     * name可以随便写；主要是value要写对
     *
     */
    @Test
    public void testSPILoader() {
        // 只需要传入一个指定的接口，然后就可以了。
        SPIService service = ExtensionLoader.getExtension(SPIService.class, "spiService");
        String sometring = service.hello("9");
        System.out.println(sometring);
    }
}
