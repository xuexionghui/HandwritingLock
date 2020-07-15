package com.junlaninfo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 辉 on 2020/7/11.
 * AQS如何实现重入锁次数增加
 */
public class Test002 {
   static ReentrantLock lock = new ReentrantLock(false);
    public static void main(String[] args) {
        lock.lock();
        lock.lock();

    }
}
