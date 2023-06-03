package io.lhrpc.consumer.common.cache;

import io.netty.channel.Channel;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The type Consumer channel cache.
 * 用来缓存连接成功channel的 缓存类工具。它的主要的作用是在服务消费者端缓存连接服务提供者成功的 channel。
 * @author menglinghao
 */
public class ConsumerChannelCache {

    private static volatile Set<Channel> channelCache = new CopyOnWriteArraySet<>();

    /**
     * Add.
     *
     * @param channel the channel
     */
    public static void add(Channel channel) {
        channelCache.add(channel);
    }

    /**
     * Remove.
     *
     * @param channel the channel
     */
    public static void remove(Channel channel) {
        channelCache.remove(channel);
    }

    /**
     * Gets channel cache.
     *
     * @return the channel cache
     */
    public static Set<Channel> getChannelCache() {
        return channelCache;
    }
}
