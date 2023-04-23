package io.lh.rpc.reflect.api;

import io.lh.rpc.spi.annotation.SPI;

/**
 * The interface Reflect invoker.
 * @author lh
 */
@SPI
public interface ReflectInvoker {
    /**
     * Invoke method object.
     * 调用真是的方法，服务提供者使用的 SPI 接口
     * @param serviceBean the service bean 方法所在对象实例
     * @param serviceClass the service class 对象实例的class对象
     * @param methodName the method name 方法的名称
     * @param parameterTypes the parameter types
     * @param parameters the parameters
     * @return the object 结果
     * @throws Throwable the throwable
     */
Object invokeMethod(Object serviceBean, Class<?> serviceClass,
                        String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable;
}
