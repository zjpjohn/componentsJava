package com.java.component.thread.pool;

import java.io.Serializable;

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
 * Time: 0:29
 */

public class ThreadPoolInfo implements Serializable, Cloneable {
    private static final long serialVersionUID = 2501208560813428817L;

    //线程池名称
    private String name;
    //核心线程数
    private int coreSize = 5;
    //最大线程数
    private int maxSize = 30;
    //空闲线程存活时间
    private long threadKeepAliveTime = 5;
    //线程池队列容量
    private int queueSize = 10000;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getThreadKeepAliveTime() {
        return threadKeepAliveTime;
    }

    public void setThreadKeepAliveTime(long threadKeepAliveTime) {
        this.threadKeepAliveTime = threadKeepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }


    @Override
    public ThreadPoolInfo clone() {
        ThreadPoolInfo poolInfo = new ThreadPoolInfo();
        poolInfo.name = this.name;
        poolInfo.coreSize = this.coreSize;
        poolInfo.maxSize = this.maxSize;
        poolInfo.threadKeepAliveTime = this.threadKeepAliveTime;
        poolInfo.queueSize = this.queueSize;
        return poolInfo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ThreadPoolInfo that = (ThreadPoolInfo) object;

        if (coreSize != that.coreSize) return false;
        if (maxSize != that.maxSize) return false;
        if (threadKeepAliveTime != that.threadKeepAliveTime) return false;
        if (queueSize != that.queueSize) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + coreSize;
        result = 31 * result + maxSize;
        result = 31 * result + (int) (threadKeepAliveTime ^ (threadKeepAliveTime >>> 32));
        result = 31 * result + queueSize;
        return result;
    }
}
