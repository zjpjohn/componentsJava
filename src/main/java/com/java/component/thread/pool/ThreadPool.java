package com.java.component.thread.pool;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
 * Module Desc:com.java.component.thread.pool
 * User: zjprevenge
 * Date: 2016/8/18
 * Time: 0:44
 */

public interface ThreadPool {

    /**
     * 提交一个不需要返回值的异步任务给默认的线程池执行
     *
     * @param task
     * @return
     */
    Future<?> submit(Runnable task);

    /**
     * 提交一个不需要返回值的异步任务给指定的线程池
     *
     * @param task           异步任务
     * @param threadPoolName 线程池名称
     * @return
     */
    Future<?> submit(Runnable task, String threadPoolName);

    /**
     * 提交一个需要返回值的异步任务给默认的线程池执行
     *
     * @param task 异步任务
     * @param <T>
     * @return
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * 提交一个需要返回值的异步任务给指定线程池
     *
     * @param task           异步任务
     * @param threadPoolName 线程池名称
     * @param <T>
     * @return
     */
    <T> Future<T> submit(Callable<T> task, String threadPoolName);

    /**
     * 在默认线程池中执行多个需要返回值的异步任务，并设置超时时间
     *
     * @param tasks    异步任务列表
     * @param timeout  超时时间
     * @param timeUnit 超时时间单位
     * @param <T>
     * @return
     */
    <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeUnit);

    /**
     * 在指定的线程池中执行多个有返回值的异步任务，并设定超时时间
     *
     * @param tasks          异步任务列表
     * @param timeout        超时时间
     * @param timeUnit       超时时间单位
     * @param threadPoolName 线程池名称
     * @param <T>
     * @return
     */
    <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeUnit, String threadPoolName);

    /**
     * 查询指定名称的线程池是否存在
     *
     * @param threadPoolName 线程池名称
     * @return 存在 true；不存在 false
     */
    boolean isExists(String threadPoolName);

    /**
     * 获取线程池的信息
     *
     * @param threadPoolName 线程池名称
     * @return 线程池信息
     */
    ThreadPoolInfo getThreadPoolInfo(String threadPoolName);
}
