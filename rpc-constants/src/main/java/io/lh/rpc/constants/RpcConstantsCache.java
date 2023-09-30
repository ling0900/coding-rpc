package io.lh.rpc.constants;

/**
 * The type Rpc constants cache.
 * 和缓存相关的常量放在这个类里面，方便后续的管理。
 */
public class RpcConstantsCache {

    /**
     * 扫描结果缓存的时间间隔，单位毫秒
     */
    public static final int RPC_SCAN_RESULT_CACHE_TIME_INTERVAL = 1000;

    /**
     * 默认的结果缓存时长，单位是毫秒
     */
    public static final int RPC_SCAN_RESULT_CACHE_EXPIRE = 5000;

}
