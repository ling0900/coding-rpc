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
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcServiceProvider {


    // 服务的class全路径
    Class<?> interfaceClass() default void.class;
    // 服务名
    String interfaceClassName() default "";
    // 服务版本号
    String version() default "1.0.0";
    // 服务ip
    String ipAddress() default "";
    // 服务端口
    String port() default "";
    // 服务分组
    String group() default "";
}
