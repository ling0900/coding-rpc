package io.lh.rpc.provider.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.lh.rpc.commom.helper.RpcServiceHelper;
import io.lh.rpc.commom.threadpool.ServerThreadPool;
import io.lh.rpc.constants.RpcConstants;
import io.lh.rpc.protocol.RpcProtocol;
import io.lh.rpc.protocol.enumeration.RpcStatus;
import io.lh.rpc.protocol.enumeration.RpcType;
import io.lh.rpc.protocol.header.RpcHeader;
import io.lh.rpc.protocol.request.RpcRequest;
import io.lh.rpc.protocol.response.RpcResponse;
import io.lh.rpc.reflect.api.ReflectInvoker;
import io.lh.rpc.spi.loader.ExtensionLoader;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 描述：服务提供者的消息处理工具类
 * SimpleChannelInboundHandler 是核心的类，基于netty封装。
 * 版本：1.0.0
 *
 * @author ：lh 创建时间：2023/02/12
 */
public class RpcServiceProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {


    private final Logger LOGGER = LoggerFactory.getLogger(RpcServiceProviderHandler.class);

    private final ReflectInvoker reflectInvoker;

    private final Map<String, Object> hadlerMap;

    /**
     * Instantiates a new Rpc service provider handler.
     *
     * @param hadlerMap   the hadler map
     * @param reflectType the reflect type
     */
    public RpcServiceProviderHandler(Map<String, Object> hadlerMap, String reflectType) {
        this.hadlerMap = hadlerMap;
        this.reflectInvoker = ExtensionLoader.getExtension(ReflectInvoker.class, reflectType);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {


        LOGGER.warn("服务提供者收到的消息是，{}", JSONObject.toJSONString(protocol));

        for (Map.Entry<String, Object> entry : hadlerMap.entrySet()) {
            LOGGER.warn("handlerMap里存放的元素有以下，key:{},value:{}\n", entry.getKey(), entry.getValue());
        }

        ServerThreadPool.submit(()->{
            RpcProtocol<RpcResponse> responseRpcProtocol = handlerMessage(protocol);

            ctx.writeAndFlush(responseRpcProtocol).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    LOGGER.warn("响应response，对应的请求时id{}", protocol.getHeader().getRequestId());
                }
            });

        });
    }

    private RpcProtocol<RpcResponse> handlerMessage(RpcProtocol<RpcRequest> protocol) {
        RpcProtocol<RpcResponse> responseRpcProtocol = null;
        RpcHeader header = protocol.getHeader();
        // 心跳信息
        if (header.getMsgType() == (byte) RpcType.HEARTBEAT_TO_CONSUMER.getType()) {
            // 心跳
            LOGGER.warn("provider❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤心跳❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
            responseRpcProtocol = handlerHeartbeatMessage(protocol, header);
        } else if (header.getMsgType() == (byte) RpcType.REQUEST.getType()) {
            responseRpcProtocol = handlerRequestMessage(protocol, header);
        }
        return responseRpcProtocol;
    }

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

    private RpcProtocol<RpcResponse> handlerRequestMessage(RpcProtocol<RpcRequest> protocol, RpcHeader header) {
        header.setMsgType((byte) RpcType.RESPONSE.getType());
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

    private Object invokeJdkMethod(Object serviceBean, String methodName,
                                   Class<?>[] parameterTypes, Object[] parameters,
                                   Class<?> serviceClass) throws Throwable {
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);

    }

    /**
     * 调用 cglib的方法
     * @param serviceBean
     * @param methodName
     * @param parameterTypes
     * @param parameters
     * @param serviceClass
     * @return
     * @throws Throwable
     */
    private Object invokeCglibMethod(Object serviceBean, String methodName,
                                     Class<?>[] parameterTypes, Object[] parameters,
                                     Class<?> serviceClass) throws Throwable {

        FastClass fastServiceClass = FastClass.create(serviceClass);
        FastMethod fastServiceClassMethod = fastServiceClass.getMethod(methodName, parameterTypes);
        return fastServiceClassMethod.invoke(serviceBean, parameters);

    }


}
