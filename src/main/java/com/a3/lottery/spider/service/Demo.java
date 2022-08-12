package com.a3.lottery.spider.service;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class Demo {
	
	 
	    private static final int timeOut = 60 * 1000;

	  public static void main(String[] args) {
		  
			PoolingHttpClientConnectionManager cm = null;
	    	
   	   RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
          ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
          registryBuilder.register("http", plainSF);
          // 指定信任密钥存储对象和连接套接字工厂
          try {
              KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
              // 信任任何链接
              TrustStrategy anyTrustStrategy = new TrustStrategy() {

                  public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                      return true;
                  }
              };
              SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy)
                      .build();
              LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext,
                      SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
              registryBuilder.register("https", sslSF);
          } catch (KeyStoreException e) {
              e.printStackTrace();
              throw new RuntimeException(e);
          } catch (KeyManagementException e) {
              e.printStackTrace();
              throw new RuntimeException(e);
          } catch (NoSuchAlgorithmException e) {
              e.printStackTrace();
              throw new RuntimeException(e);
          }
          Registry<ConnectionSocketFactory> registry = registryBuilder.build();

          // 初始化连接管理器
          cm = new PoolingHttpClientConnectionManager(registry);
          // Increase max total connection to 200
          cm.setMaxTotal(200);
          // Increase default max connection per route to 20
          cm.setDefaultMaxPerRoute(20);
          
   	 // 请求重试处理
       HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {

           public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
               if (executionCount >= 1) {// 如果已经重试了3次，就放弃
                   return false;
               }
               if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                   return true;
               }
               if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                   return false;
               }
               if (exception instanceof InterruptedIOException) {// 超时
                   return false;
               }
               if (exception instanceof UnknownHostException) {// 目标服务器不可达
                   return false;
               }
               if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                   return false;
               }
               if (exception instanceof SSLException) {// SSL握手异常
                   return false;
               }

               HttpClientContext clientContext = HttpClientContext.adapt(context);
               HttpRequest request = clientContext.getRequest();
               // 如果请求是幂等的，就再次尝试
               if (!(request instanceof HttpEntityEnclosingRequest)) {
                   return true;
               }
               return false;
           }
       };
       CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
               .setRetryHandler(httpRequestRetryHandler).build();
       String url = "https://sapi.og088.com/u/user/match?sign=0af4cacf61001cf5750a829751913c59&t=1643040699";
       HttpPost httpPost = new HttpPost(url);
       
       /**
        * 	Accept: application/json
			Accept-Encoding: gzip, deflate, br
			Accept-Language: zh-CN,zh;q=0.9
			Cache-Control: no-cache
			Connection: keep-alive
			Content-Length: 2
			Content-Type: application/json;charset=UTF-8
			D: pc_browser
			Host: sapi.og088.com
			L: zh-CN
			Origin: https://www.og11.com
			Pragma: no-cache
			Referer: https://www.og11.com/
			S: a54fd6bfdcae26b952c4432abf04f30c
			sec-ch-ua: " Not;A Brand";v="99", "Google Chrome";v="97", "Chromium";v="97"
			sec-ch-ua-mobile: ?1
			sec-ch-ua-platform: "Android"
			Sec-Fetch-Dest: empty
			Sec-Fetch-Mode: cors
			Sec-Fetch-Site: cross-site
			User-Agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Mobile Safari/537.36
        */
       httpPost.setHeader("Accept", "application/json");
       httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
       httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
       httpPost.setHeader("Cache-Control", "no-cache");
       httpPost.setHeader("Connection", "keep-alive");
//       httpPost.setHeader("Content-Length", "20000");
       httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
       httpPost.setHeader("D", "pc_browser");
       httpPost.setHeader("Host", "sapi.og088.com");
       httpPost.setHeader("L", "zh-CN");
       httpPost.setHeader("Origin", "https://www.og11.com");
       httpPost.setHeader("Pragma", "no-cache");
       httpPost.setHeader("Referer", "https://www.og11.com/");
       httpPost.setHeader("S", "a54fd6bfdcae26b952c4432abf04f30c");
       httpPost.setHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"");
       httpPost.setHeader("sec-ch-ua-mobile", "?1");
       httpPost.setHeader("sec-ch-ua-platform", "\"Android\"");
       httpPost.setHeader("Sec-Fetch-Dest", "empty");
       httpPost.setHeader("Sec-Fetch-Mode", "cors");
       httpPost.setHeader("Sec-Fetch-Site", "cross-site");
      
      
      
	   httpPost.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Mobile Safari/537.36");
		
		
       config(httpPost);
       String json = null;
       CloseableHttpResponse response = null;
//       httpPost.setEntity(paramEntity);
       try {
           response = httpClient.execute(httpPost);
           HttpEntity entity = response.getEntity();
           json = EntityUtils.toString(entity, "UTF-8");
           System.out.println(json);
       } catch (Exception e) {
           e.printStackTrace();
       } 
	}
	  
	  
	  private static void config(HttpRequestBase httpRequestBase) {

	        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
	                .setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
	        httpRequestBase.setConfig(requestConfig);
	    }
}
