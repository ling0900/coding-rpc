package io.lh.rpc.demo.spring.xml.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringXmlProviderStarter {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("server.xml");
    }
}
