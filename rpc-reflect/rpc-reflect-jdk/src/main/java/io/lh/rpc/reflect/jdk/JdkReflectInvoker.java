package io.lh.rpc.reflect.jdk;

import io.lh.rpc.reflect.api.ReflectInvoker;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * The type Jdk reflect invoker.
 * @author lh
 */
@SPIClass
public class JdkReflectInvoker implements ReflectInvoker {

    private final Logger logger = LoggerFactory.getLogger(JdkReflectInvoker.class);
    @Override
    public Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName,
                               Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        logger.warn("使用 jdk 反射调用服务提供者的真实方法>>>>>>");
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);

        return method.invoke(serviceBean, parameters);
    }
}
