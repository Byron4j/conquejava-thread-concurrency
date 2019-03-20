package org.byron4j.fuckjavathreadconcurrency.beforejava5SynCurr;

import org.byron4j.fuckjavathreadconcurrency.utils.Utils;

public class SynCounter {

    private static  int count = 0;
    public static  void increment() {
        synchronized (SynCounter.class) {
            count++;
        }
    }
    public static  int getCount() {
        synchronized (SynCounter.class) {
            return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i=1; i <= 1000; i++){
            Utils.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //count++;
                    increment();
                }
            });
        }

        Thread.sleep(2000);
        System.out.println("count: " + getCount());
    }


}
