package com.java.component.eventbus;

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
 * Module Desc:com.java.component.eventbus
 * User: zjprevenge
 * Date: 2016/8/18
 * Time: 17:38
 */

public class Event implements Serializable {
    private static final long serialVersionUID = -8840823291773818205L;

    private Object bean;

    private String methodName;

    private Object[] args;

    @SuppressWarnings("rawtypes")
    private Class[] clazzs;

    public Event(Object bean, String methodName, Object[] args) {
        this.bean = bean;
        this.methodName = methodName;
        this.args = args;
    }

    public Event(Object bean, String methodName, Object[] args, @SuppressWarnings("rawtypes") Class[] clazzs) {
        this.bean = bean;
        this.methodName = methodName;
        this.args = args;
        this.clazzs = clazzs;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @SuppressWarnings("rawtypes")
    public Class[] getClazzs() {
        return clazzs;
    }

    @SuppressWarnings("rawtypes")
    public void setClazzs(Class[] clazzs) {
        this.clazzs = clazzs;
    }
}
