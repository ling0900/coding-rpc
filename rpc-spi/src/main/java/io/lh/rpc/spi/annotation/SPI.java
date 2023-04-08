package io.lh.rpc.spi.annotation;

import java.lang.annotation.*;

/**
 * The interface Spi.
 * 注解
 * 这个注解，用来标注到加入spi机制的接口上。
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {
    /**
     * Value string.
     *
     * @return the string
     */
    String value() default "";
}
