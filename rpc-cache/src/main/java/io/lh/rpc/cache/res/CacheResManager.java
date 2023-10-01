package io.lh.rpc.cache.res;

import io.lh.rpc.constants.RpcConstantsCache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The type Cache res manager.
 * 缓存结果的管理工具类。
 * @param <T> the type parameter
 */
public class CacheResManager<T> {

    /**
     * 缓存结果信息 map结构，线程安全的。
     */
    private final Map<CacheResKey, T> cacheResult = new ConcurrentHashMap<>(4096);

    /**
     * 扫描结果缓存的线程池
     *
     */
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    /**
     * 读写锁
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 读锁
     */
    private final Lock readLock = lock.readLock();

    /**
     * 写锁
     */
    private final Lock writeLock = lock.writeLock();

    /**
     * 结果缓存过期时长：毫秒
     */
    private int resultCacheExpire;

    private static volatile CacheResManager instance;


    public static <T> CacheResManager<T> getInstance(int resultCacheExpire, boolean enableResCache) {
        if (null == instance) {
            synchronized (CacheResManager.class) {
                if (null == instance) {
                    instance = new CacheResManager(resultCacheExpire, enableResCache);
                }
            }
        }
        return instance;
    }

    /**
     * Instantiates a new Cache res manager.
     *
     * @param resultCacheExpire the result cache expire
     * @param enableResultCache the enable result cache
     */
    public CacheResManager(int resultCacheExpire, boolean enableResultCache){
        this.resultCacheExpire = resultCacheExpire;
        if (enableResultCache){
            this.startScanTask();
        }
    }

    /**
     * 扫描结果缓存
     */
    private void startScanTask() {
        scheduledExecutorService.scheduleAtFixedRate(()->{
            if (cacheResult.size() > 0){
                writeLock.lock();
                try{
                    Iterator<Map.Entry<CacheResKey, T>> iterator = cacheResult.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<CacheResKey, T> entry = iterator.next();
                        CacheResKey cacheKey = entry.getKey();
                        //当时间减去保存数据的缓存时间大于配置的时间间隔，则需要剔除缓存数据
                        if (System.currentTimeMillis() - cacheKey.getCacheTimeStamp() > resultCacheExpire){
                            cacheResult.remove(cacheKey);
                        }
                    }
                }finally {
                    writeLock.unlock();
                }
            }
        }, 0, RpcConstantsCache.RPC_SCAN_RESULT_CACHE_TIME_INTERVAL, TimeUnit.MILLISECONDS);
    }


    /**
     * 获取缓存中的数据
     *
     * @param key the key
     * @return the t
     */
    public T get(CacheResKey key){
        return cacheResult.get(key);
    }

    /**
     * 缓存数据
     *
     * @param key   the key
     * @param value the value
     */
    public void put(CacheResKey key, T value){
        writeLock.lock();
        try {
            cacheResult.put(key, value);
        }finally {
            writeLock.unlock();
        }
    }
}
