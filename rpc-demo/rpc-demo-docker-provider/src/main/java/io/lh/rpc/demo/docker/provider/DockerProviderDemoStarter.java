package io.lh.rpc.demo.docker.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The type Docker provider demo starter.
 * 框架接入docker服务提供者，启动类。
 * @author Ling
 */
@SpringBootApplication
@ComponentScan(value = {"io.lh.rpc"})
public class DockerProviderDemoStarter {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DockerProviderDemoStarter.class);
    }
}
