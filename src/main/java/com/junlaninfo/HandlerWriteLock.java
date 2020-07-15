package com.junlaninfo;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by 辉 on 2020/7/10.
 */
public class HandlerWriteLock {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    private transient Thread ownLockThread;
    ConcurrentLinkedDeque<Thread> waitThreads = new ConcurrentLinkedDeque<>();

    public void lock() {
        if (acquire()) { //获取到锁
            return;  //直接结束掉方法
        }
        //没有获取到锁进入自旋 ,把当前线程放入等待同步队列中
        Thread currentThread = Thread.currentThread();
        waitThreads.add(currentThread);//放入到等待的同步队列中
        for (; ; ) {
            if (acquire()) {
                //第一次自旋获取到了锁，那么就把它从等待同步队列中push出来
                waitThreads.push(currentThread);
                return;  //结束方法
            }
            //第一次自旋不成功，那么进入阻塞
            LockSupport.park();
        }
    }

    //释放锁的方法
    public void unlock() {
        //判断拥有锁的线程是否是当前线程s,如果不是拥有锁的线程释放锁，那么就报异常
        if (ownLockThread != Thread.currentThread()) {
            throw new RuntimeException("不是持有锁的线程释放锁");
        }
        if (compareAndSetState(1, 0)) {
            Thread poll = waitThreads.poll();
            ownLockThread = null;//将持有锁的线程清空
            if (poll != null) {
                LockSupport.unpark(poll);
            }
        }
    }

    private boolean acquire() {
        if (compareAndSetState(0, 1)) {
            //同时保存获取到锁的线程
            setOwnLockThread(Thread.currentThread());
            return true;//返回true，表示获取到锁
        }
        return false;
    }

    private void setOwnLockThread(Thread thread) {
        this.ownLockThread = thread;
    }

    private boolean compareAndSetState(int expect, int update) {
        return atomicInteger.compareAndSet(expect, update);
    }
}
