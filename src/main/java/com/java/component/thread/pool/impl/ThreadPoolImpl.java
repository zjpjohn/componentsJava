package com.java.component.thread.pool.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.java.component.thread.pool.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
 * Module Desc:com.java.component.thread.pool.impl
 * User: zjprevenge
 * Date: 2016/8/18
 * Time: 11:04
 */

public class ThreadPoolImpl implements ThreadPool, LifeCycle {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolImpl.class);

    //默认线程名称
    private static final String DEFAULT_THREAD_POOL = "default";

    private ThreadPoolConfig poolConfig;

    private Map<String, ExecutorService> multiThreadPool = Maps.newHashMap();

    private ThreadPoolStateJob threadPoolStateJob;

    private ThreadStateJob threadStateJob;

    public ThreadPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(ThreadPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public ThreadPoolStateJob getThreadPoolStateJob() {
        return threadPoolStateJob;
    }

    public void setThreadPoolStateJob(ThreadPoolStateJob threadPoolStateJob) {
        this.threadPoolStateJob = threadPoolStateJob;
    }

    public ThreadStateJob getThreadStateJob() {
        return threadStateJob;
    }

    public void setThreadStateJob(ThreadStateJob threadStateJob) {
        this.threadStateJob = threadStateJob;
    }

    /**
     * 初始化
     */
    @Override
    public void init() {
        initThreadPool();
        startThreadPoolStateJob();
        startThreadStateJob();
    }

    /**
     * 初始化所有线程
     */
    private void initThreadPool() {
        boolean defaultPoolExists = false;
        Map<String, ThreadPoolInfo> poolInfoMap = poolConfig.getMultiThreadPoolInfo();
        for (Map.Entry<String, ThreadPoolInfo> entry : poolInfoMap.entrySet()) {
            ThreadPoolInfo threadPoolInfo = entry.getValue();
            if (DEFAULT_THREAD_POOL.equals(threadPoolInfo.getName())) {
                defaultPoolExists = true;
            }
            BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(threadPoolInfo.getQueueSize());
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(),
                    threadPoolInfo.getMaxSize(),
                    threadPoolInfo.getThreadKeepAliveTime(),
                    TimeUnit.SECONDS,
                    blockingQueue,
                    new DefaultThreadFactory(threadPoolInfo.getName()));
            multiThreadPool.put(threadPoolInfo.getName(), threadPoolExecutor);
            log.info(String.format("initialization thread pool %s success", threadPoolInfo.getName()));
        }
        if (!defaultPoolExists) {
            throw new IllegalStateException("the default thread pool not exists...");
        }
    }

    /**
     * 初始化并启动线程池状态统计job
     */
    private void startThreadPoolStateJob() {
        if (poolConfig.isThreadPoolStateSwitch()) {
            threadPoolStateJob = new ThreadPoolStateJob(multiThreadPool,
                    poolConfig.getThreadPoolStateInterval());
            threadPoolStateJob.init();
            Thread thread = new Thread(threadPoolStateJob);
            thread.setName("pool-job-state");
            thread.start();
            log.info("start job 'pool-job-state' success");
        }
    }

    /**
     * 初始化并启动线程状态统计job
     */
    private void startThreadStateJob() {
        if (poolConfig.isThreadStateSwitch()) {
            threadStateJob = new ThreadStateJob(poolConfig.getThreadStateInterval());
            threadStateJob.init();
            Thread thread = new Thread(threadStateJob);
            thread.setName("job-thread-state");
            thread.start();
            log.info("start job 'job-thread-state' success");
        }
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        for (Map.Entry<String, ExecutorService> entry : multiThreadPool.entrySet()) {
            entry.getValue().shutdown();
        }

        if (threadPoolStateJob != null) {
            threadPoolStateJob.destroy();
            threadPoolStateJob = null;
        }
        if (threadStateJob != null) {
            threadStateJob.destroy();
            threadStateJob = null;
        }
    }

    /**
     * 提交一个不需要返回值的异步任务给默认的线程池执行
     *
     * @param task
     * @return
     */
    @Override
    public Future<?> submit(Runnable task) {
        return submit(task, DEFAULT_THREAD_POOL);
    }

    /**
     * 提交一个不需要返回值的异步任务给指定的线程池
     *
     * @param task           异步任务
     * @param threadPoolName 线程池名称
     * @return
     */
    @Override
    public Future<?> submit(Runnable task, String threadPoolName) {
        Preconditions.checkNotNull(task);
        ExecutorService threadPool = getExistsThreadPool(threadPoolName);
        if (log.isDebugEnabled()) {
            log.debug("submit a task to thread pool:{}", threadPoolName);
        }

        return threadPool.submit(task);
    }

    /**
     * 提交一个需要返回值的异步任务给默认的线程池执行
     *
     * @param task 异步任务
     * @return
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, DEFAULT_THREAD_POOL);
    }

    /**
     * 提交一个需要返回值的异步任务给指定线程池
     *
     * @param task           异步任务
     * @param threadPoolName 线程池名称
     * @return
     */
    @Override
    public <T> Future<T> submit(Callable<T> task, String threadPoolName) {
        Preconditions.checkNotNull(task);
        ExecutorService threadPool = getExistsThreadPool(threadPoolName);
        if (log.isDebugEnabled()) {
            log.debug("submit a tsk to thread pool:{}", threadPoolName);
        }
        return threadPool.submit(task);
    }

    /**
     * 在默认线程池中执行多个需要返回值的异步任务，并设置超时时间
     *
     * @param tasks    异步任务列表
     * @param timeout  超时时间
     * @param timeUnit 超时时间单位
     * @return
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeUnit) {
        return invokeAll(tasks, timeout, timeUnit, DEFAULT_THREAD_POOL);
    }

    /**
     * 在指定的线程池中执行多个有返回值的异步任务，并设定超时时间
     *
     * @param tasks          异步任务列表
     * @param timeout        超时时间
     * @param timeUnit       超时时间单位
     * @param threadPoolName 线程池名称
     * @return
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeUnit, String threadPoolName) {
        Preconditions.checkArgument(null == tasks || tasks.isEmpty(), "task list must not be empty");
        Preconditions.checkArgument(timeout <= 0, "timeout must greater than zero");
        ExecutorService threadPool = getExistsThreadPool(threadPoolName);
        if (log.isDebugEnabled()) {
            log.debug("invoke task list to thread pool:{}", threadPool);
        }
        try {
            return threadPool.invokeAll(tasks, timeout, timeUnit);
        } catch (InterruptedException e) {
            log.error("invoke task list occurs error:{}", e);
        }
        return null;
    }

    /**
     * 查询指定名称的线程池是否存在
     *
     * @param threadPoolName 线程池名称
     * @return 存在 true；不存在 false
     */
    @Override
    public boolean isExists(String threadPoolName) {
        ExecutorService service = multiThreadPool.get(threadPoolName);
        return null == service;
    }

    /**
     * 获取线程池的信息
     *
     * @param threadPoolName 线程池名称
     * @return 线程池信息
     */
    @Override
    public ThreadPoolInfo getThreadPoolInfo(String threadPoolName) {
        ThreadPoolInfo poolInfo = poolConfig.getMultiThreadPoolInfo().get(threadPoolName);
        return poolInfo.clone();
    }

    private ExecutorService getExistsThreadPool(String threadPoolName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(threadPoolName), "threadPoolName must not be empty...");
        ExecutorService executorService = multiThreadPool.get(threadPoolName);
        Preconditions.checkNotNull(executorService);
        return executorService;
    }
}
