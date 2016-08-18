package com.java.component.thread.pool;

import com.google.common.collect.Maps;

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
 * Time: 11:55
 */

public class ThreadPoolConfig {

    private Map<String, ThreadPoolInfo> multiThreadPoolInfo = Maps.newHashMap();

    private boolean threadPoolStateSwitch = false;
    private int threadPoolStateInterval = 60;

    private boolean threadStateSwitch = false;
    private int threadStateInterval = 60;

    public Map<String, ThreadPoolInfo> getMultiThreadPoolInfo() {
        return multiThreadPoolInfo;
    }

    public void setMultiThreadPoolInfo(Map<String, ThreadPoolInfo> multiThreadPoolInfo) {
        this.multiThreadPoolInfo = multiThreadPoolInfo;
    }

    public boolean isThreadPoolStateSwitch() {
        return threadPoolStateSwitch;
    }

    public void setThreadPoolStateSwitch(boolean threadPoolStateSwitch) {
        this.threadPoolStateSwitch = threadPoolStateSwitch;
    }

    public boolean isThreadStateSwitch() {
        return threadStateSwitch;
    }

    public void setThreadStateSwitch(boolean threadStateSwitch) {
        this.threadStateSwitch = threadStateSwitch;
    }

    public int getThreadPoolStateInterval() {
        return threadPoolStateInterval;
    }

    public void setThreadPoolStateInterval(int threadPoolStateInterval) {
        this.threadPoolStateInterval = threadPoolStateInterval;
    }

    public int getThreadStateInterval() {
        return threadStateInterval;
    }

    public void setThreadStateInterval(int threadStateInterval) {
        this.threadStateInterval = threadStateInterval;
    }
}
