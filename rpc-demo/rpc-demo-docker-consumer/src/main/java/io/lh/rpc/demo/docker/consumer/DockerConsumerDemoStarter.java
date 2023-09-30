package io.lh.rpc.demo.docker.consumer;

import io.lh.rpc.demo.docker.consumer.service.IDockerConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"io.lh.rpc"})
@Slf4j
public class DockerConsumerDemoStarter {
    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(DockerConsumerDemoStarter.class, args);
        IDockerConsumer bean = run.getBean(IDockerConsumer.class);

        String s = bean.helloDocker("hi 消费者 docker");

        log.info("调试的结果是：=====>{}", s);
    }

}
