package com.junlaninfo;

/**
 * Created by 辉 on 2020/7/10.
 */
public class TEST extends  Thread {
    private  static  int count=0;
     HandlerWriteLock handlerWriteLock=new  HandlerWriteLock();
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
        handlerWriteLock.lock();
        System.out.println(Thread.currentThread().getName() + "," + count++);
        handlerWriteLock.unlock();

    }

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            new TEST().start();
        }

    }
}
