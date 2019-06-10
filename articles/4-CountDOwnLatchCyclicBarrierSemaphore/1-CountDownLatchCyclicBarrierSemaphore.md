# CountDownLatchCyclicBarrierSemaphore 案例

## CountDownLatch

CountDown 倒计数器--JAVA并发类模拟运动员赛跑


>并发类CountDownLatch类的使用示例
 CountDownLatch = Count(计数) + Down(减少) + Latch(门闩(可以理解为控制开关))
 - 该类是java.util.concurrent包(大神 Doug Lea)下的一个同步锁计数器类。
 - 该类最有用的方法:
 - (1)  传入计数器初始值创建对象：CountDownLatch startLatch = new CountDownLatch(int cnt);
 - (2)  await()方法：所有线程处于等待直至等待时间超时、或者期间线程发生中断.
 - (3)  down()方法：对计数器进行减1操作，是同步方法。计数器原来大于0，只要减至0，则释放所有线程锁，使得线程继续工作；计数器原来已经为0，则nothing to do.


```java
package org.byron4j.fuckjavathreadconcurrency.countdownlatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *  @author Byron.Y.Y 2016年7月5日
 *	<pre>
 *	并发类CountDownLatch类的使用示例
 *	CountDownLatch = Count(计数) + Down(减少) + Latch(门闩(可以理解为控制开关))
 *	该类是java.util.concurrent包(大神 Doug Lea)下的一个同步锁计数器类。
 *	该类最有用的方法:
 *		(1)传入计数器初始值创建对象：CountDownLatch startLatch = new CountDownLatch(int cnt);
 *		(2)await()方法：所有线程处于等待直至等待时间超时、或者期间线程发生中断.
 *		(3)down()方法：对计数器进行减1操作，是同步方法。计数器原来大于0，只要减至0，则释放所有线程锁，使得线程继续工作；
 *				计数器原来已经为0，则nothing to do.
 *	</pre>
 *	
 */
public class CountDownLatch4Running{
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SS");
	public static void main(String[] args) throws Exception{
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch endLatch = new CountDownLatch(10);
		//创建线程池
		final ExecutorService es = Executors.newFixedThreadPool(10);
		//制定跑步规则--10个运动员（线程任务）
		for( int i=1; i<=10; i++ ){
			final int No = i;
			Runnable runner = new Runnable() {
				
				public void run() {
					try {
						//如果当前开始计数器为0立即返回。此处警示哨令未发出，所有运动员均处于预备状态（线程阻塞等待）
						startLatch.await();
						Thread.sleep((long)(Math.random() * 10000));//0-1的小数乘以10000到千级
						System.out.println(No + "  号运动员抵达终点." + sdf.format(new Date()));
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally{
						//只要有运动员抵达终点，结束计数器减1
						endLatch.countDown();
					}
				}
			};
			es.submit(runner);
		}
		
		//开始跑步...
		startLatch.countDown();
		System.out.println("预备，开始...");
		//等待所有运动员抵达终点,或者15秒未抵达结束
		endLatch.await(15000, TimeUnit.MILLISECONDS);
		System.out.println("体育课跑步测试结束。");
		//关闭线程池
		es.shutdown();
	}
}

```

运行结果为：

```
预备，开始...
1  号运动员抵达终点.2019-06-06 09:49:25 11
4  号运动员抵达终点.2019-06-06 09:49:25 126
10  号运动员抵达终点.2019-06-06 09:49:26 321
8  号运动员抵达终点.2019-06-06 09:49:26 439
5  号运动员抵达终点.2019-06-06 09:49:27 340
3  号运动员抵达终点.2019-06-06 09:49:27 405
6  号运动员抵达终点.2019-06-06 09:49:27 655
7  号运动员抵达终点.2019-06-06 09:49:28 856
9  号运动员抵达终点.2019-06-06 09:49:29 364
2  号运动员抵达终点.2019-06-06 09:49:31 34
体育课跑步测试结束。

```

