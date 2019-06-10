package org.byron4j.fuckjavathreadconcurrency.curr.waitnotifypattern;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {
    public static void main(String[] args){
        Condition condition = new ReentrantLock().newCondition();
    }
}
