package com.java.component.ratelimit;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
 * Module Desc:com.java.component.ratelimit
 * User: zjprevenge
 * Date: 2016/8/19
 * Time: 14:41
 */

public class RateLimiterHelper {

    private static Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    /**
     * 根据指定的稳定吞吐量创建RateLimiter，这里的吞吐率是指每秒多少许可数(QPS)
     *
     * @param permitsPerSeconds 每秒许可数
     * @return
     */
    public static void create(String serviceName, double permitsPerSeconds) {
        RateLimiter rateLimiter = rateLimiterMap.get(serviceName);
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(permitsPerSeconds);
            rateLimiterMap.put(serviceName, rateLimiter);
        } else {
            double rate = rateLimiter.getRate();
            if (rate != permitsPerSeconds) {
                rateLimiter.setRate(permitsPerSeconds);
            }
        }
    }

    /**
     * 根据指定的稳定吞吐率和预热期来创建RateLimiter,
     * 在预热期时间内，RateLimiter每秒分配的许可数会平稳地增长到预热器结束时达到最大速率
     * 只要存在足够的请求来使其饱和
     *
     * @param permitsPerSeconds
     * @param warmupPeriod
     * @param unit
     * @return
     */
    public static void create(String serviceName, double permitsPerSeconds, long warmupPeriod, TimeUnit unit) {
        RateLimiter rateLimiter = rateLimiterMap.get(serviceName);
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(permitsPerSeconds, warmupPeriod, unit);
            rateLimiterMap.put(serviceName, rateLimiter);
        } else {
            double rate = rateLimiter.getRate();
            if (rate != permitsPerSeconds) {
                rateLimiter.setRate(permitsPerSeconds);
            }
        }
    }

    /**
     * 获取一个许可，该方法会阻塞知道获取请求，如果存在等待的情况，
     * 调用者获取到该请求所需的睡眠时间
     *
     * @return
     */
    public static double acquire(String serviceName) {
        RateLimiter rateLimiter = Preconditions.checkNotNull(rateLimiterMap.get(serviceName));
        return rateLimiter.acquire();
    }

    /**
     * 获取指定的许可个数，该方法会阻塞知道获取请求，如果存在等待的情况，
     * 调用者获取到该请求所需的睡眠时间
     *
     * @param permits
     * @return
     */
    public static double acquire(String serviceName, int permits) {
        RateLimiter rateLimiter = Preconditions.checkNotNull(rateLimiterMap.get(serviceName));
        return rateLimiter.acquire(permits);
    }

    /**
     * 获取正定服务的限流器的许可率
     *
     * @return
     */
    public static double getRate(String serviceName) {
        RateLimiter rateLimiter = Preconditions.checkNotNull(rateLimiterMap.get(serviceName));
        return rateLimiter.getRate();
    }

    /**
     * 更新稳定许可率
     *
     * @param permitsPerSeconds
     */
    public static void setRate(String serviceName, double permitsPerSeconds) {
        RateLimiter rateLimiter = Preconditions.checkNotNull(rateLimiterMap.get(serviceName));
        rateLimiter.setRate(permitsPerSeconds);
    }

    /**
     * 获取指定的服务的RateLimiter许可数
     * 在无延迟情况下获得返回true，否则false
     *
     * @param serviceName
     * @return
     */
    public static boolean tryAcquire(String serviceName) {
        RateLimiter rateLimiter = Preconditions.checkNotNull(rateLimiterMap.get(serviceName));
        return rateLimiter.tryAcquire();
    }

    /**
     * 获取去指定服务的RateLimiter的许可数
     * 在无延迟情况下返回true，否则false
     *
     * @param serviceName
     * @param permits
     * @return
     */
    public static boolean tryAcquire(String serviceName, int permits) {
        RateLimiter rateLimiter = Preconditions.checkNotNull(rateLimiterMap.get(serviceName));
        return rateLimiter.tryAcquire(permits);
    }

    /**
     * 在指定超时时间下，获取指定的服务的RateLimiter的一个许可数
     * 在超时时间内获取到返回true，否则false
     *
     * @param serviceName
     * @param timeout
     * @param unit
     * @return
     */
    public static boolean tryAcquire(String serviceName, long timeout, TimeUnit unit) {
        RateLimiter rateLimiter = Preconditions.checkNotNull(rateLimiterMap.get(serviceName));
        return rateLimiter.tryAcquire(timeout, unit);
    }

    /**
     * 在制定超时时间的情况下，获取指定服务的permits个许可
     *
     * @param serviceName
     * @param permits
     * @param timeout
     * @param unit
     * @return
     */
    public static boolean tryAcquire(String serviceName, int permits, long timeout, TimeUnit unit) {
        RateLimiter rateLimiter = Preconditions.checkNotNull(rateLimiterMap.get(serviceName));
        return rateLimiter.tryAcquire(permits, timeout, unit);
    }
}
