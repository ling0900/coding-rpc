package io.lh.rpc.test.spi;

import io.lh.rpc.test.spi.service.SPIService;
import loader.ExtensionLoader;
import org.junit.Test;

public class SPITest {
    @Test
    public void testSPILoader() {
        SPIService service = ExtensionLoader.getExtension(SPIService.class, "spiService");
        String sometring = service.hello("9");
        System.out.println(sometring);
    }
}
