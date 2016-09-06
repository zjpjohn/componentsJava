package com.java.component.asynctask.bean;

import java.util.concurrent.Future;

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
 * Module Desc:com.java.component.asynctask.bean
 * User: zjprevenge
 * Date: 2016/8/23
 * Time: 16:09
 */

public class AsyncTaskFuture {

    private AsyncTaskContext context;

    private Future<FutureResult> future;

    public AsyncTaskFuture() {
    }

    public AsyncTaskFuture(AsyncTaskContext context, Future<FutureResult> future) {
        this.context = context;
        this.future = future;
    }

    public AsyncTaskContext getContext() {
        return context;
    }

    public void setContext(AsyncTaskContext context) {
        this.context = context;
    }

    public Future<FutureResult> getFuture() {
        return future;
    }

    public void setFuture(Future<FutureResult> future) {
        this.future = future;
    }
}
