package io.lh.rpc.provider.common.cache;

import io.netty.channel.Channel;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The type Provider channel cache.
 * @author Ling
 */
public class ProviderChannelCache {
    /**
     * The constant channelCache.
     */
    private static volatile Set<Channel> channelCache = new
            CopyOnWriteArraySet<>();

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
