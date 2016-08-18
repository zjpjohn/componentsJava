package com.java.component.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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
 * Module Desc:com.java.component.eventbus
 * User: zjprevenge
 * Date: 2016/8/18
 * Time: 17:38
 */

public class EventListener {
    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    @Subscribe
    @AllowConcurrentEvents
    public void listen(Event event) throws Exception {
        if (event == null) {
            log.warn("event is null,do nothing...");
            return;
        }
        //目标方法参数
        Object[] args = event.getArgs();
        //目标方法参数类型
        Class[] parameterTypes = event.getClazzs();
        if (parameterTypes == null) {
            if (null != args && args.length > 0) {
                parameterTypes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    parameterTypes[i] = args[i].getClass();
                }
            }
        }

        //获取bean的方法对象
        Method method = event.getBean().getClass().getDeclaredMethod(event.getMethodName(), parameterTypes);
        method.setAccessible(true);
        //调用目标方法
        method.invoke(event.getBean(), args);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void listenStatic(Map<String, Object> params) throws Exception {
        String className = String.valueOf(params.get("className"));
        String methodName = String.valueOf(params.get("methodName"));
        Object[] args = (Object[]) params.get("params");
        Class<?> clazz = Class.forName(className);
        Class[] argList = null;
        if (null != args && args.length > 0) {
            argList = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argList[i] = args[i].getClass();
            }
        }
        Method method = clazz.getMethod(methodName, argList);
        method.invoke(clazz, argList);
    }
}
