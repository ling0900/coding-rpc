package io.lh.rpc.reflect.cglib;

import io.lh.rpc.reflect.api.ReflectInvoker;
import io.lh.rpc.spi.annotation.SPIClass;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Cglib reflect invoker.
 * @author lh
 */
@SPIClass
public class CglibReflectInvoker implements ReflectInvoker {

    private final Logger logger = LoggerFactory.getLogger(CglibReflectInvoker.class);

    @Override
    public Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        logger.warn("cglib spi 机制，服务提供者 反射调用真实方法 >>>");

        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);
        return fastMethod.invoke(serviceBean, parameters);
    }
}
