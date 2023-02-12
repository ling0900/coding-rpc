package io.lh.rpc.commom.scanner.server;

import io.lh.rpc.annotation.RpcServiceConsumer;
import io.lh.rpc.annotation.RpcServiceProvider;
import io.lh.rpc.commom.scanner.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

/**
 * 描述：注解扫描器
 * 版本：1.0.0
 * 作者：lh
 * 创建时间：2023/02/11
 */
public class RpcServiceProviderScanner extends ClassScanner {


    // todo LOGGER原理是什么？
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceProviderScanner.class);

    public static Map<String, Objects> doScannerWithRpcReferenceAnnotationFilter(String scanPackage) {
        Map<String, Objects> handlerMap = new HashMap<>();
        try {
            List<String> classNameList = getClassNameList(scanPackage);

            if (classNameList == null || classNameList.isEmpty()) return handlerMap;


            List<Field[]> list = new ArrayList<>();

            // getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。
            // getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。

            for (String className : classNameList) {
                Class<?> aClass = Class.forName(className);
                RpcServiceProvider annotation = aClass.getAnnotation(RpcServiceProvider.class);
                if (annotation != null) { // 判断类上用到了注解
                    System.out.println("标注了@RpcReference注解的字段名称===>>>" + aClass.getName());
                }

                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field declaedField : declaredFields) { // 用来检查类内部有没有使用某个注解的
                    RpcServiceConsumer rpcServiceProvider = declaedField.getAnnotation(RpcServiceConsumer.class);
                    if (rpcServiceProvider != null) { // 用到了注解
                        System.out.println("标注了@RpcServiceConsumer注解的字段名称===>>>" + declaedField.getName());
                    }
                }
            }



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
        }
        // todo 还没有给这个map放入数值。
        return handlerMap;
    }
}
