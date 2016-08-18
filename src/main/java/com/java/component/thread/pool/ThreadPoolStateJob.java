package com.java.component.thread.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
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
 * Module Desc:com.java.component.thread.pool
 * User: zjprevenge
 * Date: 2016/8/18
 * Time: 0:39
 */

public class ThreadPoolStateJob extends AbstractJob {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolStateJob.class);

    private Map<String, ExecutorService> threadPoolMap;

    private int interval = 60;

    public ThreadPoolStateJob(Map<String, ExecutorService> threadPoolMap, int interval) {
        this.threadPoolMap = threadPoolMap;
        this.interval = interval;
    }

    @Override
    public void execute() {
        for (Map.Entry<String, ExecutorService> entry : threadPoolMap.entrySet()) {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) entry.getValue();
            log.info(String.format("ThreadPool:%s, ActiveThread:%d, TotalTask:%d, CompletedTask:%d, Queue:%d",
                    entry.getKey(),
                    pool.getActiveCount(),
                    pool.getTaskCount(),
                    pool.getCompletedTaskCount(),
                    pool.getQueue().size()));
        }
        try {
            Thread.sleep(interval * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
