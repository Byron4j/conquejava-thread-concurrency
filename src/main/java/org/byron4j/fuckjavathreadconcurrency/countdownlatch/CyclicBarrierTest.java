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
