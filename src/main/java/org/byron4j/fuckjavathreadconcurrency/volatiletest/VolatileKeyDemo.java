package org.byron4j.fuckjavathreadconcurrency.volatiletest;

import org.byron4j.fuckjavathreadconcurrency.utils.Utils;

/**
 * volatile 关键字用法
 * stop 如果不是 volatile 修饰，则thread启动后，看到的是自己线程栈的变量值false，陷入死循环；
 * 如果被 volatile 修饰，则 stop 变量是线程可见的，stop 设置为true后，thread 会退出 while 循环
 */
public class VolatileKeyDemo {
    volatile boolean stop = false;

    public static void main(String[] args) {

        VolatileKeyDemo demo = new VolatileKeyDemo();

        Thread thread = new Thread(demo.getConcurrencyCheckTask());
        thread.start();

        Utils.sleep(1000);
        System.out.println("Set stop to true in main!");
        demo.stop = true;
        System.out.println("Exit main.");
    }

    ConcurrencyCheckTask getConcurrencyCheckTask() {
        return new ConcurrencyCheckTask();
    }

    private class ConcurrencyCheckTask implements Runnable {
        @Override
        public void run() {
            System.out.println("ConcurrencyCheckTask started!");
            // 如果主线中stop的值可见，则循环会退出。
            while (!stop) {
            }
            System.out.println("ConcurrencyCheckTask stopped!");
        }
    }
}
