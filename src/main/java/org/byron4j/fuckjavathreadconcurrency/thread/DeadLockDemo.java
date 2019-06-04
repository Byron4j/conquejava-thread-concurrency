package org.byron4j.fuckjavathreadconcurrency.thread;

public class DeadLockDemo {
	
	
	
	public static void main(String[] args) {
		Object lockA = new Object();
		Object lockB = new Object();
		
		ThreadA ta = new ThreadA(lockA, lockB);
		ThreadB tb = new ThreadB(lockA, lockB);
		
		ta.start();
		tb.start();
		
	}
	
	
	
	static class ThreadA extends Thread{
		private Object lockA;
        private Object lockB;
        
        ThreadA(Object lockA, Object lockB){
        	this.lockA = lockA;
            this.lockB = lockB;
        }
        
        @Override
        public void run() {
        	synchronized (lockA) {
				try {
					Thread.sleep(1000);
					synchronized (lockB) {
						System.out.println("do A");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
	}
	
	static class ThreadB extends Thread{
		private Object lockA;
        private Object lockB;
        
        ThreadB(Object lockA, Object lockB){
        	this.lockA = lockA;
            this.lockB = lockB;
        }
        
        @Override
        public void run() {
        	synchronized (lockB) {
				try {
					Thread.sleep(1000);
					synchronized (lockA) {
						System.out.println("do B");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
	}
}
