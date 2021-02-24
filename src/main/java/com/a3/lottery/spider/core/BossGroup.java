package com.a3.lottery.spider.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.yaml.snakeyaml.Yaml;

import com.a3.lottery.domain.config.DomainConfigBean;
import com.a3.lottery.domain.config.DrawConfig;
import com.a3.lottery.domain.config.LotteryConfigBean;
import com.a3.lottery.domain.config.PlatformConfig;

@Component
public class BossGroup {

	public String lotterysPath;
	public String domainsPath;

	Map<String, DrawConfig> lotteryConfigMap = new HashMap<>();
	Map<String, PlatformConfig> platformConfigMap = new HashMap<>();

	private WorkGroup<Worker> requestWorkerGroup;
	private WorkGroup<Worker> listenWorkerGroup;
	private WorkGroup<Worker> listenRetryWorkerGroup;

	private Set<String> platforms = new CopyOnWriteArraySet<>();

	private Yaml yaml = new Yaml();

	public void init() {
		lotterysPath = WebApplicationContextUtils.class.getResource("/config-lottery.yaml").getPath();
		domainsPath = WebApplicationContextUtils.class.getResource("/config-domain.yaml").getPath();

		requestWorkerGroup = new WorkGroup<>();
		listenWorkerGroup = new WorkGroup<>();
		listenRetryWorkerGroup = new WorkGroup<>();

		startSacanner();
	}

	private void startSacanner() {
		Thread scanThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					refresh();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
		});
		scanThread.start();
	}

	private void refresh() {
		List<DrawConfig> newConfigs = getConfigs();

		for (DrawConfig drawConfig : newConfigs) {
			DrawConfig oldConfig = lotteryConfigMap.get(drawConfig.getLotteryCode());
			LotterySpiderTask task = new LotterySpiderTask(drawConfig);

			lotteryConfigMap.put(drawConfig.getLotteryCode(), drawConfig);

			if (oldConfig == null) {
				requestWorkerGroup.startThread(task, drawConfig);
			} else {
				if (!drawConfig.equals(oldConfig)) {
					requestWorkerGroup.refreshThread(task, drawConfig);
				} else {
					boolean running = requestWorkerGroup.checkThread(drawConfig);
					if (!running) {
						lotteryConfigMap.remove(drawConfig.getLotteryCode());
					}
				}
			}
		}

		List<PlatformConfig> newPlatformConfigs = getPlatformConfigs();

		for (PlatformConfig platformConfig : newPlatformConfigs) {
			PlatformConfig oldConfig = platformConfigMap.get(platformConfig.getName());

			LotteryListenTask task = new LotteryListenTask(platformConfig);

			platformConfigMap.put(platformConfig.getName(), platformConfig);

			// retry Thread..
			PlatformConfig errorConfig = new PlatformConfig();
			errorConfig.setName(platformConfig.getName() + "_error");
			errorConfig.setUrl(platformConfig.getUrl());
			LotteryListenTask errorTask = new LotteryListenTask(errorConfig);
			
			if (oldConfig == null) {
				listenWorkerGroup.startThread(task, platformConfig);
				listenRetryWorkerGroup.startThread(errorTask, errorConfig);
				
			} else {
				if (!platformConfig.equals(oldConfig)) {
					listenWorkerGroup.refreshThread(task, platformConfig);
					listenRetryWorkerGroup.refreshThread(errorTask, errorConfig);
					
					boolean running = requestWorkerGroup.checkThread(platformConfig);
					if (!running) {
						platformConfigMap.remove(platformConfig.getName());
					}
				}
			}
		}

		Set<String> set = new HashSet<>();
		for (PlatformConfig platforms : newPlatformConfigs) {
			set.add(platforms.getName());
		}

		platforms = new CopyOnWriteArraySet<>(set);

	}

	private List<DrawConfig> getConfigs() {

		File file = new File(lotterysPath);

		LotteryConfigBean lotteryConfigeBean = null;
		try {
			lotteryConfigeBean = yaml.loadAs(new FileInputStream(file), LotteryConfigBean.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// List<DrawConfig> configs = new ArrayList<>();
		// {
		// DrawConfig config = new DrawConfig();
		// config.setLotteryCode("CQSSC");
		// config.setToCode("cqssc");
		// config.setPeriod(1000L);
		// config.setRoute("apiplus");
		// configs.add(config);
		// }

		if (lotteryConfigeBean != null) {
			return lotteryConfigeBean.getApiplus();
		}

		return null;
	}

	private List<PlatformConfig> getPlatformConfigs() {

		File file = new File(domainsPath);

		DomainConfigBean domainConfigBean = null;
		try {
			domainConfigBean = yaml.loadAs(new FileInputStream(file), DomainConfigBean.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// List<PlatformConfig> configs = new ArrayList<>();
		// {
		// PlatformConfig config = new PlatformConfig();
		// config.setName("test");
		// config.setUrl("http://127.0.0.1");
		// configs.add(config);
		// }

		if (domainConfigBean != null) {
			return domainConfigBean.getPushto();
		}

		return null;
	}

	public Set<String> getPlatforms() {
		return platforms;
	}

	public void shutdown() {
		requestWorkerGroup.shutdown();
		listenWorkerGroup.shutdown();
		listenRetryWorkerGroup.shutdown();
	}

}
