package com.junlaninfo;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName MeiteMayiktLock
 * @Author 蚂蚁课堂余胜军 QQ644064779 www.mayikt.com
 * @Version V1.0
 **/
public class MeiteMayiktLock {

    /**
     * 获取锁的状态 0当前线程没有获取该锁，1 已经有线程获取到该锁
     */
//    private volatile int state;

    /**
     * 获取锁的状态 0当前线程没有获取该锁，1 已经有线程获取到该锁
     */
    AtomicInteger atomicstate = new AtomicInteger(0);
    // 记录锁被那个线程持有
    private transient Thread exclusiveOwnerThread;
    // 存放没有获取锁到的线程
    private ConcurrentLinkedDeque<Thread> waitThreads = new ConcurrentLinkedDeque<>();

    // 获取锁
    public void lock() {
        //e=0,n=1 v=0
        // 底层使用cas 修改锁的状态从0变为1  硬件层面帮助我们实现
        if (acquire()) {
            return;
        }

        // 使用cas 修改锁的状态失败 设计重试次数
        Thread currentThread = Thread.currentThread();
        // 如果该线程已经存在的情况下
        waitThreads.add(currentThread);
        for (; ; ) {
            //短暂重试
            if (acquire()) {
                // 移除队列
                waitThreads.push(currentThread);
                return;
            }
            // 重试一次还是没有获取到锁，将当前的这个线程变为阻塞状态
            LockSupport.park();

        }
    }

    /**
     * 释放锁
     */
    public void unLock() {
        if (exclusiveOwnerThread != Thread.currentThread()) {
            throw new RuntimeException("不是当前线程在释放锁");
        }
        // 释放锁
        if (compareAndSetState(1, 0)) {
            this.exclusiveOwnerThread = null;
            // 取出阻塞的线程 唤醒
            Thread pollThread = waitThreads.poll();
            if (pollThread != null)
                // 唤醒刚才阻塞的线程
                LockSupport.unpark(pollThread);
        }
    }

    //修改锁的状态
    private boolean acquire() {
        if (compareAndSetState(0, 1)) {
            //当前线程获取到锁
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    /**
     * CAS 三个参数  V  E
     *
     * @param expect
     * @param update
     * @return
     */
    private boolean compareAndSetState(int expect, int update) {
        return atomicstate.compareAndSet(expect, update);
    }

    public void setExclusiveOwnerThread(Thread exclusiveOwnerThread) {
        this.exclusiveOwnerThread = exclusiveOwnerThread;
    }

}
