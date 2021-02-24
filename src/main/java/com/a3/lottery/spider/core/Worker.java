package com.a3.lottery.spider.core;

import java.util.concurrent.Callable;

import org.slf4j.Logger;

public abstract class Worker implements Callable<WorkerResult> {
	private static final long DEFAULT_PEROID = 1000L;

	private volatile boolean stop = false;

	public volatile long period = DEFAULT_PEROID;

	public void tryStop() {
		stop = true;
	}

	public abstract void doLoop();

	public abstract void init();

	public abstract Logger getLogger();

	@Override
	public WorkerResult call() {
		init();

		while (!stop) {
			try {
				doLoop();
				Thread.sleep(period);
			} catch (InterruptedException e) {
				tryStop();
			} catch (Exception e) {
				getLogger().error("Error do loop.", e);
				if (Thread.currentThread().isInterrupted())
					break;
			}
		}

		return null;
	}

	public void setSleepPeriod(Long toPeriod) {
		if (toPeriod == null || toPeriod <= 0) {
			period = DEFAULT_PEROID;
		} else {
			period = toPeriod;
		}
	}

}
