package io.lh.rpc.provider.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.lh.rpc.cache.res.CacheResKey;
import io.lh.rpc.cache.res.CacheResManager;
import io.lh.rpc.commom.helper.RpcServiceHelper;
import io.lh.rpc.commom.threadpool.ServerThreadPool;
import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.constants.RpcConstantsCache;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.enumeration.RpcStatus;
import io.lh.rpc.protocol.enumeration.RpcType;
import io.lh.rpc.protocol.header.RpcHeader;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import io.lh.rpc.reflect.api.ReflectInvoker;
import io.lh.rpc.spi.loader.ExtensionLoader;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 描述：服务提供者的消息处理工具类
 * SimpleChannelInboundHandler 是核心的类，基于netty封装。
 * 利用netty的tiger实现了定时心跳机制。
 *
 * @author ：lh 创建时间：2023/02/12
 * @methoduserEventTriggered 版本  ：1.0.0
 */
public class RpcServiceProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {


    /**
     * The Logger.
     */
    private final Logger LOGGER = LoggerFactory.getLogger(RpcServiceProviderHandler.class);

    /**
     * The Reflect invoker.
     */
    private final ReflectInvoker reflectInvoker;

    /**
     * The Hadler map.
     */
    private final Map<String, Object> hadlerMap;

    /**
     * The Enable res cache.
     */
    private final Boolean enableResCache;

    /**
     * The Cache res manager.
     */
    private final CacheResManager<RpcProtocol<RpcResponse>> cacheResManager;

    /**
     * Instantiates a new Rpc service provider handler.
     *
     * @param hadlerMap      the hadler map
     * @param reflectType    the reflect type
     * @param enableResCache the enable res cache
     * @param resCacheExpire the res cache expire
     */
    public RpcServiceProviderHandler(Map<String, Object> hadlerMap, String reflectType,
                                     boolean enableResCache, int resCacheExpire) {
        if (resCacheExpire <= 0) resCacheExpire = RpcConstantsCache.RPC_SCAN_RESULT_CACHE_EXPIRE;
        this.cacheResManager = CacheResManager.getInstance(resCacheExpire, enableResCache);
        this.enableResCache = enableResCache;
        this.hadlerMap = hadlerMap;
        this.reflectInvoker = ExtensionLoader.getExtension(ReflectInvoker.class, reflectType);
    }

