package com.java.component.kafka.common;

import com.java.component.kafka.exec.ConnectionException;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.Closeable;
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
 * Module Desc:com.java.component.kafka.common
 * User: zjprevenge
 * Date: 2016/9/6
 * Time: 13:54
 */

public abstract class PoolBase<T> implements Closeable, Serializable {


    private static final long serialVersionUID = -5194231383069803279L;

    protected GenericObjectPool<T> pool;

    public PoolBase() {
    }

    public PoolBase(final GenericObjectPoolConfig config,
                    PooledObjectFactory<T> factory) {
        initPool(config, factory);
    }

    protected void initPool(final GenericObjectPoolConfig config,
                            PooledObjectFactory<T> factory) {
        if (pool != null) {
            this.destroy();
        }
        this.pool = new GenericObjectPool<T>(factory, config);
    }

    /**
     * 从对象池中获取对象
     *
     * @return
     */
    protected T getResource() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new ConnectionException("Could not get a resource from the pool", e);
        }
    }

    /**
     * 返回对象
     *
     * @param resource
     */
    protected void returnResource(final T resource) {
        if (resource != null) {
            try {
                pool.returnObject(resource);
            } catch (Exception e) {
                throw new ConnectionException("Could not return the resource to the pool", e);
            }
        }
    }

    /**
     * 是当前对象失效
     *
     * @param resource
     */
    protected void invalidateResource(final T resource) {
        if (resource != null) {
            try {
                pool.invalidateObject(resource);
            } catch (Exception e) {
                throw new ConnectionException("could not invalidate the resource to the pool", e);
            }
        }
    }

    /**
     * 获取活动的对象数量
     *
     * @return
     */
    public int getNumActive() {
        if (isInactive()) {
            return -1;
        }
        return pool.getNumActive();
    }

    /**
     * 判断对象池是否关闭
     *
     * @return
     */
    public boolean isClosed() {
        try {
            return pool.isClosed();
        } catch (Exception e) {
            throw new ConnectionException("Could not check closed from the pool", e);
        }
    }

    /**
     * 获取空闲的对象数量
     *
     * @return
     */
    public int getNumIdle() {
        if (isInactive()) {
            return -1;
        }
        return pool.getNumIdle();
    }

    /**
     * 获取等待的对象数
     *
     * @return
     */
    public int getNumWaiters() {
        if (isInactive()) {
            return -1;
        }
        return pool.getNumWaiters();
    }

    /**
     * 获取平均等待时间
     *
     * @return
     */
    public long getMeanBorrowWaitTimeMillis() {
        if (isInactive()) {
            return -1;
        }
        return pool.getMeanBorrowWaitTimeMillis();
    }

    /**
     * 获取最大等待时间
     *
     * @return
     */
    public long getMaxBorrowWaitTimeMillis() {
        if (isInactive()) {
            return -1;
        }
        return pool.getMaxBorrowWaitTimeMillis();
    }

    /**
     * 对对象池扩容
     *
     * @param count
     */
    public void addObjects(final int count) {
        try {
            for (int i = 0; i < count; i++) {
                pool.addObject();
            }
        } catch (Exception e) {
            throw new ConnectionException("Error trying to add idle objects", e);
        }
    }

    /**
     * 清除对象
     */
    public void clear() {
        try {
            pool.clear();
        } catch (Exception e) {
            throw new ConnectionException("Could not clear the pool", e);
        }
    }

    /**
     * 判断对象池是否失效
     *
     * @return
     */
    private boolean isInactive() {
        try {
            return pool == null || pool.isClosed();
        } catch (Exception e) {
            throw new ConnectionException("Could not check inactive from the pool", e);
        }
    }

    /**
     * 销毁对象池
     */
    protected void destroy() {
        this.close();
    }

    /**
     * 关闭对象池
     */
    @Override
    public void close() {
        try {
            pool.close();
        } catch (Exception e) {
            throw new ConnectionException("Could not destroy this pool", e);
        }
    }
}
