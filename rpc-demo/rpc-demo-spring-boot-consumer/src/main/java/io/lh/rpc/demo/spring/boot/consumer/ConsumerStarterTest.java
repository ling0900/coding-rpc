package io.lh.rpc.demo.spring.boot.consumer;

import io.lh.rpc.demo.spring.boot.consumer.service.DemoConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"io.lh.rpc.*"})
public class ConsumerStarterTest {
    public static void main(String[] args) {
        ConfigurableApplicationContext context
                = SpringApplication.run(ConsumerStarterTest.class, args);

        DemoConsumer consumerDemoService = context.getBean(DemoConsumer.class);
        String result = consumerDemoService.consumer("****--**--**-**-**-**-**");

        System.out.println(result);

    }
}
