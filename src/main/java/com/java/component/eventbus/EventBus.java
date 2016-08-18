package com.java.component.eventbus;

import com.google.common.collect.Maps;
import com.google.common.eventbus.AsyncEventBus;

import java.util.Map;
import java.util.concurrent.Executors;

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
 * Module Desc:com.java.component.eventbus
 * User: zjprevenge
 * Date: 2016/8/18
 * Time: 17:38
 */
@SuppressWarnings("rawtypes")
public class EventBus {

    private AsyncEventBus eventBus = null;

    private static EventBus instance;

    private EventBus() {
        //系统处理器数量
        int count = Runtime.getRuntime().availableProcessors();
        //创建eventbus
        eventBus = new AsyncEventBus("eventbus", Executors.newFixedThreadPool(count * 2));
        //注册事假监听器
        eventBus.register(new EventListener());
    }


    /**
     * 双重校验锁创建单例对象
     *
     * @return
     */
    public static final EventBus getInstance() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    /**
     * 异步提交任务
     *
     * @param bean
     * @param methodName
     * @param args
     * @param clazzs
     */
    public void invokeAsync(Object bean, String methodName, Object[] args, Class[] clazzs) {
        Event event = new Event(bean, methodName, args, clazzs);
        eventBus.post(event);
    }

    public void invokeAsync(Object bean, String methodName, Object... args) {
        invokeAsync(bean, methodName, args, null);
    }

    /**
     * 异步调用静态方法
     *
     * @param className
     * @param methodName
     * @param params
     */
    public void invokeStatic(String className, String methodName, Object... params) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("className", className);
        map.put("methodName", methodName);
        map.put("params", params);
        eventBus.post(map);
    }
}
