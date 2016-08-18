package com.java.component.thread.pool;

import com.google.common.base.Preconditions;
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
 * Date: 2016/8/17
 * Time: 23:52
 */

public class ThreadUtil {

    /**
     * 获取当前线程的顶级线程组
     *
     * @return 顶级线程组
     */
    public static ThreadGroup getRootThreadGroup() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while (null != group.getParent()) {
            group = group.getParent();
        }
        return group;
    }

    /**
     * 获取所有线程组下的所有线程状态信息
     *
     * @return
     */
    public static Map<String, ThreadStateInfo> getAllThreadState() {
        ThreadGroup root = getRootThreadGroup();
        int groupCapacity = root.activeCount() * 2;
        ThreadGroup[] groupList = new ThreadGroup[groupCapacity];
        int groupNum = root.enumerate(groupList, true);
        Map<String, ThreadStateInfo> statInfoList = Maps.newHashMap();
        statInfoList.put(root.getName(), getThreadState(root));
        for (int i = 0; i < groupNum; i++) {
            ThreadGroup group = groupList[i];
            ThreadStateInfo threadState = getThreadState(group);
            statInfoList.put(group.getName(), threadState);
        }
        return statInfoList;
    }

    /**
     * 获取指定的线程组中所有线程的状态信息
     *
     * @param threadGroup 线程组
     * @return
     */
    public static ThreadStateInfo getThreadState(ThreadGroup threadGroup) {
        Preconditions.checkNotNull(threadGroup);
        int threadCapacity = threadGroup.activeCount() * 2;
        Thread[] threads = new Thread[threadCapacity];
        int threadNum = threadGroup.enumerate(threads);

        ThreadStateInfo stateInfo = new ThreadStateInfo();

        for (int i = 0; i < threadNum; i++) {
            Thread thread = threads[i];
            switch (thread.getState()) {
                case NEW:
                    stateInfo.newCount++;
                    break;
                case RUNNABLE:
                    stateInfo.runnableCount++;
                    break;
                case BLOCKED:
                    stateInfo.blockedCount++;
                    break;
                case WAITING:
                    stateInfo.waitingCount++;
                    break;
                case TIMED_WAITING:
                    stateInfo.timeWaitingCount++;
                    break;
                case TERMINATED:
                    stateInfo.terminateCount++;
                    break;
                default:
                    break;
            }
        }
        return stateInfo;
    }
}
