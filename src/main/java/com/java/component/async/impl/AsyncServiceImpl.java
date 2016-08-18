package com.java.component.async.impl;

import com.java.component.async.AsyncService;
import com.java.component.async.KeyCallable;
import com.java.component.async.KeyFuture;
import com.java.component.async.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * ━━━━━━南无阿弥陀佛━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃stay hungry stay foolish
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━萌萌哒━━━━━━
 * Module Desc:com.java.component.async.impl
 * User: zjprevenge
 * Date: 2016/8/17
 * Time: 16:39
 */

public class AsyncServiceImpl implements AsyncService {
    private static final Logger log = LoggerFactory.getLogger(AsyncServiceImpl.class);

    private final int WARNING_TIMEOUT = 2000;

    private final int INTERRUPT_TIMEOUT = 10000;

    private final String DEFAULT_THREAD_POOL = "default_thread_pool";

    private final ExecutorService defaultThreadPool;

    private final ThreadPoolExecutor observerFuturePool;

    public ConcurrentMap<String, ExecutorService> threadPoolMap;

    private final BlockingQueue<KeyFuture<?>> futureQueue;

    public AsyncServiceImpl() {
        defaultThreadPool = new ThreadPoolExecutor(256, 256,
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory("async", true));
        observerFuturePool = new ThreadPoolExecutor(3, 3,
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory("future-observe", true));
        threadPoolMap = new ConcurrentSkipListMap<String, ExecutorService>();
        futureQueue = new LinkedBlockingDeque<KeyFuture<?>>();
        threadPoolMap.put(DEFAULT_THREAD_POOL, defaultThreadPool);
        startObserveFuture();
    }

    public void startObserveFuture() {
        observerFuturePool.execute(observeTask);
    }

    private final Runnable observeTask = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    KeyFuture<?> keyFuture = futureQueue.take();
                    String key = keyFuture.getKey();
                    Future<?> future = keyFuture.getFuture();
                    long start = System.currentTimeMillis();
                    try {
                        future.get(INTERRUPT_TIMEOUT, TimeUnit.MILLISECONDS);
                        long costTime = System.currentTimeMillis() - start;
                        if (costTime >= WARNING_TIMEOUT) {
                            log.warn("WARNING:future={} costTime={}", key, costTime);
                        } else {
                            log.info("future={} costTime={}", key, costTime);
                        }
                    } catch (ExecutionException e) {
                        log.error(e.getMessage(), e);
                    } catch (TimeoutException e) {
                        log.error("ERROR:Timeout:future={} costTime={}", key, INTERRUPT_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }

            }
        }
    };

    /**
     * 提交任务
     *
     * @param callable
     * @return 返回事否提交成功
     */
    @Override
    public boolean submitFuture(KeyCallable<?> callable) {
        return submitFuture(DEFAULT_THREAD_POOL, callable);
    }

    /**
     * 提交任务
     *
     * @param threadPoolKey
     * @param callable
     * @return 返回是否提交成功
     */
    @Override
    public boolean submitFuture(String threadPoolKey, KeyCallable<?> callable) {
        try {
            Future<?> future = getExecutorService(threadPoolKey).submit(callable);
            futureQueue.put(new KeyFuture(callable.getKey(), future));
            return true;
        } catch (InterruptedException e) {
            log.error(e.getMessage() + callable.getKey(), e);
        }
        return false;
    }

    /**
     * 提交任务
     *
     * @param callable
     * @return 返回成功结果
     */
    @Override
    public Future<?> submitFuture(Callable<?> callable) {
        try {
            Future<?> future = defaultThreadPool.submit(callable);
            futureQueue.put(new KeyFuture(callable.getClass().getName(), future));
            return future;
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 装配key对应的线程池
     *
     * @param threadPoolKey
     * @param threadPool
     */
    @Override
    public void assemblyPool(String threadPoolKey, ThreadPoolExecutor threadPool) {
        ExecutorService executorService = threadPoolMap.putIfAbsent(threadPoolKey, threadPool);
        if (executorService != null) {
            log.error("{} is assembled...", threadPoolKey);
        }
    }

    public ExecutorService getExecutorService(String key) {
        return threadPoolMap.get(key);
    }
}
