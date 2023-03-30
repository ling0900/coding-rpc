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

    private final String reflectType;

    private final Map<String, Object> hadlerMap;

    /**
     * Instantiates a new Rpc service provider handler.
     *
     * @param hadlerMap   the hadler map
     * @param reflectType the reflect type
     */
    public RpcServiceProviderHandler(Map<String, Object> hadlerMap, String reflectType) {
        this.hadlerMap = hadlerMap;
        this.reflectType = reflectType;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {


        LOGGER.info("服务提供者收到的消息是，{}", JSONObject.toJSONString(protocol));

        for (Map.Entry<String, Object> entry : hadlerMap.entrySet()) {
            LOGGER.info("handlerMap里存放的元素有以下，key:{},value:{}\n", entry.getKey(), entry.getValue());
        }

        ServerThreadPool.submit(()->{
            RpcHeader header = protocol.getHeader();
            header.setMsgType((byte) RpcType.RESPONSE.getType());
            header.setStatus((byte) RpcStatus.SUCCESS.getCode());
            RpcRequest request = protocol.getBody();
            RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
            RpcResponse response = new RpcResponse();
            try {
                Object result = handle(request);
                response.setResult(result);
                response.setAsync(request.isAsync());
                response.setOneWay(request.isOneWay());
            } catch (Throwable e) {
                LOGGER.error("服务者消息解析异常,{}",e);
                response.setError(e.toString());
                header.setStatus((byte) RpcStatus.FAIL.getCode());
            }
            responseRpcProtocol.setHeader(header);
            responseRpcProtocol.setBody(response);
            ctx.writeAndFlush(responseRpcProtocol).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    LOGGER.info("ctx.writeAndFlush==");
                }
            });

        });
    }

    /**
     * Handle object.
     *
     * @param request the request
     * @return the object
     * @throws Throwable the throwable
     */
    private Object handle(RpcRequest request) throws Throwable {

        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getVersion(), request.getGroup());
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
        return invokeMethod(serviceBean, methodName, parameterTypes, parameters, serviceBeanClass);
    }

    /**
     * 异常，向上throws，让调用者去处理。
     */
    private Object invokeMethod(Object serviceBean, String methodName,
                                Class<?>[] parameterTypes, Object[] parameters, Class<?> serviceClass) throws Throwable {

        LOGGER.info("调用目标方法用到的反射类型是{}", this.reflectType);

        switch (this.reflectType) {
            case RpcConstants.REFLECT_TYPE_JDK:
                LOGGER.info("进入了 jdk 反射");
                return this.invokeJdkMethod(serviceBean, methodName, parameterTypes, parameters, serviceClass);
            case RpcConstants.REFLECT_TYPE_CGLIB:
                LOGGER.info("进入了 cglib 反射");
                return this.invokeCglibMethod(serviceBean, methodName, parameterTypes, parameters, serviceClass);
            default:
                throw new IllegalArgumentException(String.format("找不到匹配的反射类型%s", this.reflectType));
        }

    }

    private Object invokeJdkMethod(Object serviceBean, String methodName,
                                   Class<?>[] parameterTypes, Object[] parameters, Class<?> serviceClass) throws Throwable {
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
                                     Class<?>[] parameterTypes, Object[] parameters, Class<?> serviceClass) throws Throwable {

        FastClass fastServiceClass = FastClass.create(serviceClass);
        FastMethod fastServiceClassMethod = fastServiceClass.getMethod(methodName, parameterTypes);
        return fastServiceClassMethod.invoke(serviceBean, parameters);

    }
}
