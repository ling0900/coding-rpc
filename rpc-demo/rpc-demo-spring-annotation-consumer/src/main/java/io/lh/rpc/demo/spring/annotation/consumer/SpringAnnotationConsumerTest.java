package io.lh.rpc.demo.spring.annotation.consumer;

import io.lh.rpc.demo.spring.annotation.consumer.config.SpringAnnotationConsumerConfig;
import io.lh.rpc.demo.spring.annotation.consumer.service.ConsumerDemoService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringAnnotationConsumerTest {

    @Test
    public void test() {
        AnnotationConfigApplicationContext an = new AnnotationConfigApplicationContext(SpringAnnotationConsumerConfig.class);

        ConsumerDemoService bean = an.getBean(ConsumerDemoService.class);

        bean.consumerHello("0000SpringAnnotationConsumerTest000");
    }
}
