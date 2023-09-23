package io.lh.rpc.demo.spring.annotation.provide;

import io.lh.rpc.demo.spring.annotation.provide.config.SpringAnnotationProviderConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringAnnotationProviderStarter {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(SpringAnnotationProviderConfig.class);
    }
}