示例代码见: [CountDownLatch4Running](../..//src/main/java/org/byron4j/fuckjavathreadconcurrency/countdownlatch/CountDownLatch4Running.java)

- 在线程（运动员）中，先等待： ```startLatch.await();```
- 开始跑步，哨令员放枪开始： ```startLatch.countDown();```
- 运动员开始跑，每个运动员通过sleep模拟了百米耗时，每一个运动员到终点后喊出已到达终点
- 等待15秒，结束比赛： ```endLatch.await(15000, TimeUnit.MILLISECONDS);```

- await() 方法会阻塞当前线程，知道通过调用countdown方法将计数减为0的时候，才会释放线程继续执行
- countdown() 方法会将计数进行递减，递减为0后释放所有被await方法阻塞的线程，如果已经为0则不做任何事情

**示例描述**
>CountDownLatch： 遥远的部落时代，有一群难民为了躲避战乱来到了一座城堡。
>
>- 存在一堵城墙，外面有一群难民，一开始大家都在等待城门开放（每个难民--线程调用await()阻塞等待）；
>
>- 烈日炙烤下，城主心怜，下令城门打开（countdown()计数减为0），难民蜂拥而入（躲进城内就是这些线程的业务逻辑）


## CyclicBarrier

循环栅栏，和 CountDownLatch 的差异是，```CyclicBarrier``` 可以在计数减为0之后可以重新循环使用，而  ```CountDownLatch``` 在计数减为0之后无法再次使用。

### 构造器

```java
CyclicBarrier(int parties)
```

创建一个新的 CyclicBarrier，它将在给定数量的参与者（线程）处于等待状态时启动，但它不会在启动 barrier 时执行预定义的操作。
          
```java
CyclicBarrier(int parties, Runnable barrierAction)
```

创建一个新的 CyclicBarrier，它将在给定数量的参与者（线程）处于等待状态时启动，并在启动 barrier 时执行给定的屏障操作，该操作由最后一个进入 barrier的线程执行。

如果parties为10，则需要10个线程都处于等待的时候，才会启动。

### 使用方法

```java
int	await()
```
 在所有参与者都已经在此 barrier 上调用 await 方法之前，将一直等待。

 
### 示例
 
 还是以跑步为例：
 
```java
package org.byron4j.fuckjavathreadconcurrency.countdownlatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {
	
	private static final int N = 10;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SS");
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(N, new Runnable() {
			
			@Override
			public void run() {
				System.out.println("当前线程"+Thread.currentThread().getName() + " " + sdf.format(new Date()));
			}
		});
		
		final ExecutorService es = Executors.newFixedThreadPool(10);
		
		for( int i=1; i<=N; i++ ){
			final int No = i;
			Runnable runner = new Runnable() {
				
				public void run() {
					try {
						cyclicBarrier.await();
						Thread.sleep((long)(Math.random() * 1000));
						System.out.println("当前线程"+Thread.currentThread().getName()  + " " + No + "  号运动员抵达终点." + sdf.format(new Date()));
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			es.submit(runner);
		}
		
		es.shutdown();
	}
}

```

运行输出：

```
当前线程pool-1-thread-10 2019-06-10 11:24:02 31
当前线程pool-1-thread-8 8  号运动员抵达终点.2019-06-10 11:24:02 195
当前线程pool-1-thread-1 1  号运动员抵达终点.2019-06-10 11:24:02 274
当前线程pool-1-thread-6 6  号运动员抵达终点.2019-06-10 11:24:02 461
当前线程pool-1-thread-5 5  号运动员抵达终点.2019-06-10 11:24:02 472
当前线程pool-1-thread-7 7  号运动员抵达终点.2019-06-10 11:24:02 504
当前线程pool-1-thread-2 2  号运动员抵达终点.2019-06-10 11:24:02 556
当前线程pool-1-thread-10 10  号运动员抵达终点.2019-06-10 11:24:02 785
当前线程pool-1-thread-4 4  号运动员抵达终点.2019-06-10 11:24:02 830
当前线程pool-1-thread-3 3  号运动员抵达终点.2019-06-10 11:24:02 913
当前线程pool-1-thread-9 9  号运动员抵达终点.2019-06-10 11:24:02 984

```


``CyclicBarrier(int parties, Runnable barrierAction)`` 会由最后一个await()的线程负责启动。


## Semaphore

信号量 Semaphore，可以控制访问线程的个数，``acquire()`` 方法获取一个许可，``release()`` 用于释放一个许可。

### 构造器

```java
public Semaphore(int permits) {          //参数permits表示许可数目，即同时可以允许多少线程进行访问
    sync = new NonfairSync(permits);
}
public Semaphore(int permits, boolean fair) {    //这个多了一个参数fair表示是否是公平的，即等待时间越久的越先获取许可
    sync = (fair)? new FairSync(permits) : new NonfairSync(permits);
}
```

### 常用重要方法


#### 阻塞方法

```java
public void acquire() throws InterruptedException {  }     //获取一个许可,若没有则一直等待
public void acquire(int permits) throws InterruptedException { }    //一次性获取permits个许可
public void release() { }          //释放一个许可
public void release(int permits) { }    //一次性释放permits个许可
```

#### 非阻塞方法

```java
public boolean tryAcquire() { };    //尝试获取一个许可，若获取成功，则立即返回true，若获取失败，则立即返回false

public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException { };  //尝试获取一个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false

public boolean tryAcquire(int permits) { }; //尝试获取permits个许可，若获取成功，则立即返回true，若获取失败，则立即返回false

public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException { }; //尝试获取permits个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false

```

### 5辆车过2车道的隧道示例

假设有5辆车，现在要过2车道的隧道。

```java
package org.byron4j.fuckjavathreadconcurrency.countdownlatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

import lombok.Getter;
import lombok.Setter;

public class SemaphoreTest {
	
	private static final int tunnel_N = 2;
	private static final int car_N = 5;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SS");
	
	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore(2, true);
		
		for( int i=1; i <=  car_N ; i++) {
			new Car(i, semaphore).start();
		}
		
	}
	
	@Getter
	@Setter
	static class Car extends Thread{
		private int no;
		private Semaphore semaphore;
		
		
		@Override
		public void run() {
			System.out.println();
			try {
				semaphore.acquire();
				System.out.println(no + "号车占领了一条车道" + " " + sdf.format(new Date()));
				Thread.sleep((long)(Math.random() * 10000));
				System.out.println(no + "号车空出一条车道" + " " + sdf.format(new Date()));
				semaphore.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		public Car(int no, Semaphore semaphore) {
			super();
			this.no = no;
			this.semaphore = semaphore;
		}
		
	}
}

```

运行结果：

```
2号车占领了一条车道 2019-06-10 12:32:53 307
4号车占领了一条车道 2019-06-10 12:32:53 307
4号车空出一条车道 2019-06-10 12:32:53 987
1号车占领了一条车道 2019-06-10 12:32:53 988
2号车空出一条车道 2019-06-10 12:33:01 394
3号车占领了一条车道 2019-06-10 12:33:01 394
1号车空出一条车道 2019-06-10 12:33:01 852
5号车占领了一条车道 2019-06-10 12:33:01 852
5号车空出一条车道 2019-06-10 12:33:08 431
3号车空出一条车道 2019-06-10 12:33:10 420
```

## 对比

- 1）CountDownLatch和CyclicBarrier都能够实现线程之间的等待，只不过它们侧重点不同：

	- CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；

	- 而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；

	- 另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。

- 2）Semaphore其实和锁有点类似，它一般用于控制对某组资源的访问权限。

## 示例程序

- [CountDownLatch示例](../../src/main/java/org/byron4j/fuckjavathreadconcurrency/countdownlatch/CountDownLatch4Running.java)
- [CyclicBarrier示例](../../src/main/java/org/byron4j/fuckjavathreadconcurrency/countdownlatch/CyclicBarrierTest.java)
- [Semaphore示例](../../src/main/java/org/byron4j/fuckjavathreadconcurrency/countdownlatch/SemaphoreTest.java)