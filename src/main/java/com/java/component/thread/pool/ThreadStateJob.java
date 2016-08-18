package com.java.component.thread.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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
 * Time: 0:40
 */

public class ThreadStateJob extends AbstractJob {

    private static final Logger log = LoggerFactory.getLogger(ThreadStateJob.class);

    private int interval = 60;

    public ThreadStateJob(int interval) {
        this.interval = interval;
    }

    @Override
    public void execute() {
        Map<String, ThreadStateInfo> stateInfoMap = ThreadUtil.getAllThreadState();
        for (Map.Entry<String, ThreadStateInfo> entry : stateInfoMap.entrySet()) {
            ThreadStateInfo stateInfo = entry.getValue();
            log.info(String.format("ThreadGroup:%s, New:%d,  Runnable:%d, Blocked:%d, Waiting:%d, TimedWaiting:%d, Terminated:%d",
                    entry.getKey(), stateInfo.getNewCount(), stateInfo.getRunnableCount(), stateInfo.getBlockedCount(),
                    stateInfo.getWaitingCount(), stateInfo.getTimeWaitingCount(), stateInfo.getTerminateCount()));
        }
        try {
            Thread.sleep(interval * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
