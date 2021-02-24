package com.a3.lottery.async;

import java.util.Date;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DefaultLauncher implements Launcher{
	@Async
	public void publishOrder(Object data) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new Date().toString());
	}
}
