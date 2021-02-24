package com.a3.lottery.spider.service;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.ProxyClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * HttpClient 相关工厂类
 * @author ostin
 * @since JUN 07 2015
 */
public class HttpClientFactory {
	
	/**
	 * 常用User-Agent 
	 */
	
	/** Chrome User-Agent */
	public static final String CHROME = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.94 Safari/537.36";
	
	/** FireFox User-Agent */
	public static final String FIREFOX = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0";
	
	/** IE11 Win8 User-Agent */
	public static final String IE11W8 = "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko";
	
	/** IE10 Win7 User-Agent */
	public static final String IE10W7 = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)";
	
	/** 默认连接超时时间  */
	public static final int DEFAULT_CONNECT_TIMEOUT = 30*1000;
	
	/** 默认读取超时时间 */
	public static final int DEFAULT_READ_TIMEOUT = 30*1000;
	
	
	/**
	 * 取得默认 HttpClient 实例
	 * @return
	 */
	public static HttpClient getDefaultHttpClientInstance(){
		return HttpClients.createDefault();
	}
	
	/**
	 * 生产一个带SSL支持的HttpClient实例
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static HttpClient getSSLHttpClientInstance(){
		
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
        registryBuilder.register("http", plainSF);  
        //指定信任密钥存储对象和连接套接字工厂  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            //信任任何链接  
            TrustStrategy anyTrustStrategy = new TrustStrategy() {  
            	
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {  
                    return true;  
                }  
            };  
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();  
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
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
        //设置连接管理器  
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);  
        //构建客户端  
        return HttpClientBuilder.create().setConnectionManager(connManager).build();  
	}
	
	/**
	 * 返回默认的 HttpGet 实例
	 * @param requestURL
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static HttpGet getDefaultHttpGetInstance(String requestURL){
		HttpGet hg = new HttpGet(requestURL);
    	hg.getParams().setParameter(AllClientPNames.CONNECTION_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
    	hg.getParams().setParameter(AllClientPNames.SO_TIMEOUT, DEFAULT_READ_TIMEOUT);
    	hg.getParams().setParameter(AllClientPNames.USER_AGENT, CHROME);
		return hg;
	}
	
	/**
	 * 返回默认的 HttpPost 实例
	 * @param requestURL
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static HttpPost getDefaultHttpPostInstance(String requestURL){
		HttpPost hp = new HttpPost(requestURL);
    	hp.getParams().setParameter(AllClientPNames.CONNECTION_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
    	hp.getParams().setParameter(AllClientPNames.SO_TIMEOUT, DEFAULT_READ_TIMEOUT);
    	hp.getParams().setParameter(AllClientPNames.USER_AGENT, CHROME);
		return hp;
	}
	
	/**
	 * 生产一个带代理的HttpGet请求
	 * @param requestURL
	 * @param proxyHost
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static HttpGet getProxyHttpGetInstance(String requestURL, HttpHost proxyHost){
		HttpGet get = getDefaultHttpGetInstance(requestURL);
		get.getParams().setParameter(AllClientPNames.DEFAULT_PROXY, proxyHost);
		return get;
	}
	
	/**
	 * 生产一个带代理的HttpGet请求
	 * @param requestURL
	 * @param proxyIP
	 * @param proxyPort
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static HttpGet getProxyHttpGetInstance(String requestURL, String proxyIP, int proxyPort){
		HttpGet get = getDefaultHttpGetInstance(requestURL);
		HttpHost proxyHost = new HttpHost(proxyIP, proxyPort);
		get.getParams().setParameter(AllClientPNames.DEFAULT_PROXY, proxyHost);
		return get;
	}
	
	/**
	 * 生产一个带代理的HttpPost请求
	 * @param requestURL
	 * @param proxyHost
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static HttpPost getProxyHttpPostInstance(String requestURL, HttpHost proxyHost){
		HttpPost get = getDefaultHttpPostInstance(requestURL);
		get.getParams().setParameter(AllClientPNames.DEFAULT_PROXY, proxyHost);
		return get;
	}
	
	/**
	 * 生产一个带代理的HttpPost请求
	 * @param requestURL
	 * @param proxyIP
	 * @param proxyPort
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static HttpPost getProxyHttpPostInstance(String requestURL, String proxyIP, int proxyPort){
		HttpPost get = getDefaultHttpPostInstance(requestURL);
		HttpHost proxyHost = new HttpHost(proxyIP, proxyPort);
		get.getParams().setParameter(AllClientPNames.DEFAULT_PROXY, proxyHost);
		return get;
	}

}
