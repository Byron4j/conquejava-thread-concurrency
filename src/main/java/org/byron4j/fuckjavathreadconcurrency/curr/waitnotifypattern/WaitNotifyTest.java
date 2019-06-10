package org.byron4j.fuckjavathreadconcurrency.curr.waitnotifypattern;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 模拟等待通知场景
 */
public class WaitNotifyTest {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SS");

    public static void main(String[] args) throws InterruptedException {


        Task task = new Task();
        task.start();
        Thread.sleep(5000);
        Task.wakeup();

    }

    static class Task extends Thread{

        private static final Object obj = new Object();

        @Override
        public void run() {
            synchronized (obj){
                try {
                    System.out.println("开始执行业务..."+ sdf.format(new Date()));
                    Thread.sleep(2000);
                    obj.wait();
                    System.out.println("被通知"+ sdf.format(new Date()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void wakeup(){
            synchronized (obj){
                obj.notify();
                System.out.println("通知释放"+ sdf.format(new Date()));
            }
        }
    }
}
