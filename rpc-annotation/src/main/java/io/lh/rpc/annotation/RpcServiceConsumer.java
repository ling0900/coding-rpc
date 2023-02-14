package io.lh.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 描述：rpc服务消费者注解
 * 版本：1.0.0
 * 作者：lh
 * 创建时间：2023/02/11
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcServiceConsumer {

    /**
     * 服务的class全路径
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 注册中心的类型:zookeeper nacos etcd consul
     */
    String registryType() default "zookeeper";

    /**
     * 注册中心的地址
     */
    String registryAddress() default "127.0,0.1:2111";

    /**
     * 负载均衡类型
     */
    String loadBalanceType() default "zkhash";

    /**
     * 序列化类型:protostuff kryo json jdk hessian2 fst
     */
    String serializaitonType() default "protostuff";

    /**
     * 超时时间
     */
    long timeout() default 5000;

    /**
     * 代理方式 jdk javassist cglib
     */
    String proxy() default "jdk";

    // 是否同步调用

    /**
     * 是否异步调用
     */
    boolean async() default false;

    // 是否回调 调用

    /**
     * 是否单向调用
     */
    boolean oneway() default false;

    // 注册中心的类型
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
    // todo 注解有tostring吗？
}
