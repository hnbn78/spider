package com.a3.lottery.spider.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.a3.lottery.domain.config.WorkerConfig;

public class WorkGroup<T extends Worker> {

	private ExecutorService workerThreads = Executors.newCachedThreadPool();
	private Map<String, Worker> taskMap = new ConcurrentHashMap<>();
	private Map<String, Future<? extends WorkerResult>> taskFutureMap = new ConcurrentHashMap<>();
	
	

	public synchronized void startThread(Worker worker, WorkerConfig workerConfig) {

		if (taskMap.get(workerConfig.getWorkerKey()) == null) {

			taskMap.put(workerConfig.getWorkerKey(), worker);

			Future<WorkerResult> future = workerThreads.submit(worker);
			taskFutureMap.put(workerConfig.getWorkerKey(), future);
		}
	}

	public synchronized void stopThread(WorkerConfig workerConfig) {
		Worker oldTask = taskMap.get(workerConfig.getWorkerKey());
		if (oldTask != null) {
			oldTask.tryStop();
		}

		Future<? extends WorkerResult> future = taskFutureMap.get(workerConfig.getWorkerKey());
		if (future != null) {
			try {
				future.get(5, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {

			} finally {
				if (oldTask != null) {
					oldTask.tryStop();
				}
				taskMap.remove(workerConfig.getWorkerKey());
				taskFutureMap.remove(workerConfig.getWorkerKey());
			}
		}
	}

	public synchronized void refreshThread(Worker worker, WorkerConfig workerConfig) {
		stopThread(workerConfig);
		startThread(worker, workerConfig);
	}

	public boolean checkThread(WorkerConfig workerConfig) {
		Future<? extends WorkerResult> future = taskFutureMap.get(workerConfig.getWorkerKey());
		WorkerResult result = null;
		try {
			result = future.get(1, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		// 线程已结束
		if (result != null) {
			taskMap.remove(workerConfig.getWorkerKey());
			taskFutureMap.remove(workerConfig.getWorkerKey());
			return false;
		} else {
			return true;
		}
	}

	public void shutdown() {
		for (Worker worker : taskMap.values()) {
			worker.tryStop();
		}
		try {
			workerThreads.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		workerThreads.shutdownNow();
	}

}
