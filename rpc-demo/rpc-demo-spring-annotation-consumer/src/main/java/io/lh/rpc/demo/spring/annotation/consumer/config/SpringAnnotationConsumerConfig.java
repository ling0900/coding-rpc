package io.lh.rpc.demo.spring.annotation.consumer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The type Spring annotation consumer config.
 *
 * @author lh
 */
@Configuration
@ComponentScan(value = {"io.lh.rpc.*"})
public class SpringAnnotationConsumerConfig {
}
