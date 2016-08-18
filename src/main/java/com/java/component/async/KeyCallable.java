package com.java.component.async;

import java.util.concurrent.Callable;

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
 * Module Desc:com.java.component.async
 * User: zjprevenge
 * Date: 2016/8/17
 * Time: 16:15
 */

public abstract class KeyCallable<V> implements Callable<V> {

    private final String key;

    private volatile boolean canceled = false;

    public KeyCallable(String key) {
        this.key = key;
    }

    public abstract V execute();

    @Override
    public V call() throws Exception {
        if (!canceled) {
            V v = execute();
            return v;
        }
        return null;
    }

    public void cancel() {
        this.canceled = true;
    }

    public String getKey() {
        return key;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
