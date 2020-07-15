package com.junlaninfo;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 辉 on 2020/7/11.
 */
public class Test001 extends Thread {

    private static int count = 0;
    ReentrantLock lock = new ReentrantLock();//默认是创建非公平锁

    @Override
    public void run() {
        while (count < 1000) {
            create();
        }

    }

    private void create() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {

        }
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "," + count++);
        lock.unlock();

    }

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            new TEST().start();
        }

    }
}
