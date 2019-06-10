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
