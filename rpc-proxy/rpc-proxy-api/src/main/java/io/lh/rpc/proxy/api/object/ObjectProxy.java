package io.lh.rpc.proxy.api.object;

import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.header.RpcHeaderFactory;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.proxy.api.async.IAsyncObjectProxy;
import io.lh.rpc.proxy.api.consumer.Consumer;
import io.lh.rpc.proxy.api.future.RpcFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * <p>描述：</p>
 * <p>版本：1.0.0</p>
 * <p>@author：lh</p>
 * <p>创建时间：2023/02/21</p>
 */
public class ObjectProxy<T> implements IAsyncObjectProxy, InvocationHandler {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectProxy.class);

    /**
     *
     */
    private Class<T> clazz;

    /**
     *
     */
    private String serviceVersion;

    /**
     *
     */
    private String serviceGroup;

    /**
     *
     */
    private long timeout = 15000;

    /**
     *
     */
    private Consumer consumer;

    /**
     *
     */
    private String serializationType;

    /**
     *
     */
    private boolean async;

    /**
     *
     */
    private boolean oneway;

    /**
     * @param clazz
     * @param serviceVersion
     * @param serviceGroup
     * @param timeout
     * @param consumer
     * @param serializationType
     * @param async
     * @param oneway
     */
    public ObjectProxy(Class<T> clazz, String serviceVersion, String serviceGroup, long timeout, Consumer consumer, String serializationType, boolean async, boolean oneway) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    /**
     * @param clazz
     * @param serviceVersion
     * @param serviceGroup
     * @param serializationType
     * @param timeout
     * @param consumer
     * @param async
     * @param oneway
     */
    public ObjectProxy(Class<T> clazz, String serviceVersion, String serviceGroup, String serializationType, long timeout, Consumer consumer, boolean async, boolean oneway) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    /**
     * @return {@link Class}<{@link T}>
     */
    public Class<T> getClazz() {
        return clazz;
    }

    /**
     * @param clazz
     */
    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * @return {@link String}
     */
    public String getServiceVersion() {
        return serviceVersion;
    }

    /**
     * @param serviceVersion
     */
    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    /**
     * @return {@link String}
     */
    public String getServiceGroup() {
        return serviceGroup;
    }

    /**
     * @param serviceGroup
     */
    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    /**
     * @return long
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * @return {@link Consumer}
     */
    public Consumer getConsumer() {
        return consumer;
    }

    /**
     * @param consumer
     */
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    /**
     * @return {@link String}
     */
    public String getSerializationType() {
        return serializationType;
    }

    /**
     * @param serializationType
     */
    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    /**
     * @return boolean
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * @param async
     */
    public void setAsync(boolean async) {
        this.async = async;
    }

    /**
     * @return boolean
     */
    public boolean isOneway() {
        return oneway;
    }

    /**
     * @param oneway
     */
    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    /**
     * @param proxy
     * @param method
     * @param args
     * @return {@link Object}
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 这里非常像 spring里面的源代码
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) + ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }

        RpcProtocol<RpcRequest> rpcRequestRpcProtocol = new RpcProtocol<>();
        rpcRequestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType));
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setVersion(this.serviceVersion);
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setGroup(this.serviceGroup);
        rpcRequest.setParameters(args);
        rpcRequest.setAsync(async);
        rpcRequest.setOneWay(oneway);
        rpcRequestRpcProtocol.setBody(rpcRequest);

        LOGGER.debug(method.getName() + "vs" + method.getDeclaringClass().getName());

        if (method.getParameterTypes() != null && method.getParameterTypes().length > 0) {
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                LOGGER.debug(method.getParameterTypes()[i].getName());
            }
        }

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                LOGGER.debug(args[i].toString());
            }
        }

        RpcFuture rpcFuture = this.consumer.sendRequest(rpcRequestRpcProtocol);

        return rpcFuture == null ? null : timeout > 0 ? rpcFuture.get(timeout, TimeUnit.MILLISECONDS) : rpcFuture.get();
    }

    @Override
    public RpcFuture call(String funcName, Object... args) {
        RpcProtocol<RpcRequest> request = createRequest(this.clazz.getName(), funcName, args);
        RpcFuture rpcFuture = null;
        try {
            rpcFuture = this.consumer.sendRequest(request);
        } catch (Exception e) {
            LOGGER.error("async all throws exception:{}", e);
        }
        return rpcFuture;
    }

    /**
     * 封装一个请求协议对象
     * @param className
     * @param methodName
     * @param args
     * @return
     */
    private RpcProtocol<RpcRequest> createRequest(String className, String methodName, Object[] args) {

        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<RpcRequest>();
        // 设置header
        requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType));

        // 封装request，其实就是 body
        RpcRequest request = new RpcRequest();
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameters(args);
        request.setVersion(this.serviceVersion);
        request.setGroup(this.serviceGroup);

        Class[] parameterTypes = new Class[args.length];
        // 遍历
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }
        request.setParameterTypes(parameterTypes);
        requestRpcProtocol.setBody(request);

        LOGGER.debug("className{}", className);
        LOGGER.debug("methodName{}", methodName);

        for (int i = 0; i < parameterTypes.length; ++i) {
            LOGGER.debug(parameterTypes[i].getName());
        }

        for (int i = 0; i < args.length; ++i) {
            LOGGER.debug("参数的长度是{}",args[i].toString());
        }

        return requestRpcProtocol;
    }

    /**
     * 根据class所在的包，获取类型。
     * @param obj
     * @return
     */
    private Class<?> getClassType(Object obj){

        Class<?> clazz = obj.getClass();
        // 其实就是类型，包名
        String clazzName = clazz.getName();

        switch (clazzName){
            case "java.lang.Float":
                return Float.TYPE;
            case "java.lang.Double":
                return Double.TYPE;
            case "java.lang.Long":
                return Long.TYPE;
            case "java.lang.Byte":
                return Byte.TYPE;
            case "java.lang.Character":
                return Character.TYPE;
            case "java.lang.Integer":
                return Integer.TYPE;
            case "java.lang.Short":
                return Short.TYPE;
            case "java.lang.Boolean":
                return Boolean.TYPE;
        }
        return clazz;
    }

}
