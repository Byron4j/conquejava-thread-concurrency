# Java并发之Condition案例

Condition 是一个接口。
Condition 接口的实现类是AQS中的内部类：java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject。
Lock接口中有一个java.util.concurrent.locks.Lock.newCondition方法获取Condition。
常用的实现有重入锁的实现：

```java
public Condition newCondition() {
    return sync.newCondition();
}
```

AQS的内部类ConditionObject借助内部的Node节点类实现同步与等待的：

```java
public class ConditionObject implements Condition, java.io.Serializable {
        private transient Node firstWaiter;
        private transient Node lastWaiter;
        ...
}
```


## await()方法

```java
public final void await() throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
    // 1. 创建一个Node节点并将其添加到链表尾
    Node node = addConditionWaiter(); 
    int savedState = fullyRelease(node);
    int interruptMode = 0;
    while (!isOnSyncQueue(node)) {
        LockSupport.park(this);
        if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
            break;
    }
    if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
        interruptMode = REINTERRUPT;
    if (node.nextWaiter != null) // clean up if cancelled
        unlinkCancelledWaiters();
    if (interruptMode != 0)
        reportInterruptAfterWait(interruptMode);
}
```


### addConditionWaiter()方法创建一个代表当前线程的Node到链表尾

```java
private Node addConditionWaiter() {
    Node t = lastWaiter;
    // If lastWaiter is cancelled, clean out.
    if (t != null && t.waitStatus != Node.CONDITION) {
        unlinkCancelledWaiters();
        t = lastWaiter;
    }
    Node node = new Node(Thread.currentThread(), Node.CONDITION);
    if (t == null)
        firstWaiter = node;
    else
        t.nextWaiter = node;
    lastWaiter = node;
    return node;
}
```

### signal()方法

signal方法会通知头节点（在队列中等待最久了）。

```java
public final void signal() {
    if (!isHeldExclusively())
        throw new IllegalMonitorStateException();
    Node first = firstWaiter;
    if (first != null)
        doSignal(first);
}
```


[WaitNotify案例](../../src/main/java/org/byron4j/fuckjavathreadconcurrency/curr/waitnotifypattern/WaitNotifyTest.java)