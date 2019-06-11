package org.byron4j.fuckjavathreadconcurrency.curr.waitnotifypattern;

import org.byron4j.fuckjavathreadconcurrency.utils.Utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {
    Lock lock = new ReentrantLock();
    Condition produceLock = lock.newCondition();
    Condition consumeLock = lock.newCondition();

    Object[] msgs = new Object[10];
    int count, proIndex, conIndex;

    /**
     * 生产数据，数组满了则等待被消费
     */
    void product(Object obj) throws InterruptedException {
        lock.lock();
        try{
            while( count ==  msgs.length){
                // 满了则等待
                System.out.println("队列已满，等待被消耗...");
                produceLock.await();
            }

            msgs[proIndex++] = obj;
            if(proIndex == msgs.length) proIndex = 0;
            ++count;
            consumeLock.signal();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 消费数据，空了则等待放入数据
     */
    Object consume() throws InterruptedException {
        lock.lock();
        try{
            while(count == 0){
                System.out.println("队列已空，等待新数据产生...");
                consumeLock.await();
            }
            Object obj = msgs[conIndex++];
            if(conIndex == msgs.length) conIndex = 0;
            --count;
            produceLock.signal();
            return obj;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {

        final ConditionTest conditionTest = new ConditionTest();
        new Thread(() -> {
            for( int i=1; i<=100; i++ ){
                try {
                    conditionTest.product(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        for( int i = 1; i<=100 ; i++ ){
            new Thread(() -> {
                try {
                    System.out.println(conditionTest.consume());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }


    }
}
