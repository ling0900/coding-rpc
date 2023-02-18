package io.lh.rpc.commom.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Server thread pool.
 * 线程池工具类
 * 主要是在服务提供者这端执行异步任务。
 * @author lh
 */
public class ServerThreadPool {

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65535));
    }

    /**
     * Submit.
     *
     * @param task the task
     */
    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    /**
     * Shutdown.
     */
    public static void shutdown() {
        threadPoolExecutor.shutdown();
    }
}
