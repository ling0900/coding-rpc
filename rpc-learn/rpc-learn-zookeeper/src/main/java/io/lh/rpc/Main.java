package io.lh.rpc;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class Main {

    public static void main(String[] args) {
        Logger loggerFactory = LoggerFactory.getLogger(Main.class);

        log.info("00");

        loggerFactory.info("99");

        System.out.println("Hello world!");
    }

}