package io.lh.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 描述：rpc服务提供者注解
 * 版本：1.0.0
 * 作者：lh
 * 创建时间：2023/02/11
 */
@SuppressWarnings("ALL")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcServiceProvider {


    /**
     * Interface class class.
     *
     * @return the class
     */
// 服务的class全路径
    Class<?> interfaceClass() default void.class;

    /**
     * Interface class name string.
     *
     * @return the string
     */
// 服务名
    String interfaceClassName() default "";

    /**
     * Version string.
     *
     * @return the string
     */
// 服务版本号
    String version() default "1.0.0";

    /**
     * Ip address string.
     *
     * @return the string
     */
// 服务ip
    String ipAddress() default "";

    /**
     * Port string.
     *
     * @return the string
     */
// 服务端口
    int port() default 0;

    /**
     * Group string.
     *
     * @return the string
     */
// 服务分组
    String group() default "";

    /**
     * 权重
     */
    int weight() default 0;

    /**
     * 心跳间隔时间，默认30秒
     */
    int heartbeatInterval() default 30000;

    /**
     * 扫描空闲连接间隔时间，默认60秒
     */
    int scanNotActiveChannelInterval() default 60000;

}
