package io.lh.rpc.spi.annotation;

import java.lang.annotation.*;

/**
 * The interface Spi class.
 * 主要用在标注到加入SPI接口的实现类上的
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPIClass {
}
