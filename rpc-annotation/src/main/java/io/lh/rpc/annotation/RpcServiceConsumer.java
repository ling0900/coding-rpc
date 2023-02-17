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
     *
     * @return the class
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 注册中心的类型:zookeeper nacos etcd consul
     *
     * @return the string
     */
    String registryType() default "zookeeper";

    /**
     * 注册中心的地址
     *
     * @return the string
     */
    String registryAddress() default "127.0,0.1:2111";

    /**
     * 负载均衡类型
     *
     * @return the string
     */
    String loadBalanceType() default "zkhash";

    /**
     * 序列化类型:protostuff kryo json jdk hessian2 fst
     *
     * @return the string
     */
    String serializaitonType() default "protostuff";

    /**
     * 超时时间
     *
     * @return the long
     */
    long timeout() default 5000;

    /**
     * 代理方式 jdk javassist cglib
     *
     * @return the string
     */
    String proxy() default "jdk";

    // 是否同步调用

    /**
     * 是否异步调用
     *
     * @return the boolean
     */
    boolean async() default false;

    // 是否回调 调用

    /**
     * 是否单向调用
     *
     * @return the boolean
     */
    boolean oneway() default false;

    /**
     * Interface class name string.
     *
     * @return the string
     */
// 注册中心的类型
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
    String port() default "";

    /**
     * Group string.
     *
     * @return the string
     */
// 服务分组
    String group() default "";
    // todo 注解有tostring吗？
}
