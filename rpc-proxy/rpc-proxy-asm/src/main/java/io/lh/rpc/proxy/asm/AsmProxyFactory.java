package io.lh.rpc.proxy.asm;

import io.lh.rpc.proxy.api.BaseProxyFactory;
import io.lh.rpc.proxy.api.ProxyFactory;
import io.lh.rpc.proxy.asm.proxy.ASMProxy;
import io.lh.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPIClass
public class AsmProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {
    private final Logger logger = LoggerFactory.getLogger(AsmProxyFactory.class);

    @Override
    public <T> T getProxy(Class<T> clazz) {
        try {
            logger.info("基于ASM动态代理...");
            return (T) ASMProxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, objectProxy);
        } catch (Exception e) {
            logger.error("asm proxy throws exception:{}", e);
        }
        return null;
    }
}
