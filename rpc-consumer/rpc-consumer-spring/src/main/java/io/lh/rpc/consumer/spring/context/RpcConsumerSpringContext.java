package io.lh.rpc.consumer.spring.context;

import org.springframework.context.ApplicationContext;

public class RpcConsumerSpringContext {

    private ApplicationContext context;

    private RpcConsumerSpringContext(){

    }

    private static class Holder{
        private static final RpcConsumerSpringContext INSTANCE = new RpcConsumerSpringContext();
    }

    public static RpcConsumerSpringContext getInstance(){
        return Holder.INSTANCE;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

}
