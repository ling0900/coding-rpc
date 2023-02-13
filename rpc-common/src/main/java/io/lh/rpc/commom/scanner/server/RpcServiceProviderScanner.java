package io.lh.rpc.commom.scanner.server;

import io.lh.rpc.annotation.RpcServiceConsumer;
import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.commom.scanner.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 描述：注解扫描器
 * 版本：1.0.0
 * 作者：lh
 * 创建时间：2023/02/11
 */
public class RpcServiceProviderScanner extends ClassScanner {


    // todo LOGGER原理是什么？
    // private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceProviderScanner.class);

    public static Map<String, Object> doScannerWithRpcReferenceAnnotationFilter(String scanPackage) {

        Map<String, Object> handlerMap = new HashMap<>();

        try {

            List<String> classNameList = getClassNameList(scanPackage);

            if (classNameList == null || classNameList.isEmpty()) return handlerMap;

            List<Field[]> list = new ArrayList<>();

            // getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。
            // getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。

            // 判断服务提供者的注解
            for (String className : classNameList) {

                // 判断类上用到了服务提供者的注解
                Class<?> aClass = Class.forName(className);
                RpcServiceProvider annotation = aClass.getAnnotation(RpcServiceProvider.class);
                if (annotation != null) {
                    System.out.println("标注了@RpcReference注解的字段名称===>>>" + aClass.getName());
                    String name = aClass.getName();
                    // 这里需要是sevicename！
                    name = getServiceName(annotation);
                    String key = name.concat(annotation.version()).concat(annotation.group());
                    handlerMap.put(key, aClass.newInstance());
                }

                // 用来检查类内部有没有使用某个注解的
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field declaedField : declaredFields) {
                    RpcServiceConsumer rpcServiceProvider = declaedField.getAnnotation(RpcServiceConsumer.class);
                    if (rpcServiceProvider != null) { // 用到了注解
                        System.out.println("标注了@RpcServiceConsumer注解的字段名称===>>>" + declaedField.getName());
                    }
                }
            }

            // 判断服务消费者的注解
            classNameList.stream().forEach((className) -> {
                Class<?> aClass = null;
                try {
                    aClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                // Field[] fields = aClass.getFields();
                Field[] fields = aClass.getDeclaredFields();
                aClass.getAnnotations();
                Stream.of(fields).forEach((field) -> {
                    RpcServiceProvider rpcServiceProvider = field.getAnnotation(RpcServiceProvider.class);
                    if (rpcServiceProvider != null) { // 用到了注解
                        System.out.println("标注了@RpcReference注解的字段名称===>>>" + field.getName());
                        //LOGGER.info("标注了@RpcReference注解的字段名称===>>>" + field.getName());
                    }
                });
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // todo 还没有完全 map放入数值。
        return handlerMap;
    }

    /**
     *
     */
    private static String getServiceName(RpcServiceProvider rpcServiceProvider) {

        Class tClass = rpcServiceProvider.interfaceClass();
        if (tClass == void.class) {
            return rpcServiceProvider.interfaceClassName();
        }

        String serviceName = tClass.getName();
        if (serviceName == null || serviceName.trim().isEmpty()) {
            serviceName = rpcServiceProvider.interfaceClassName();
        }
        return serviceName;
    }

}
