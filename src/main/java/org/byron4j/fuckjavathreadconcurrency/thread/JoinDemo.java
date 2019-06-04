package org.byron4j.fuckjavathreadconcurrency.thread;

import org.byron4j.fuckjavathreadconcurrency.utils.Utils;

public class JoinDemo {
	public static void main(String[] args) throws InterruptedException {
		TaskA taskA = new TaskA();
		new Thread(taskA).start();
		Thread.sleep(1000);
		
		// 用户请求完毕
		
		TaskE taskE = new TaskE();
		Thread te = new Thread(taskE);
		te.start();
	}
}

class TaskA implements Runnable{

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println("A-申请开通会员." + Utils.acquireCurrMillisSec());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

class TaskB implements Runnable{

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println("B-冻结券." + Utils.acquireCurrMillisSec());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

class TaskC implements Runnable{

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println("C-冻结额度." + Utils.acquireCurrMillisSec());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

class TaskD implements Runnable{

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println("D-资金扣款." + Utils.acquireCurrMillisSec());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

class TaskE implements Runnable{


	TaskB taskB = new TaskB();
	TaskC taskC = new TaskC();
	TaskD taskD = new TaskD();
	
	Thread tb = new Thread(taskB);
	Thread tc = new Thread(taskC);
	Thread td = new Thread(taskD);
	
	
	
	@Override
	public void run() {
		try {
			tb.start();
			tb.join();
			tc.start();
			tc.join();
			td.start();
			td.join();
			Thread.sleep(30);
			System.out.println("E-购买会员成功." + Utils.acquireCurrMillisSec());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
