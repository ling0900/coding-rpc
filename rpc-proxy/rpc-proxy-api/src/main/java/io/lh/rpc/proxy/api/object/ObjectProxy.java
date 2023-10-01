package io.lh.rpc.proxy.api.object;

import io.lh.rpc.cache.res.CacheResKey;
import io.lh.rpc.cache.res.CacheResManager;
import io.lh.rpc.constants.RpcConstantsCache;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.enumeration.RpcType;
import io.lh.rpc.protocol.header.RpcHeaderFactory;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.proxy.api.async.IAsyncObjectProxy;
import io.lh.rpc.proxy.api.consumer.Consumer;
import io.lh.rpc.proxy.api.future.RpcFuture;
import io.lh.rpc.registry.api.RegistryService;
import lombok.Data;
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
@Data
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
    private long timeout = 900000;

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

    private RegistryService registryService;

    private boolean enableResultCache;

    /**
     * 内部会启动一个定时任务扫描过期的缓存数据。
     */
    private CacheResManager<Object> cacheResManager;

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
    public ObjectProxy(Class<T> clazz, String serviceVersion,
                       String serviceGroup, String serializationType,
                       long timeout, Consumer consumer, boolean async,
                       boolean oneway, RegistryService registryService,
                       boolean enableResultCache, int resCacheExpire) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.registryService = registryService;
        this.enableResultCache = enableResultCache;
        if (resCacheExpire <= 0) resCacheExpire = RpcConstantsCache.RPC_SCAN_RESULT_CACHE_EXPIRE;
        this.cacheResManager = CacheResManager.getInstance(resCacheExpire, enableResultCache);
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

        // 开启缓存则直接取缓存
        if (enableResultCache) return invokeSendRequestMethodCache(method, args);
        return invokeSendRequestMethod(method, args);
    }

    @Override
    public RpcFuture call(String funcName, Object... args) {
        RpcProtocol<RpcRequest> request = createRequest(this.clazz.getName(), funcName, args);
        RpcFuture rpcFuture = null;
        try {
            rpcFuture = this.consumer.sendRequest(request, registryService);
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

        LOGGER.debug("请求==》className{}", className);
        LOGGER.debug("请求==》methodName{}", methodName);

        for (int i = 0; i < parameterTypes.length; ++i) {
            LOGGER.debug(parameterTypes[i].getName());
        }

        for (int i = 0; i < args.length; ++i) {
            LOGGER.debug("参数的内容是{}",args[i].toString());
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

    // 调用生产者的远程方法。
    private Object invokeSendRequestMethod(Method method, Object[] args) throws
            Exception {

        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<RpcRequest>();
        requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType,
                RpcType.REQUEST.getType()));
        RpcRequest request = new RpcRequest();
        request.setVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setGroup(this.serviceGroup);
        request.setParameters(args);
        request.setAsync(async);
        request.setOneWay(oneway);
        requestRpcProtocol.setBody(request);

        LOGGER.debug(method.getDeclaringClass().getName());
        LOGGER.debug(method.getName());

        if (method.getParameterTypes() != null && method.getParameterTypes().length >
                0) {
            for (int i = 0; i < method.getParameterTypes().length; ++i) {
                LOGGER.debug(method.getParameterTypes()[i].getName());
            }
        }

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                LOGGER.debug(args[i].toString());
            }
        }

        RpcFuture rpcFuture = this.consumer.sendRequest(requestRpcProtocol,
                registryService);

        return rpcFuture == null
                ? null
                : timeout > 0
                    ? rpcFuture.get(timeout, TimeUnit.MILLISECONDS)
                    : rpcFuture.get();
    }

    private Object invokeSendRequestMethodCache(Method method, Object[] args) throws
            Exception {

        // 开启缓存，则处理缓存
        CacheResKey cacheResultKey = new
                CacheResKey(method.getDeclaringClass().getName(), method.getName(),
                method.getParameterTypes(), args, serviceVersion, serviceGroup);
        Object obj = this.cacheResManager.get(cacheResultKey);
        if (obj == null) {
            obj = invokeSendRequestMethod(method, args);
            if (obj != null) {
                cacheResultKey.setCacheTimeStamp(System.currentTimeMillis());
                this.cacheResManager.put(cacheResultKey, obj);
            }
        }
        return obj;
    }
}
