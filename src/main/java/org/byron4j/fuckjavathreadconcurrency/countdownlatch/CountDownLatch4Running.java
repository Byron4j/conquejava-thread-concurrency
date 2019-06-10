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
