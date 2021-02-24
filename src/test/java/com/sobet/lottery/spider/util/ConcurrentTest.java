package com.sobet.lottery.spider.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTest {

	public static void test(int threads, final Runnable runnable) {

		final CountDownLatch countDown = new CountDownLatch(1);
		final CountDownLatch finishCountDown = new CountDownLatch(threads);

		final int repeat = 500;
		
		Runnable run = new Runnable() {

			@Override
			public void run() {
				try {
					countDown.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					for (int i = 0; i < repeat; i++) {
						runnable.run();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				finishCountDown.countDown();
			}
		};

		ExecutorService executor = Executors.newFixedThreadPool(threads);

		for (int i = 0; i < threads; i++) {
			executor.execute(run);
		}
		long s = System.currentTimeMillis();
		countDown.countDown();

		try {
			finishCountDown.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		long e = System.currentTimeMillis();
		long time = e - s;
		System.out.println("TOTAL:TIME:" + time +"ms");
		double per = time / threads ;
		double qps = threads*repeat*1000/time;
		
		System.out.println(per);
		System.out.println(qps);
	}
}
