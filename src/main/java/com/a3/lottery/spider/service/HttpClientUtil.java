package com.a3.lottery.spider.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP接口调用工具类
 *
 */
public class HttpClientUtil {

    private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String getSignature(HashMap<String, String> params, String secret) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"keyvalue"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        basestring.append(secret);
        for (Entry<String, String> param : entrys) {
            basestring.append(param.getKey()).append(param.getValue());
        }
        basestring.append(secret);

        // 使用MD5对待签名串求签
        return DigestUtils.md5Hex(basestring.toString()).toUpperCase();
    }
    
    public static void main(String[] args) throws Exception {
    	String url  = "https://77tj.org/api/tencent/onlineim";
		HttpClient httpclient = HttpClientFactory.getSSLHttpClientInstance();
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.setHeader("cookie","cf_chl_2=e4011f34c59ca31; cf_chl_prog=x12; cf_clearance=GQ02rkC2mMg2dLYDTZmdz5TDDMPIw5EFxNUf6cQabP0-1641712556-0-150; tc_cookie_name=61166e3acf614f9cb2463d394726557e");
		httpGet.setHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36");
		//httpGet.setHeader("device_info","Windows NT 10.0 Google Inc. Windows NT 10.0; Win64; x64");
		HttpResponse response = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
	 		.setCookieSpec(CookieSpecs.STANDARD_STRICT).build();// 设置请求和传输超时时间

		httpGet.setConfig(requestConfig);
		 response = httpclient.execute(httpGet);
		 if (response.getStatusLine().getStatusCode() != 200) {
		     response = httpclient.execute(httpGet);
		 }
		 String strFromResponse = HttpClientUtil.getStrFromResponse(response);
		 System.out.println(strFromResponse);
	}

    /**
     * 读返回数据
     * @param response
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static Map<String, String> covertHttpUrlMap(HttpResponse response) throws ParseException, IOException {
        String resposeMessage = EntityUtils.toString(response.getEntity());
        log.info("******报文接收内容*****：" + resposeMessage);
        return getHeader(resposeMessage);
    }

    /**
     * 支付HTTPS调用
     * @param smsUrl
     * @param sendMessageStr
     * @return
     */
    public static HttpResponse sendHttpsPost(String smsUrl, String sendMessageStr) {
        log.info("请求地址" + smsUrl + "*****报文发送内容*******：" + sendMessageStr);
        HttpClient httpclient = HttpClientFactory.getSSLHttpClientInstance();
        HttpPost httpPost = new HttpPost(smsUrl);
        HttpResponse response = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
                    .build();// 设置请求和传输超时时间
            // httpPost.addHeader("Content-type",
            // "application/x-www-form-urlencoded");
            httpPost.setEntity(new StringEntity(sendMessageStr, "UTF-8"));
            httpPost.setConfig(requestConfig);
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                log.info("****sendSms重新发起第一次HTTP请求*****************************");
                response = httpclient.execute(httpPost);
            }
            return response;
        } catch (Exception e) {
            log.debug("********发送HTTP请求----处理异常******************");
            e.printStackTrace();
            try {
                log.info("****sendSms重新发起第二次HTTP请求*****************************");
                response = httpclient.execute(httpPost);
            } catch (IOException e1) {
                log.debug("********发送HTTP请求最终失败----处理异常******************");
                e1.printStackTrace();
            }
        }
        return response;
    }

    /**
     * 支付HTTPS调用
     * @param smsUrl
     * @param sendMessageStr
     * @return
     */
    public static HttpResponse sendHttpsPost(String smsUrl, UrlEncodedFormEntity entity) {
        log.info("请求地址" + smsUrl + "*****报文发送内容*******：" + entity.toString());
        HttpClient httpclient = HttpClientFactory.getSSLHttpClientInstance();
        HttpPost httpPost = new HttpPost(smsUrl);
        HttpResponse response = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
                    .build();// 设置请求和传输超时时间
            // httpPost.addHeader("Content-type",
            // "application/x-www-form-urlencoded");
            httpPost.setEntity(entity);
            httpPost.setConfig(requestConfig);
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                log.info("****sendSms重新发起第一次HTTP请求*****************************");
                response = httpclient.execute(httpPost);
            }
            return response;
        } catch (Exception e) {
            log.debug("********发送HTTP请求----处理异常******************");
            e.printStackTrace();
            try {
                log.info("****sendSms重新发起第二次HTTP请求*****************************");
                response = httpclient.execute(httpPost);
            } catch (IOException e1) {
                log.debug("********发送HTTP请求最终失败----处理异常******************");
                e1.printStackTrace();
            }
        }
        return response;
    }

    /**
     * 发送消息GET方式
     * @param smsUrl
     * @param sendMessageStr
     *            http://zhcheng.iteye.com/blog/1292350
     * @return
     */
    public static HttpResponse sendHttpsGet(String smsUrl, String sendMessageStr) {
        log.info("*****请求地址报文发送内容*******：" + smsUrl + sendMessageStr);
        HttpClient httpclient = HttpClientFactory.getSSLHttpClientInstance();
        HttpGet get = new HttpGet(smsUrl + sendMessageStr);
        try {
            return httpclient.execute(get);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送消息GET方式
     * @param smsUrl
     * @param sendMessageStr
     *            http://zhcheng.iteye.com/blog/1292350
     * @return
     */
    public static HttpResponse sendSmsGet(String smsUrl, String sendMessageStr) {
        log.info("*****请求地址报文发送内容*******：" + smsUrl + sendMessageStr);
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(smsUrl + sendMessageStr);
        try {
            return httpClient.execute(get);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取域名URL
     * @param url
     * @return
     */
    public static String subUrl(String url) {
        String[] s = url.split("//");
        if (s.length > 1) {
            String[] urlParams = s[1].split("/");
            if (urlParams.length > 0) {
                return s[0].concat("//").concat(urlParams[0]);
            } else {
                return s[1];
            }
        } else {
            String[] urlParams = url.split("/");
            if (urlParams.length > 1) {
                return urlParams[0];
            } else {
                return url;
            }
        }
    }

    /**
     * 把URL串转换成MAP对象
     * @param url
     * @return
     */
    private static Map<String, String> getHeader(String url) {
        Map<String, String> map = new HashMap<String, String>();
        int start = url.indexOf("?");
        if (start >= 0) {
            String str = url.substring(start + 1);
            String[] paramsArr = str.split("&");
            for (String param : paramsArr) {
                String[] temp = param.split("=");
                if (temp.length == 2) {
                    map.put(temp[0], temp[1]);
                }
            }
        } else if (url.indexOf("&") > 0) {
            String[] paramsArr = url.split("&");
            for (String param : paramsArr) {
                String[] temp = param.split("=");
                if (temp.length == 2) {
                    map.put(temp[0], temp[1]);
                }
            }
        } else {
            if (url.indexOf(",") > 0) {
                String[] str = url.split(",");
                map.put("errCode", str[0]);
                map.put("errMsg", str[1]);
            }
        }
        return map;
    }
    
    public static String getStrFromResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
    	String str=null;
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        // 判断响应实体是否为空
        if (entity != null) {
        	str = EntityUtils.toString(entity, "UTF-8");
        }
        return str;
    }
}
