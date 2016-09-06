package com.java.component.asynctask.process;

import com.java.component.asynctask.bean.AsyncTaskFuture;
import com.java.component.asynctask.handle.FutureResultProcessorCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

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
 * Module Desc:com.java.component.asynctask.process
 * User: zjprevenge
 * Date: 2016/8/23
 * Time: 16:26
 */

public class FutureResultProcessor extends Thread {

    private static final Logger log = LoggerFactory.getLogger(FutureResultProcessor.class);

    private static boolean processFuture = true;

    private BlockingQueue<AsyncTaskFuture> futureQueue;

    private FutureResultProcessorCallback callback;


    public FutureResultProcessor() {
        super();
    }

    public FutureResultProcessor(BlockingQueue<AsyncTaskFuture> futureQueue,
                                 FutureResultProcessorCallback callback) {
        super();
        this.futureQueue = futureQueue;
        this.callback = callback;
    }

    @Override
    public void run() {
        while (processFuture) {
            try {
                AsyncTaskFuture taskFuture = futureQueue.take();
                callback.process(taskFuture);
            } catch (InterruptedException e) {
                log.error("获取异步任务信息出现异常：{}", e);
            }
        }
    }

    public void clearUp() {
        processFuture = false;
        futureQueue = null;
        callback = null;
    }
}
