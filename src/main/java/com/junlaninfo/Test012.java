package com.junlaninfo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName Test012
 * @Author 蚂蚁课堂余胜军 QQ644064779 www.mayikt.com
 * @Version V1.0
 **/
public class Test012 extends Thread {

    private static int count = 0;
    private static Object lockObject = new Object();
    private static Lock lock = new ReentrantLock(true);
    private static MeiteMayiktLock meiteMayiktLock = new MeiteMayiktLock();

    @Override
    public void run() {
        while (count < 1000) {
            create3();
        }

    }

    private void create() {
        try {
            Thread.sleep(350);
        } catch (Exception e) {

        }
//        synchronized (lockObject) {
        System.out.println(Thread.currentThread().getName() + "," + count++);
//        }

    }

    private void create2() {
        try {
            Thread.sleep(300);
        } catch (Exception e) {

        }
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "," + count++);
        lock.unlock();
    }

    private void create3() {
        try {
            Thread.sleep(300);
        } catch (Exception e) {

        }
        meiteMayiktLock.lock();
        System.out.println(Thread.currentThread().getName() + "," + count++);
        meiteMayiktLock.unLock();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            new Test012().start();
        }


    }
}
