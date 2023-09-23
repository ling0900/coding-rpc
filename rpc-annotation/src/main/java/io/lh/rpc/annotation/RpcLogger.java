package io.lh.rpc.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface RpcLogger{

}
