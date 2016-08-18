package com.java.component.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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
 * Module Desc:com.java.component.async
 * User: zjprevenge
 * Date: 2016/8/17
 * Time: 16:14
 */

public interface AsyncService {

    /**
     * 提交任务
     *
     * @param callable
     * @return 返回事否提交成功
     */
    boolean submitFuture(KeyCallable<?> callable);

    /**
     * 提交任务
     *
     * @param threadPoolKey
     * @param callable
     * @return 返回是否提交成功
     */
    boolean submitFuture(String threadPoolKey, KeyCallable<?> callable);

    /**
     * 提交任务
     *
     * @param callable
     * @return 返回成功结果
     */
    Future<?> submitFuture(Callable<?> callable);

    /**
     * 装配key对应的线程池
     *
     * @param threadPoolKey
     * @param threadPool
     */
    void assemblyPool(String threadPoolKey, ThreadPoolExecutor threadPool);
}
