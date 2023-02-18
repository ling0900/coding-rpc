package io.lh.rpc.commom.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Client thread pool.
 * @author lh
 */
public class ClientThreadPool {
    private static ThreadPoolExecutor threadPoolExecutor;
    static {
        threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
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
