package com.java.component.thread.pool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

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
 * Time: 0:21
 */

public class DefaultThreadFactory implements ThreadFactory {

    private String namePrefix;

    private AtomicInteger number = new AtomicInteger(1);

    private ThreadGroup threadGroup;

    private boolean daemon;

    public DefaultThreadFactory() {
        this("thr");
    }

    public DefaultThreadFactory(String namePrefix) {
        this(false, namePrefix);
    }

    public DefaultThreadFactory(boolean daemon, String namePrefix) {
        this.daemon = daemon;
        this.namePrefix = namePrefix;
        ThreadGroup root = ThreadUtil.getRootThreadGroup();
        threadGroup = new ThreadGroup(root, namePrefix + "-pool");
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(threadGroup, runnable);
        thread.setDaemon(daemon);
        if (Thread.NORM_PRIORITY != thread.getPriority()) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
