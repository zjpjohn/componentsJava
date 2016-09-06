package com.java.component.asynctask.core;

import com.java.component.asynctask.bean.AsyncTaskContext;
import com.java.component.asynctask.bean.FutureResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

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
 * Module Desc:com.java.component.asynctask.core
 * User: zjprevenge
 * Date: 2016/8/23
 * Time: 16:16
 */

public class AsyncTaskExecutor implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(AsyncTaskExecutor.class);

    private int coreSize;

    private int maxSize;

    private long keepAliveTime;

    private int queueSize;

    private ExecutorService executorService;

    private AsyncTaskResultHandler handler;

    public static Logger getLog() {
        return log;
    }

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public AsyncTaskResultHandler getHandler() {
        return handler;
    }

    public void setHandler(AsyncTaskResultHandler handler) {
        this.handler = handler;
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = new ThreadPoolExecutor(coreSize,
                maxSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(queueSize));

    }

    public void execute(Callable<FutureResult> task, AsyncTaskContext taskContext) {
        Future<FutureResult> resultFuture = executorService.submit(task);
        handler.addAsyncTaskResult(resultFuture, taskContext);
    }
}
