package com.sobet.lottery;

import org.eclipse.jetty.server.Server;

import com.sobet.lottery.spider.util.JettyFactory;

public class BlockCenterServer {

	public static final int PORT = 1004;
	public static final String CONTEXT = "/lottery-center";
	public static final String[] TLD_JAR_NAMES = new String[] {"spring-webmvc" };

	public static void main(String[] args) throws Exception {
		
		Server server = JettyFactory.createServerInSource(PORT, CONTEXT);
//		JettyFactory.setTldJarNames(server, TLD_JAR_NAMES);
		try {
			server.start();
			System.out.println("[INFO] Server running at http://localhost:"
					+ PORT + CONTEXT);

			while (true) {
				char c = (char) System.in.read();
				if (c == '\n') {
					JettyFactory.reloadContext(server);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