    /**
     * Channel read 0.
     *
     * @param ctx      the ctx
     * @param protocol the protocol
     * @throws Exception the exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {


        LOGGER.warn("服务提供者收到的消息是，{}", JSONObject.toJSONString(protocol));

        for (Map.Entry<String, Object> entry : hadlerMap.entrySet()) {
            LOGGER.warn("handlerMap里存放的元素有以下，key:{},value:{}\n", entry.getKey(), entry.getValue());
        }

        ServerThreadPool.submit(()->{
            RpcProtocol<RpcResponse> responseRpcProtocol = handlerMessage(protocol, ctx.channel());

            ctx.writeAndFlush(responseRpcProtocol).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    LOGGER.warn("响应response，对应的请求时id{}", protocol.getHeader().getRequestId());
                }
            });

        });
    }

    /**
     * Handler message rpc protocol.
     *
     * @param protocol the protocol
     * @param channel  the channel
     * @return the rpc protocol
     */
    private RpcProtocol<RpcResponse> handlerMessage(RpcProtocol<RpcRequest> protocol, Channel channel) {

        RpcProtocol<RpcResponse> responseRpcProtocol = null;
        RpcHeader header = protocol.getHeader();
        // 心跳信息
        if (header.getMsgType() == (byte) RpcType.HEARTBEAT_TO_CONSUMER.getType()) {
            // 心跳
            LOGGER.warn("provider❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤心跳❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
            responseRpcProtocol = handlerHeartbeatMessage(protocol, header);
        } else if (header.getMsgType() == (byte) RpcType.REQUEST.getType()) {
            responseRpcProtocol = handlerRequestMessageWithCache(protocol, header);
        } else if (header.getMsgType() == (byte)
                RpcType.HEARTBEAT_TO_PROVIDER.getType()){
            handlerHeartbeatMessageToProvider(protocol, channel);
        }
        return responseRpcProtocol;
    }

    /**
     * Handler heartbeat message rpc protocol.
     *
     * @param protocol the protocol
     * @param header   the header
     * @return the rpc protocol
     */
    private RpcProtocol<RpcResponse> handlerHeartbeatMessage(RpcProtocol<RpcRequest> protocol, RpcHeader header) {
        header.setMsgType((byte) RpcType.HEARTBEAT_TO_CONSUMER.getType());
        RpcRequest request = protocol.getBody();
        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<RpcResponse>();
        RpcResponse response = new RpcResponse();
        response.setResult(RpcConstants.HEARTBEAT_PONG);
        response.setAsync(request.isAsync());
        response.setOneWay(request.isOneWay());
        header.setStatus((byte) RpcStatus.SUCCESS.getCode());
        responseRpcProtocol.setHeader(header);
        responseRpcProtocol.setBody(response);
        return responseRpcProtocol;
    }

    /**
     * Handler request message rpc protocol.
     *
     * @param protocol the protocol
     * @param header   the header
     * @return the rpc protocol
     */
    private RpcProtocol<RpcResponse> handlerRequestMessage(RpcProtocol<RpcRequest> protocol, RpcHeader header) {

        RpcRequest request = protocol.getBody();
        LOGGER.warn("收到请求id：{}", header.getRequestId());

        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<RpcResponse>();
        RpcResponse response = new RpcResponse();

        try {
            Object result = handle(request);
            response.setResult(result);
            response.setAsync(request.isAsync());
            response.setOneWay(request.isOneWay());
            header.setStatus((byte) RpcStatus.SUCCESS.getCode());
        } catch (Throwable e) {
            response.setError(e.toString());
            header.setStatus((byte) RpcStatus.FAIL.getCode());
            LOGGER.error("RPC server 处理 error{}", e);
        }

        responseRpcProtocol.setHeader(header);
        responseRpcProtocol.setBody(response);

        return responseRpcProtocol;
    }

    // 通过缓存处理生产者调用具体方法获取到结果数据：缓存
    private RpcProtocol<RpcResponse> handlerRequestMessageCache(RpcProtocol<RpcRequest> protocol, RpcHeader header) {
        RpcRequest request = protocol.getBody();

        CacheResKey cacheResKey = new CacheResKey(request.getClassName(),
                request.getMethodName(),
                request.getParameterTypes(),
                request.getParameters(),
                request.getVersion(),
                request.getGroup());

        RpcProtocol<RpcResponse> rpcResponseRpcProtocol = cacheResManager.get(cacheResKey);

        if(null == rpcResponseRpcProtocol) {
            rpcResponseRpcProtocol = handlerRequestMessage(protocol, header);
            // 保存时间
            cacheResKey.setCacheTimeStamp(System.currentTimeMillis());
            cacheResManager.put(cacheResKey, rpcResponseRpcProtocol);
        }

        rpcResponseRpcProtocol.setHeader(header);

        return rpcResponseRpcProtocol;
    }

    // 用来调用缓存还是非缓存获取结果的。
    private RpcProtocol<RpcResponse> handlerRequestMessageWithCache(RpcProtocol<RpcRequest> protocol, RpcHeader header) {
        header.setMsgType((byte) RpcType.RESPONSE.getType());

        if (enableResCache) return handlerRequestMessageCache(protocol, header);

        return handlerRequestMessage(protocol, header);
    }

    /**
     * Handle object.
     *
     * @param request the request
     * @return the object
     * @throws Throwable the throwable
     */
    private Object handle(RpcRequest request) throws Throwable {

        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(),
                request.getVersion(), request.getGroup());
        String methodName = request.getMethodName();

        // 获取对象
        Object serviceBean = hadlerMap.get(serviceKey);

        if (null == serviceBean) {
            throw new RuntimeException(String.format("服务不存在%s.%s", request.getClassName(), methodName));
        }

        // 获取class信息
        Class<?> serviceBeanClass = serviceBean.getClass();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // 打印记录参数类型(非常原生的方法)
        if (parameterTypes != null && parameterTypes.length > 0) {
            for (int m = 0; m < parameterTypes.length; m++) {
                LOGGER.info("参数类型{}", parameterTypes[m].getName());
            }
        }

        // 打印记录参数(非常原生的方法)
        if (parameters != null && parameters.length > 1) {
            for (int m = 0; m < parameters.length; m++) {
                LOGGER.info("参数{}", parameters[m]);
            }
        }

        // 调用方法 这里根据反射获取的实现类。动态代理的体现！
        return this.reflectInvoker.invokeMethod(serviceBean, serviceBeanClass, methodName,
                parameterTypes, parameters);
    }

    /**
     * Invoke jdk method object.
     *
     * @param serviceBean    the service bean
     * @param methodName     the method name
     * @param parameterTypes the parameter types
     * @param parameters     the parameters
     * @param serviceClass   the service class
     * @return the object
     * @throws Throwable the throwable
     */
    private Object invokeJdkMethod(Object serviceBean, String methodName,
                                   Class<?>[] parameterTypes, Object[] parameters,
                                   Class<?> serviceClass) throws Throwable {
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);

    }

    /**
     * 调用 cglib的方法
     *
     * @param serviceBean    the service bean
     * @param methodName     the method name
     * @param parameterTypes the parameter types
     * @param parameters     the parameters
     * @param serviceClass   the service class
     * @return object object
     * @throws Throwable the throwable
     */
    private Object invokeCglibMethod(Object serviceBean, String methodName,
                                     Class<?>[] parameterTypes, Object[] parameters,
                                     Class<?> serviceClass) throws Throwable {

        FastClass fastServiceClass = FastClass.create(serviceClass);
        FastMethod fastServiceClassMethod = fastServiceClass.getMethod(methodName, parameterTypes);
        return fastServiceClassMethod.invoke(serviceBean, parameters);

    }

    /**
     * Handler heartbeat message to provider.
     *
     * @param protocol the protocol
     * @param channel  the channel
     */
    private void handlerHeartbeatMessageToProvider(RpcProtocol<RpcRequest> protocol, Channel channel) {

        LOGGER.info("receive service consumer heartbeat message, the consumer is: {}, the heartbeat message is: {}",
        channel.remoteAddress(), protocol.getBody().getParameters()[0]);
    }


    /**
     * User event triggered.
     * 用来实现心跳检测的
     * @param ctx the ctx
     * @param evt the evt
     * @throws Exception the exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {

        if (evt instanceof IdleStateEvent){
            LOGGER.info("是IdleStateEvent事件");
            Channel channel = ctx.channel();
            try{
                LOGGER.info("IdleStateEvent triggered, close channel " +
                        channel);
                channel.close();
            }finally {
                channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
