package com.java.component.asynctask.core;

import com.google.common.base.Preconditions;
import com.java.component.asynctask.bean.AsyncTaskContext;
import com.java.component.asynctask.bean.AsyncTaskFuture;
import com.java.component.asynctask.bean.FutureResult;
import com.java.component.asynctask.handle.FutureResultProcessorCallback;
import com.java.component.asynctask.process.FutureResultProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

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
 * Time: 16:25
 */

public class AsyncTaskResultHandler implements InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(AsyncTaskResultHandler.class);

    private FutureResultProcessor processor;

    private BlockingQueue<AsyncTaskFuture> futureQueue;

    private FutureResultProcessorCallback callback;

    public FutureResultProcessorCallback getCallback() {
        return callback;
    }

    public void setCallback(FutureResultProcessorCallback callback) {
        this.callback = callback;
    }

    @Override
    public void destroy() throws Exception {
        processor.clearUp();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        futureQueue = new LinkedBlockingDeque<>();
        processor = new FutureResultProcessor(futureQueue,
                Preconditions.checkNotNull(callback)
        );
        processor.start();
    }

    public void addAsyncTaskResult(Future<FutureResult> resultFuture, AsyncTaskContext context) {
        futureQueue.offer(new AsyncTaskFuture(context, resultFuture));
    }
}
