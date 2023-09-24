package io.lh.rpc.demo.spring.xml.consumer;

import io.lh.rpc.consumer.RpcClient;
import io.lh.rpc.demo.api.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * The type Spring xml consumer test.
 * <p>
 * @RunWith注解: SpringXmlConsumerTest类基于Spring的测试模块启动。@ContextConfiguration注解，locations属性设置为对应的配置文件，
 * 当类启动时，Spring会读取classpath路径下的配置⽂件创建对应的bean放入IOC容器。
 * </p>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@ContextConfiguration(locations = "classpath:cs-spring.xml")
public class SpringXmlConsumerTest {


    /**
     * The Rpc client.
     */
    @Autowired
    private RpcClient rpcClient;

    /**
     * Test interface rpc.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void testRpc() throws InterruptedException {
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.helloDemo("demoSpringConsumer");
        log.info("返回===>>> " + result);
        //rpcClient.shutdown();
        while (true){
            Thread.sleep(1000);
        }
    }
}
