package com.a3.lottery.spider.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.pool.BasicConnFactory;
import org.apache.http.impl.pool.BasicConnPool;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.a3.lottery.domain.ApiPlusBean;
import com.a3.lottery.domain.ApiPlushDataBean;
import com.a3.lottery.domain.BcbCodeAM;
import com.a3.lottery.domain.BcbCodeItem;
import com.a3.lottery.domain.BitcoiNumAM;
import com.a3.lottery.domain.BitcoinNumItem;
import com.a3.lottery.domain.CaipiaoApiAM;
import com.a3.lottery.domain.CaipiaoApiData;
import com.a3.lottery.domain.CaipiaoApiDataAM;
import com.a3.lottery.domain.CaipiaoApiItem;
import com.a3.lottery.domain.CaipiaoApiItemVO;
import com.a3.lottery.domain.CaipiaoKongBean;
import com.a3.lottery.domain.CaipiaoapiCCAM;
import com.a3.lottery.domain.DjstTxffcAM;
import com.a3.lottery.domain.DjstTxffcItem;
import com.a3.lottery.domain.DrawResult;
import com.a3.lottery.domain.FengHuangItemVo;
import com.a3.lottery.domain.FengHuangVo;
import com.a3.lottery.domain.Ffc360GfcAM;
import com.a3.lottery.domain.Ffc360GfcItem;
import com.a3.lottery.domain.Ffc360scAM;
import com.a3.lottery.domain.FlbSScIssue;
import com.a3.lottery.domain.FlbSScIssueDetail;
import com.a3.lottery.domain.HeNeiGwItem;
import com.a3.lottery.domain.HeNeiGwVo;
import com.a3.lottery.domain.LotteryDrawRequestBean;
import com.a3.lottery.domain.LotteryDrawResponseBean;
import com.a3.lottery.domain.LotteryIssueResult;
import com.a3.lottery.domain.ManyCaiAM;
import com.a3.lottery.domain.NewKcwAM;
import com.a3.lottery.domain.NewKcwItem;
import com.a3.lottery.domain.NewKjapiAM;
import com.a3.lottery.domain.NewKjapiBuDataAM;
import com.a3.lottery.domain.NewKjapiDataAM;
import com.a3.lottery.domain.NewKjapiDataFreeAM;
import com.a3.lottery.domain.NewKjapiFreeAM;
import com.a3.lottery.domain.NewKjapiFreeItem;
import com.a3.lottery.domain.NewKjapiItemVo;
import com.a3.lottery.domain.TecentCjiujiuAM;
import com.a3.lottery.domain.TecentMmaIssue;
import com.a3.lottery.domain.Tencent6vcsIssue;
import com.a3.lottery.domain.TencentOnline;
import com.a3.lottery.domain.TxZjshSscAM;
import com.a3.lottery.domain.XingCaiItemVo;
import com.a3.lottery.domain.XingCaiVo;
import com.a3.lottery.domain.XycIssueAM;
import com.a3.lottery.domain.XycIssueInfo;
import com.a3.lottery.domain.XycIssueItem;
import com.a3.lottery.domain.YiLiuBaDataAM;
import com.a3.lottery.domain.YiLiuBaDataVo;
import com.a3.lottery.domain.config.DrawConfig;
import com.a3.lottery.domain.config.PlatformConfig;
import com.a3.lottery.spider.core.BossGroup;
import com.a3.lottery.util.ConvertToKenoUtil;
import com.a3.lottery.util.DateTransformer;
import com.a3.lottery.util.MD5;
import com.a3.lottery.util.SHA512Util;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Component
public class ApiPlusService {

    private static Logger logger = LoggerFactory.getLogger(ApiPlusService.class);

    @Autowired
    private JedisTemplate jedisTemplate;

    @Autowired
    private BossGroup bossGroup;
    @Autowired
    private HttpConnectionManager httpConnectionManager;

    private static Gson gson = new Gson();

    public static final String ROUTE_API_PLUS = "apiplus";

    private static final int QUEUE_MAX_SIZE = 10;

    private static final int CLIENT_SOCKET_TIMEOUT = 30000;

    private static final SimpleDateFormat SimpleParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SimpleParseDate = new SimpleDateFormat("yyyyMMdd");

    @Value("#{configProperties['domain.apiplus.host']}")
    private String ApiPlusHost = "";
    @Value("#{configProperties['domain.apiplus.token']}")
    private String ApiPlusToken = "";

    @Value("#{configProperties['domain.caipiaokong.host']}")
    private String CaipiaokongHost = "";
    @Value("#{configProperties['domain.caipiaokong.uid']}")
    private String CaipiaokongUid = "";
    @Value("#{configProperties['domain.caipiaokong.token']}")
    private String CaipiaokongToken = "";

    @Value("#{configProperties['domain.tencent77tjHost.host']}")
    private String Tencent77tjHost = "";

    @Value("#{configProperties['domain.tencent6vcs.host']}")
    private String Tencent6vcsHost = "";

    @Value("#{configProperties['domain.flbssc.host']}")
    private String flbSscHost = "";

    @Value("#{configProperties['domain.tecentMma.host']}")
    private String tecentMmaHost = "";

    @Value("#{configProperties['domain.tecentCjiujiu.host']}")
    private String tecentCjiujiuHost = "";
    @Value("#{configProperties['domain.tx30ssc.host']}")
    private String tx30sscHost = "";

    final BasicConnPool pool = new BasicConnPool(new BasicConnFactory());
    final HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

    final HttpProcessor httpproc = HttpProcessorBuilder.create().add(new RequestContent()).add(new RequestTargetHost())
            .add(new RequestConnControl()).add(new RequestUserAgent("Chrome")).add(new RequestExpectContinue(true))
            .build();

    /**
     * 线程池
     */
    public static ExecutorService writeFilePool;

    static {
        writeFilePool = Executors.newFixedThreadPool(15,
                new BasicThreadFactory.Builder().namingPattern("openCode-write-%d").build());
    }

    @PostConstruct
    public void init() {
        pool.setDefaultMaxPerRoute(50);
        pool.setMaxTotal(200);
    }

    public void getResponse(DrawConfig drawConfig, Queue<String> queue) {
        if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("caipiaokong")) {
            getFromCaipiaokong(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("caipiaokongFile")) {
            getFromCaipiaokongFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("77tj")) {
            getFrom77tj(drawConfig, queue);
        }else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("77tjFile")) {
            getFrom77tjFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("6vcs")) {
            getFrom6vcsTxssc(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("dsjt")) {
            getFromDsjtxssc(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("tecentMma")) {
            getFromTecentMa(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("txMmaFile")) {
            getFromTecentMaFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("cjiujiu")) {
            getFromTecentcjiujiu(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("dsjtTx")) {
            getFromDsjtTxffc(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("Txzjsh")) {
            getFromZjsh(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("kcwFile")) {
            getFromKcwFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("kcwZj")) {
            getFromKcwZj(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("kenoZjFile")) {
            getKenoZjFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("xingyuncai")) {
            getxingyuncai(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("xingyuncaiFile")) {
            getxingyuncaiFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("manycai")) {
            getManycai(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("manycaiFile")) {
            getManycaiFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("bitcoinum")) {
            getBitcoinumFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("bcbcaiFile")) {
            getBcbCaiFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("360ffscaiFile")) {
            get360ffscaiFileFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("360ffgfcaiFile")) {
            get360ffgfcaiFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("caipiaoapi")) {
            getCaipiaoapiFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("steamParseHtml")) {
            getSteamParseHtml(drawConfig, queue);
        }else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("heNeiGwFile")) {
        	heNeiGwFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("heNeiFHw")) {
        	heNeiFHw(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("xingcaiapi")) {
        	getXingCaiApi(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("newkjapi")) {
        	getNewkjapi(drawConfig, queue);
        }  else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("newkjapiFile")) {
        	getNewkjapiFile(drawConfig, queue);
        }  else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("newkjapiFreeFile")) {
        	getNewkjapiFreeFile(drawConfig, queue);
        }else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("newKcw")) {
        	getFromNewKcw(drawConfig, queue);
        }  else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("newKcwFile")) {
        	getFromNewKcwFile(drawConfig, queue);
        } else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("caipiaoapiCCFile")) {
        	getCaipiaoapiCCFile(drawConfig, queue);
        }else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("heneiQiqu")) {
        	getHeneiQiquApi(drawConfig, queue);
        }else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("heneiQiquFile")) {
        	getHeneiQiquFile(drawConfig, queue);
        }else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("168kjApi")) {
        	get168KjApi(drawConfig, queue);
        }else if (StringUtils.isNotEmpty(drawConfig.getRoute()) && drawConfig.getRoute().equals("168kjApiFile")) {
        	getHeneiQiquFile(drawConfig, queue);
        }else {
        	get168KjApiFile(drawConfig, queue);
        }
    }
    
    private void get168KjApi(DrawConfig drawConfig, Queue<String> queue) {
		String url="/pks/getLotteryPksInfo.do?lotCode="+drawConfig.getToCode();
		String spiderIp = drawConfig.getSpiderIp();
		
		if(StringUtils.isBlank(spiderIp)) {
			spiderIp="https://api.api68.com";
		}
		
		String response = get(spiderIp, url);
	    logger.info("{} ,url:{} ,get168KjApi spider:{}" ,drawConfig.getToCode(),url,response);
	    
	    YiLiuBaDataAM yiLiuBaDataAM= null;
	    
	    if(StringUtils.isBlank(response)){
	    	return;
	    }
	    try {
	    	yiLiuBaDataAM = gson.fromJson(response, new TypeToken<YiLiuBaDataAM>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (yiLiuBaDataAM == null || yiLiuBaDataAM.getResult()==null) {
            return;
        }
        YiLiuBaDataVo yiLiuBaDataVo=yiLiuBaDataAM.getResult();
        
        if (yiLiuBaDataVo == null || yiLiuBaDataVo.getData()==null) {
            return;
        }
        NewKjapiFreeItem newKjapiFreeItem=yiLiuBaDataVo.getData();
        if (newKjapiFreeItem == null ) {
            return;
        }
        String issueNoStr =newKjapiFreeItem.getPreDrawIssue();
        if (queue.contains(issueNoStr)) {
	    	return;
	    }
        Date onlineDateTime = null;
        String onlineStr =newKjapiFreeItem.getPreDrawTime();
        try {
            onlineDateTime = SimpleParse.parse(String.valueOf(onlineStr));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        String code=newKjapiFreeItem.getPreDrawCode();
        if(drawConfig.getLotteryCode().contains("LHECXJW")) {
        	code = formatLow10OpenCode(code);
        }
        
        notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issueNoStr, newKjapiFreeItem.getPreDrawCode(),
        		onlineStr, String.valueOf(onlineDateTime.getTime()/1000), null);
        queue.add(issueNoStr);
       
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
	}
	
	private void get168KjApiFile(DrawConfig drawConfig, Queue<String> queue) {
		String url="/pks/getLotteryPksInfo.do?lotCode="+drawConfig.getToCode();
		String spiderIp = drawConfig.getSpiderIp();
		
		if(StringUtils.isBlank(spiderIp)) {
			spiderIp="https://api.api68.com";
		}
		
		String response = get(spiderIp, url);
	    logger.info("{} ,url:{} ,get168KjApi spider:{}" ,drawConfig.getToCode(),url,response);
	    
	    YiLiuBaDataAM yiLiuBaDataAM= null;
	    
	    if(StringUtils.isBlank(response)){
	    	return;
	    }
	    try {
	    	yiLiuBaDataAM = gson.fromJson(response, new TypeToken<YiLiuBaDataAM>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (yiLiuBaDataAM == null || yiLiuBaDataAM.getResult()==null) {
            return;
        }
        YiLiuBaDataVo yiLiuBaDataVo=yiLiuBaDataAM.getResult();
        
        if (yiLiuBaDataVo == null || yiLiuBaDataVo.getData()==null) {
            return;
        }
        NewKjapiFreeItem newKjapiFreeItem=yiLiuBaDataVo.getData();
        if (newKjapiFreeItem == null ) {
            return;
        }
        String issueNoStr =newKjapiFreeItem.getPreDrawIssue();
        if (queue.contains(issueNoStr)) {
	    	return;
	    }
        Date onlineDateTime = null;
        String onlineStr =newKjapiFreeItem.getPreDrawTime();
        try {
            onlineDateTime = SimpleParse.parse(String.valueOf(onlineStr));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        String code=newKjapiFreeItem.getPreDrawCode();
        if(drawConfig.getLotteryCode().contains("LHECXJW")) {
        	code = formatLow10OpenCode(code);
        }
        
        LotteryIssueResult issueResult = new LotteryIssueResult();
        issueResult.setCode(code);
        issueResult.setIssue(issueNoStr);
        issueResult.setTime(onlineStr);
        issueResult.setOpentimestamp(String.valueOf(onlineDateTime.getTime()));
        writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
        queue.add(issueNoStr);
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
	}
    
    private void getHeneiQiquApi(DrawConfig drawConfig, Queue<String> queue) {
    	String url="https://api.fhlmapi.com/api/fhlm/HN60/5";
    	String heNeiRes = httpConnectionManager.get(url);
    	
    	logger.info("HEINEI60SSC ,url:{} ,getHeneiQiquApi spider:{}" ,url,heNeiRes);
    	
    	url="https://77tj001.org/api/tencent/onlineim";
    	String qiQuRes = httpConnectionManager.get(url);
    	logger.info("QIQU60SSC ,url:{} ,getHeneiQiquApi spider:{}" ,url,qiQuRes);
    	
        ArrayList<TencentOnline> qiQuIssues = null;
        if (!StringUtils.isEmpty(qiQuRes)) {
            try {
            	qiQuIssues = gson.fromJson(qiQuRes, new TypeToken<ArrayList<TencentOnline>>() {
                }.getType());
            } catch (Exception e) {
            }
        }
        if (qiQuIssues == null) {
            return;
        }
        if (CollectionUtils.isEmpty(qiQuIssues)) {
            return;
        }
        
        FengHuangVo fengHuangVo=null;
	    
	    if(StringUtils.isBlank(heNeiRes)){
	    	return;
	    }
	    try {
	    	fengHuangVo = gson.fromJson(heNeiRes, new TypeToken<FengHuangVo>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fengHuangVo == null || fengHuangVo.getData()==null) {
            return;
        }
        List<FengHuangItemVo> heNeList= fengHuangVo.getData();
        
        if(CollectionUtils.isEmpty(heNeList)){
        	return;
        }
        Map<String,FengHuangItemVo> heNeiMap = new HashMap<>();
        for(FengHuangItemVo heNeiGwItem: heNeList){
        	String issueNoStr = heNeiGwItem.getIssue();
        	heNeiMap.put(issueNoStr, heNeiGwItem);
        }

        for (TencentOnline entry : qiQuIssues) {
        	if(queue.contains(entry.getOnlinetime())) {
            	continue;
            }
            Date onlineDateTime = null;
            try {
                onlineDateTime = SimpleParse.parse(entry.getOnlinetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String issue = buidlTencentIssue(entry.getOnlinetime(), onlineDateTime);
            
            if(!heNeiMap.containsKey(issue)) {
            	continue;
            }
            TencentOnline tencentOnline = entry;
            String QiquNumber = buildTencentNumber(tencentOnline.getOnlinenumber());
            FengHuangItemVo fengHuangItemVo = heNeiMap.get(issue);
            String heiNeiNumber= formatOpenCode(fengHuangItemVo.getCode());
            
            String [] numArr = new String[5];
            String[] qiQuNumberArr =QiquNumber.split(",");
            String[] heNeiNumberArr =heiNeiNumber.split(",");
            
            for(int i=0;i<qiQuNumberArr.length;i++) {
            	int a = Integer.parseInt(StringUtils.trim(qiQuNumberArr[i]));
            	int b = Integer.parseInt(StringUtils.trim(heNeiNumberArr[i]));
            	
            	int c = (a+b)%10;
            	numArr[i]=String.valueOf(c);
            }
            String code = StringUtils.join(numArr, ",");
            String onlineTime = tencentOnline.getOnlinetime();
            String timestamp = "";
            try {
                timestamp = SimpleParse.parse(onlineTime).getTime() / 1000 + "";
            } catch (ParseException e) {
                e.printStackTrace();
            }
            logger.info("getHeneiQiquApi shengcheng :{} ,{}" ,issue,code);
            notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, code,
            		onlineTime, timestamp, null);
            queue.add(entry.getOnlinetime());
        }

        while (queue.size() > 10) {
            queue.poll();
        }
	}
    
    private void getHeneiQiquFile(DrawConfig drawConfig, Queue<String> queue) {
    	String url="https://api.fhlmapi.com/api/fhlm/HN60/5";
    	String heNeiRes = httpConnectionManager.get(url);
    	
    	logger.info("HEINEI60SSC ,url:{} ,getHeneiQiquApi spider:{}" ,url,heNeiRes);
    	
    	url="https://77tj001.org/api/tencent/onlineim";
    	String qiQuRes = httpConnectionManager.get(url);
    	logger.info("QIQU60SSC ,url:{} ,getHeneiQiquApi spider:{}" ,url,qiQuRes);
    	
        ArrayList<TencentOnline> qiQuIssues = null;
        if (!StringUtils.isEmpty(qiQuRes)) {
            try {
            	qiQuIssues = gson.fromJson(qiQuRes, new TypeToken<ArrayList<TencentOnline>>() {
                }.getType());
            } catch (Exception e) {
            }
        }
        if (qiQuIssues == null) {
            return;
        }
        if (CollectionUtils.isEmpty(qiQuIssues)) {
            return;
        }
        
        FengHuangVo fengHuangVo=null;
	    
	    if(StringUtils.isBlank(heNeiRes)){
	    	return;
	    }
	    try {
	    	fengHuangVo = gson.fromJson(heNeiRes, new TypeToken<FengHuangVo>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fengHuangVo == null || fengHuangVo.getData()==null) {
            return;
        }
        List<FengHuangItemVo> heNeList= fengHuangVo.getData();
        
        if(CollectionUtils.isEmpty(heNeList)){
        	return;
        }
        Map<String,FengHuangItemVo> heNeiMap = new HashMap<>();
        for(FengHuangItemVo heNeiGwItem: heNeList){
        	String issueNoStr = heNeiGwItem.getIssue();
        	heNeiMap.put(issueNoStr, heNeiGwItem);
        }

        for (TencentOnline entry : qiQuIssues) {
        	if(queue.contains(entry.getOnlinetime())) {
            	continue;
            }
            Date onlineDateTime = null;
            try {
                onlineDateTime = SimpleParse.parse(entry.getOnlinetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String issue = buidlTencentIssue(entry.getOnlinetime(), onlineDateTime);
            
            if(!heNeiMap.containsKey(issue)) {
            	continue;
            }
            TencentOnline tencentOnline = entry;
            String QiquNumber = buildTencentNumber(tencentOnline.getOnlinenumber());
            FengHuangItemVo fengHuangItemVo = heNeiMap.get(issue);
            String heiNeiNumber= formatOpenCode(fengHuangItemVo.getCode());
            
            String [] numArr = new String[5];
            String[] qiQuNumberArr =QiquNumber.split(",");
            String[] heNeiNumberArr =heiNeiNumber.split(",");
            
            for(int i=0;i<qiQuNumberArr.length;i++) {
            	int a = Integer.parseInt(StringUtils.trim(qiQuNumberArr[i]));
            	int b = Integer.parseInt(StringUtils.trim(heNeiNumberArr[i]));
            	
            	int c = (a+b)%10;
            	numArr[i]=String.valueOf(c);
            }
            String code = StringUtils.join(numArr, ",");
            String onlineTime = tencentOnline.getOnlinetime();
//            String timestamp = "";
//            try {
//                timestamp = SimpleParse.parse(onlineTime).getTime() / 1000 + "";
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            logger.info("getHeneiQiquApi shengcheng :{} ,{}" ,issue,code);
            queue.add(entry.getOnlinetime());
            LotteryIssueResult issueResult = new LotteryIssueResult();
            issueResult.setCode(code);
            issueResult.setIssue(issue);
            issueResult.setTime(onlineTime);
            issueResult.setOpentimestamp(String.valueOf(onlineDateTime.getTime()));
            writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
        }
        while (queue.size() > 10) {
            queue.poll();
        }
	}

	private void getFromNewKcw(DrawConfig drawConfig, Queue<String> queue) {
    	//https://kaicai.net/api/trial/drawResult?code=jx11x5&format=json&rows=5
    	String spiderIp = drawConfig.getSpiderIp();
        String url = spiderIp+"/api/trial/drawResult?code="+drawConfig.getToCode()+"&format=json&rows=5";

    	String response = httpConnectionManager.get(url);
        logger.info("getFromNewKcw lottery:{},json:{}", drawConfig.getLotteryCode(), response);
        NewKcwAM bean = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                bean = gson.fromJson(response, NewKcwAM.class);
            } catch (Exception e) {
            }
        }

        if (bean == null) {
            return;
        }

        List<NewKcwItem> issues = bean.getData();
        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (NewKcwItem issueBean : issues) {
        	 String issue = issueBean.getIssue();
             if(queue.contains(issue)) {
             	continue;
             }
           
             String formatIssue = issue;
             if (drawConfig.getConverter() != null) {
             	formatIssue = drawConfig.getConverter().convert(issue);
 	        }
             Date onlineDateTime = null;
             String onlineStr =issueBean.getDrawTime();
             try {
                 onlineDateTime = SimpleParse.parse(String.valueOf(onlineStr));
             } catch (ParseException e) {
                 logger.error(e.getMessage(), e);
             }
             String lotteryNumber =issueBean.getDrawResult();
	         notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, formatIssue, lotteryNumber,
	        		 onlineStr, String.valueOf(onlineDateTime.getTime() / 1000), null);
	         queue.add(issue);
        }

        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
    }
    
	private void getCaipiaoapiCCFile(DrawConfig drawConfig, Queue<String> queue) {
		//String url="/hall/nodeService/api_request?uid=56&time=1613299485&gamekey="+drawConfig.getToCode()+"&api=apiGameList&md5=5d491bfde2f9a8dc8fed214f15a7ba1d";
		String spiderIp = drawConfig.getSpiderIp();
		
		if(StringUtils.isBlank(spiderIp)) {
			logger.error("{} 未配置url",drawConfig.getLotteryCode());
			return;
		}
		
		String response = httpConnectionManager.get(spiderIp);
	    logger.info("{} ,url:{} ,getCaipiaoapiCCFile spider:{}" ,drawConfig.getToCode(),spiderIp,response);
	    
	    CaipiaoapiCCAM newKjapiAM= null;
	    
	    if(StringUtils.isBlank(response)){
	    	return;
	    }
	    try {
	    	newKjapiAM = gson.fromJson(response, new TypeToken<CaipiaoapiCCAM>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newKjapiAM == null || newKjapiAM.getResult()==null) {
            return;
        }
        CaipiaoApiDataAM newKjapiDataAM=newKjapiAM.getResult();
        
        if (newKjapiDataAM == null || newKjapiDataAM.getData()==null) {
            return;
        }
        
        List<CaipiaoApiItemVO> list= newKjapiDataAM.getData();
        
        if(CollectionUtils.isEmpty(list)){
        	return;
        }
        
        for(CaipiaoApiItemVO newKjapiItemVo: list){
            String issue = newKjapiItemVo.getGid();
            if(queue.contains(issue)) {
            	continue;
            }
            queue.add(issue);
            String formatIssue = issue;
            if (drawConfig.getConverter() != null) {
            	formatIssue = drawConfig.getConverter().convert(issue);
	        }
            Date onlineDateTime = null;
            String onlineStr = newKjapiItemVo.getTime();
            try {
                onlineDateTime = SimpleParse.parse(String.valueOf(onlineStr));
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
            
            String lotteryNumber = newKjapiItemVo.getAward();
            LotteryIssueResult issueResult = new LotteryIssueResult();
            issueResult.setCode(lotteryNumber);
            issueResult.setIssue(formatIssue);
            issueResult.setTime(onlineStr);
            issueResult.setOpentimestamp(String.valueOf(onlineDateTime.getTime()));
            writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
	     }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
	}



	private void getNewkjapi(DrawConfig drawConfig, Queue<String> queue) {
		String url="/hall/nodeService/api_request?uid=56&time=1613299485&gamekey="+drawConfig.getToCode()+"&api=apiGameList&md5=5d491bfde2f9a8dc8fed214f15a7ba1d";
		String spiderIp = drawConfig.getSpiderIp();
		
		if(StringUtils.isBlank(spiderIp)) {
			spiderIp="http://www.kjapi.com";
		}
		
		String response = get(spiderIp, url);
	    logger.info("{} ,url:{} ,getNewkjapi spider:{}" ,drawConfig.getToCode(),url,response);
	    
	    NewKjapiAM newKjapiAM= null;
	    
	    if(StringUtils.isBlank(response)){
	    	return;
	    }
	    try {
	    	newKjapiAM = gson.fromJson(response, new TypeToken<NewKjapiAM>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newKjapiAM == null || newKjapiAM.getResult()==null) {
            return;
        }
        NewKjapiDataAM newKjapiDataAM=newKjapiAM.getResult();
        
        if (newKjapiDataAM == null || newKjapiDataAM.getData()==null) {
            return;
        }
        NewKjapiBuDataAM newKjapiBuDataAM=newKjapiDataAM.getData();
        if (newKjapiBuDataAM == null || newKjapiBuDataAM.getData()==null|| CollectionUtils.isEmpty(newKjapiBuDataAM.getData())) {
            return;
        }
        
        List<NewKjapiItemVo> list= newKjapiBuDataAM.getData();
        
        if(CollectionUtils.isEmpty(list)){
        	return;
        }
        
        for(NewKjapiItemVo newKjapiItemVo: list){
        	String issueNoStr =newKjapiItemVo.getQh();
        	
    	    if (queue.contains(issueNoStr)) {
    	    	continue;
    	    }
    	   
        	
	  	    String timestamp = newKjapiItemVo.getCreate_date();
	         notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issueNoStr, newKjapiItemVo.getResults(),
	        		 newKjapiItemVo.getStime(), timestamp, null);
	         queue.add(issueNoStr);
	     }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
	}
	
	private void getNewkjapiFile(DrawConfig drawConfig, Queue<String> queue) {
		//String url="/hall/nodeService/api_request?uid=56&time=1613299485&gamekey="+drawConfig.getToCode()+"&api=apiGameList&md5=5d491bfde2f9a8dc8fed214f15a7ba1d";
		String spiderIp = drawConfig.getSpiderIp();
		
		if(StringUtils.isBlank(spiderIp)) {
			logger.error("{} 未配置url",drawConfig.getLotteryCode());
			return;
		}
		
		String response = httpConnectionManager.get(spiderIp);
	    logger.info("{} ,url:{} ,getNewkjapi spider:{}" ,drawConfig.getToCode(),spiderIp,response);
	    
	    NewKjapiAM newKjapiAM= null;
	    
	    if(StringUtils.isBlank(response)){
	    	return;
	    }
	    try {
	    	newKjapiAM = gson.fromJson(response, new TypeToken<NewKjapiAM>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newKjapiAM == null || newKjapiAM.getResult()==null) {
            return;
        }
        NewKjapiDataAM newKjapiDataAM=newKjapiAM.getResult();
        
        if (newKjapiDataAM == null || newKjapiDataAM.getData()==null) {
            return;
        }
        NewKjapiBuDataAM newKjapiBuDataAM=newKjapiDataAM.getData();
        if (newKjapiBuDataAM == null || newKjapiBuDataAM.getData()==null|| CollectionUtils.isEmpty(newKjapiBuDataAM.getData())) {
            return;
        }
        
        List<NewKjapiItemVo> list= newKjapiBuDataAM.getData();
        
        if(CollectionUtils.isEmpty(list)){
        	return;
        }
        
        for(NewKjapiItemVo newKjapiItemVo: list){
            String issue = newKjapiItemVo.getQh();
            if (drawConfig.getConverter() != null) {
                issue = drawConfig.getConverter().convert(issue);
	        }
            if (!queue.contains(issue)) {
                String lotteryNumber = newKjapiItemVo.getResults();

                String timestamp = newKjapiItemVo.getCreate_date();
                LotteryIssueResult issueResult = new LotteryIssueResult();
                issueResult.setCode(lotteryNumber);
                issueResult.setIssue(issue);
                issueResult.setTime(newKjapiItemVo.getStime());
                issueResult.setOpentimestamp(timestamp);
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
                queue.add(issue);
            }
	     }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
	}
	
	private void getNewkjapiFreeFile(DrawConfig drawConfig, Queue<String> queue) {
		//String url="/hall/nodeService/api_request?uid=56&time=1613299485&gamekey="+drawConfig.getToCode()+"&api=apiGameList&md5=5d491bfde2f9a8dc8fed214f15a7ba1d";
		String spiderIp = drawConfig.getSpiderIp();
		
		if(StringUtils.isBlank(spiderIp)) {
			logger.error("{} 未配置url",drawConfig.getLotteryCode());
			return;
		}
		
		String response = httpConnectionManager.get(spiderIp);
	    logger.info("{} ,url:{} ,getNewkjapiFreeFile spider:{}" ,drawConfig.getToCode(),spiderIp,response);
	    
	    NewKjapiFreeAM newKjapiAM= null;
	    
	    if(StringUtils.isBlank(response)){
	    	return;
	    }
	    try {
	    	newKjapiAM = gson.fromJson(response, new TypeToken<NewKjapiFreeAM>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newKjapiAM == null || newKjapiAM.getResult()==null) {
            return;
        }
        NewKjapiDataFreeAM newKjapiDataAM=newKjapiAM.getResult();
        
        if (newKjapiDataAM == null || newKjapiDataAM.getData()==null) {
            return;
        }
        
        List<NewKjapiFreeItem> list= newKjapiDataAM.getData();
        
        if(CollectionUtils.isEmpty(list)){
        	return;
        }
        
        for(NewKjapiFreeItem newKjapiItemVo: list){
            String issue = newKjapiItemVo.getPreDrawIssue();
            if(queue.contains(issue)) {
            	continue;
            }
            queue.add(issue);
            String formatIssue = issue;
            if (drawConfig.getConverter() != null) {
            	formatIssue = drawConfig.getConverter().convert(issue);
	        }
            Date onlineDateTime = null;
            String onlineStr = newKjapiItemVo.getPreDrawTime();
            try {
                onlineDateTime = SimpleParse.parse(String.valueOf(onlineStr));
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
            
            String lotteryNumber = newKjapiItemVo.getPreDrawCode();
            LotteryIssueResult issueResult = new LotteryIssueResult();
            issueResult.setCode(lotteryNumber);
            issueResult.setIssue(formatIssue);
            issueResult.setTime(onlineStr);
            issueResult.setOpentimestamp(String.valueOf(onlineDateTime.getTime()));
            writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
	     }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
	}
	
	 private void getFromNewKcwFile(DrawConfig drawConfig, Queue<String> queue) {
	    	//https://kaicai.net/api/trial/drawResult?code=jx11x5&format=json&rows=5
	    	String spiderIp = drawConfig.getSpiderIp();
	        String url = spiderIp+"/api/trial/drawResult?code="+drawConfig.getToCode()+"&format=json&rows=5";

	    	String response = httpConnectionManager.get(url);
	        logger.info("getFromNewKcwFile lottery:{},json:{}", drawConfig.getLotteryCode(), response);
	        NewKcwAM bean = null;
	        if (!StringUtils.isEmpty(response)) {
	            try {
	                bean = gson.fromJson(response, NewKcwAM.class);
	            } catch (Exception e) {
	            }
	        }

	        if (bean == null) {
	            return;
	        }

	        List<NewKcwItem> issues = bean.getData();
	        if (CollectionUtils.isEmpty(issues)) {
	            return;
	        }

	        for (NewKcwItem issueBean : issues) {
	        	 String issue = issueBean.getIssue();
	             if(queue.contains(issue)) {
	             	continue;
	             }
	             queue.add(issue);
	             String formatIssue = issue;
	             if (drawConfig.getConverter() != null) {
	             	formatIssue = drawConfig.getConverter().convert(issue);
	 	        }
	             Date onlineDateTime = null;
	             String onlineStr =issueBean.getDrawTime();
	             try {
	                 onlineDateTime = SimpleParse.parse(String.valueOf(onlineStr));
	             } catch (ParseException e) {
	                 logger.error(e.getMessage(), e);
	             }
	             
	             String lotteryNumber =issueBean.getDrawResult();
	             LotteryIssueResult issueResult = new LotteryIssueResult();
	             issueResult.setCode(lotteryNumber);
	             issueResult.setIssue(formatIssue);
	             issueResult.setTime(onlineStr);
	             issueResult.setOpentimestamp(String.valueOf(onlineDateTime.getTime()));
	             writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
	        }

	        while (queue.size() > QUEUE_MAX_SIZE) {
	            queue.poll();
	        }
	    }
	
	

	private void getFrom77tjFile(DrawConfig drawConfig, Queue<String> queue) {
		String url = Tencent77tjHost+"/api/tencent/onlineim";
        
        
        String response = httpConnectionManager.get(url);

	        //String response = get(Tencent77tjHost, url);
	        logger.info("NEW QIQU spider:" + response);
	        ArrayList<TencentOnline> issues = null;
	        if (!StringUtils.isEmpty(response)) {
	            try {
	                issues = gson.fromJson(response, new TypeToken<ArrayList<TencentOnline>>() {
	                }.getType());
	            } catch (Exception e) {
	            }
	        }

	        if (issues == null) {
	            return;
	        }

	        if (CollectionUtils.isEmpty(issues)) {
	            return;
	        }

	        for (TencentOnline entry : issues) {
	        	if(queue.contains(entry.getOnlinetime())) {
	            	continue;
	            }
	            queue.add(entry.getOnlinetime());
	            Date onlineDateTime = null;
	            try {
	                onlineDateTime = SimpleParse.parse(entry.getOnlinetime());
	            } catch (ParseException e) {
	                e.printStackTrace();
	            }

	            String issue = buidlTencentIssue(entry.getOnlinetime(), onlineDateTime);
	            
                TencentOnline tencentOnline = entry;
                String onlineTime = tencentOnline.getOnlinetime();
                String lotteryNumber = buildTencentNumber(tencentOnline.getOnlinenumber());

                String timestamp = "";
                try {
                    timestamp = SimpleParse.parse(onlineTime).getTime() / 1000 + "";
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                LotteryIssueResult issueResult = new LotteryIssueResult();
                issueResult.setCode(lotteryNumber);
                issueResult.setIssue(issue);
                issueResult.setTime(onlineTime);
                issueResult.setOpentimestamp(timestamp);
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
	        }

	        while (queue.size() > 10) {
	            queue.poll();
	        }
	}

	private void getXingCaiApi(DrawConfig drawConfig, Queue<String> queue) {
		String spiderIp = drawConfig.getSpiderIp();
		String url="/api/2731740ce32fb6662cf4b4417928901c/"+drawConfig.getToCode()+"/5-json";
		
		if(StringUtils.isBlank(spiderIp)) {
			spiderIp="http://a.apilottery.com";
		}
		
		String response = get(spiderIp, url);
	    logger.info("{} ,url:{} ,getXingCaiApi spider:{}" ,drawConfig.getToCode(),url,response);
	    
	    XingCaiVo xingCaiVo= null;
	    
	    if(StringUtils.isBlank(response)){
	    	return;
	    }
	    try {
	    	xingCaiVo = gson.fromJson(response, new TypeToken<XingCaiVo>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (xingCaiVo == null || xingCaiVo.getData()==null) {
            return;
        }
        List<XingCaiItemVo> list= xingCaiVo.getData();
        
        if(CollectionUtils.isEmpty(list)){
        	return;
        }
        
        for(XingCaiItemVo xingCaiItemVo: list){
        	String issueNoStr =xingCaiItemVo.getExpect();
        	
    	    if (queue.contains(issueNoStr)) {
    	    	continue;
    	    }
    	   
        	
	  	    String timestamp = xingCaiItemVo.getOpentimestamp();
	         notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issueNoStr, xingCaiItemVo.getOpencode(),
	        		 xingCaiItemVo.getOpentime(), timestamp, null);
	         queue.add(issueNoStr);
	     }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
	}

	private void heNeiFHw(DrawConfig drawConfig, Queue<String> queue) {
		String url="https://api.fhlmapi.com/api/fhlm/HN60/5";
		
		if(drawConfig.getLotteryCode().equals("HENEI300SSC")){
			 url ="https://api.fhlmapi.com/api/fhlm/HN300/5";
		}
		String response = httpConnectionManager.get(url);
		logger.info("{} ,url:{} ,getheNeiGwFile spider:" ,drawConfig.getToCode(),url,response);
		
		
		FengHuangVo fengHuangVo=null;
	    
	    if(StringUtils.isBlank(response)){
	    	return;
	    }
	    try {
	    	fengHuangVo = gson.fromJson(response, new TypeToken<FengHuangVo>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fengHuangVo == null || fengHuangVo.getData()==null) {
            return;
        }
        List<FengHuangItemVo> list= fengHuangVo.getData();
        
        if(CollectionUtils.isEmpty(list)){
        	return;
        }
        
        for(FengHuangItemVo heNeiGwItem: list){
        	String issueNoStr = heNeiGwItem.getIssue();
        	
    	    if (queue.contains(issueNoStr)) {
    	    	continue;
    	    }
    	   
        	String [] issueNoArr = issueNoStr.split("-");
        	int issueIndex = Integer.parseInt(issueNoArr[1]);
        	
    	    String formatIssueNo=issueNoArr[0]+"-"+  String.format("%04d", issueIndex);
  	        
  	        if(drawConfig.getLotteryCode().equals("HENEI300SSC")){
  	        	formatIssueNo=issueNoArr[0]+"-"+  String.format("%03d", issueIndex);
  			}
	  	    String timestamp = heNeiGwItem.getEnd_time();
	        try {
	            timestamp = SimpleParse.parse(heNeiGwItem.getEnd_time()).getTime() / 1000 + "";
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        String code  = formatOpenCode(heNeiGwItem.getCode());
	         notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, formatIssueNo, code,
	        		 heNeiGwItem.getEnd_time(), timestamp, null);
	         queue.add(issueNoStr);
	     }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
        
	}

	private void heNeiGwFile(DrawConfig drawConfig, Queue<String> queue) {
		

		String url ="/others/draw_ffc.php";
		
		if(drawConfig.getLotteryCode().equals("HENEI300SSC")){
			 url ="http://vietlotto.org/others/draw.php";
		}
		url+="?v=0."+  ((int) (Math.random() * 9000000) + 1000000);
		//String response = httpConnectionManager.get(url);
		String response = get("vietlotto.org", url);
	    logger.info("{} ,url:{} ,getheNeiGwFile spider:" ,drawConfig.getToCode(),url,response);
	    HeNeiGwVo heNeiGwVo=null;
	    
	    if(StringUtils.isBlank(response)){
	    	return;
	    }
	    try {
	    	heNeiGwVo = gson.fromJson(response, new TypeToken<HeNeiGwVo>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (heNeiGwVo == null || heNeiGwVo.getHistory()==null) {
            return;
        }
        List<HeNeiGwItem> list= heNeiGwVo.getHistory();
        
        if(CollectionUtils.isEmpty(list)){
        	return;
        }
        for(HeNeiGwItem heNeiGwItem: list){
        	String issueNoStr = heNeiGwItem.getNum();
        	
    	    if (queue.contains(issueNoStr)) {
    	    	continue;
    	    }
    	    queue.add(issueNoStr);
        	String [] issueNoArr = issueNoStr.split("-");
        	int issueIndex = Integer.parseInt(issueNoArr[1]);
        	String dateStr = issueNoArr[0];
        	Date DateT = null;
        	try {
        		DateT = SimpleParseDate.parse(dateStr);
			} catch (ParseException e) {
				logger.error(e.getMessage(),e);
				continue;
			}
        	if(DateT==null){
        		continue;
        	}
        	
    	    Calendar c = Calendar.getInstance();
    	    c.setTime(DateT);
	        c.set(Calendar.HOUR_OF_DAY, 0);
	        c.set(Calendar.MINUTE, 0);
	        c.set(Calendar.SECOND, 0);
	        c.set(Calendar.MILLISECOND, 0);
	        
	        int disSecond = issueIndex*60;
        	
	        if(drawConfig.getLotteryCode().equals("HENEI300SSC")){
	        	disSecond=issueIndex*60*5;
			}
	        c.add(Calendar.SECOND, disSecond);
	        Date openTime = c.getTime();
	        String formatIssueNo=issueNoArr[0]+"-"+  String.format("%04d", issueIndex);
	        
	        if(drawConfig.getLotteryCode().equals("HENEI300SSC")){
	        	formatIssueNo=issueNoArr[0]+  String.format("%03d", issueIndex);
			}
	        LotteryIssueResult issueResult = new LotteryIssueResult();
            issueResult.setCode(heNeiGwItem.getResult());
            issueResult.setIssue(formatIssueNo);
            issueResult.setTime(SimpleParse.format(openTime));
            issueResult.setOpentimestamp(String.valueOf(openTime.getTime()));
            writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
        }
        while (queue.size() > 10) {
            queue.poll();
        }
	}

	private void getSteamParseHtml(DrawConfig drawConfig, Queue<String> queue) {
        try {
            String url = "https://store.steampowered.com/stats/";
            Document document = Jsoup.parse(new URL("https://store.steampowered.com/stats/"), 10000);

            // System.out.println(document.html());

            // 获取title的内容
            // Element title = document.getElementsByTag("title").first();
            // System.out.println(title.text());
            // Elements elementsContainingText = document.getElementsContainingText("Updated:");
            Elements elementsByClass = document.getElementsByClass("pageheader");
            // String text = elementsByClass.first().text();
            // System.out.println(text);
            Elements spanEle = elementsByClass.first().getElementsByTag("span");
            String timeText = spanEle.first().text();
            if (StringUtils.isBlank(timeText)) {
                return;
            }
            timeText = timeText.replaceAll("Updated: ", "");
            System.out.println(timeText);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy @ hh:mmaa", Locale.ENGLISH);
            // String ss = "17 February, 2020 @ 8:26am";
            Date parseDate = sdf.parse(timeText);
            // System.out.println(parseDate);
            String nowTimeFromSteamTime = DateTransformer.getNowTimeFromSteamTime(parseDate);
            // System.out.println(nowTimeFromSteamTime);

            // 获取点击数
            Element hitEle = document.getElementsByClass("statsTopHi").first();
            // System.out.println(hitEle.text());
            String str = nowTimeFromSteamTime + "@" + hitEle.text();
            logger.info("steam抓取:{}", str);
            if (!queue.contains(nowTimeFromSteamTime)) {
                queue.add(nowTimeFromSteamTime);
                String path = "/data/steamIssue.txt";
                // FileUtils.writeStringToFile(new File(path), str, true);
                List<String> list = new ArrayList<>();
                list.add(str);
                FileUtils.writeLines(new File(path), list, true);
            }

            while (queue.size() > QUEUE_MAX_SIZE) {
                queue.poll();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getCaipiaoapiFile(DrawConfig drawConfig, Queue<String> queue) {
        String token = "5ba3f205950008d6f4f8fe2beec10177";
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("getCaipiaoapiFile host:为空");
            return;
        }
        String lotteryCode = "";
        if (drawConfig.getToCode().contains("CNDPCDD")) {
            lotteryCode = "luckyjnd";
        }
        if (drawConfig.getToCode().contains("DMAIPCDD")) {
            lotteryCode = "dmxy28";
        }
        String timeStr = String.valueOf(System.currentTimeMillis() / 1000);
        String signStr = timeStr + "-" + token;
        String md5Str = MD5.getDigest(MD5.getDigest(signStr));

        String uri = "/hall/nodeService/api_request?uid=1742&time=" + timeStr + "&gamekey=" + lotteryCode
                + "&api=apiGameList&md5=" + md5Str + "&site=api.jiekouapi.com";
        String url = spiderIp + uri;
        String response = httpConnectionManager.get(url);
        logger.info("getCaipiaoapiFile spider:" + response);

        CaipiaoApiAM responseVo = null;
        List<CaipiaoApiItem> issues = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                responseVo = gson.fromJson(response, new TypeToken<CaipiaoApiAM>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (responseVo == null || responseVo.getResult() == null) {
                return;
            }
            CaipiaoApiData result = responseVo.getResult();
            issues = result.getData();
        }
        if (CollectionUtils.isEmpty(issues)) {
            return;
        }
        for (CaipiaoApiItem issueVo : issues) {
            String issue = issueVo.getGid();
            if (!queue.contains(issue)) {
                queue.add(issue);
                String timestamp = null;
                try {
                    timestamp = SimpleParse.parse(issueVo.getTime()).getTime() / 1000 + "";
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                LotteryIssueResult issueResult = new LotteryIssueResult();
                issueResult.setCode(issueVo.getAward());
                issueResult.setIssue(issue);
                issueResult.setTime(issueVo.getTime());
                issueResult.setOpentimestamp(timestamp);
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
            }
        }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
    }

    private void getFromCaipiaokongFile(DrawConfig drawConfig, Queue<String> queue) {
        String url = buildCaipiaokongUrl(drawConfig);

        String response = get(CaipiaokongHost, url);
        logger.info("getFromCaipiaokongFile spider:" + response);
        Map<String, CaipiaoKongBean> issues = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                issues = gson.fromJson(response, new TypeToken<Map<String, CaipiaoKongBean>>() {
                }.getType());
            } catch (Exception e) {
            }
        }

        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (Entry<String, CaipiaoKongBean> entry : issues.entrySet()) {
            String issue = entry.getKey();
            String convertCpkIssue = convertCpkIssue(drawConfig.getLotteryCode(), issue);

            if (StringUtils.isNotBlank(convertCpkIssue)) {
                issue = convertCpkIssue;
            }
            if (drawConfig.getConverter() != null) {
                issue = drawConfig.getConverter().convert(issue);
            }
            
            if (!queue.contains(issue)) {
                queue.add(issue);
                CaipiaoKongBean caipiaoKong = entry.getValue();
                String timestamp = caipiaoKong.getDateline();
                try {
                    timestamp = SimpleParse.parse(caipiaoKong.getDateline()).getTime() / 1000 + "";
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                LotteryIssueResult issueResult = new LotteryIssueResult();
                issueResult.setCode(caipiaoKong.getNumber());
                issueResult.setIssue(issue);
                issueResult.setTime(caipiaoKong.getDateline());
                issueResult.setOpentimestamp(timestamp);
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
            }
        }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
    }

    private void get360ffgfcaiFile(DrawConfig drawConfig, Queue<String> queue) {
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("get360ffgfcaiFile host:为空");
            return;
        }
        String url = spiderIp + "/api/c?_=" + System.currentTimeMillis();

        String response = httpConnectionManager.get(url);
        logger.info("get360ffgfcaiFile spider:" + response);

        Ffc360GfcAM responseVo = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                responseVo = gson.fromJson(response, new TypeToken<Ffc360GfcAM>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (responseVo == null || responseVo.getData() == null) {
                return;
            }
            Ffc360GfcItem codeVo = responseVo.getData();
            String nowIssue = TecentMmaService.buidlTencentIssue();
            Long openCode = Long.parseLong(codeVo.getI());
            String key = "SL060SSC:" + nowIssue + ":" + openCode;
            Long count = jedisTemplate.incr(key);

            if (count.intValue() > 3) {
                String issue = nowIssue;
                if (!queue.contains(issue)) {
                    queue.add(issue);
                    Date onlineDateTime = null;
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.SECOND, 0);
                    onlineDateTime = c.getTime();

                    logger.info("SL060SSC open:" + openCode);
                    String onlineTime = SimpleParse.format(onlineDateTime);
                    String lotteryNumber = SHA512Util.get360ffcFromSha512Str(StringUtils.trim(codeVo.getS()),
                            StringUtils.trim(codeVo.getI()));

                    LotteryIssueResult issueResult = new LotteryIssueResult();

                    issueResult.setCode(lotteryNumber);
                    issueResult.setIssue(issue);
                    issueResult.setCode1(codeVo.getI());
                    issueResult.setCode2(codeVo.getS());
                    String[] splitArr = issue.split("-");
                    int index = 0;
                    if (splitArr.length == 2) {
                        index = Integer.parseInt(splitArr[1]);
                    }
                    issueResult.setIndex(index);
                    issueResult.setTime(onlineTime);
                    writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
                }
            }
        }
        while (queue.size() > 10) {
            queue.poll();
        }
    }

    private void get360ffscaiFileFile(DrawConfig drawConfig, Queue<String> queue) {
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("get360ffscaiFileFile host:为空");
            return;
        }
        String url = spiderIp + "/api/defense";

        String response = httpConnectionManager.get(url);

        logger.debug("get360ffscaiFileFile url:{} ,lottery:{},json:{}", url, drawConfig.getLotteryCode(), response);
        if (StringUtils.isBlank(response)) {
            logger.error("get360ffscaiFileFile response为空 lottery:{},json:{}", drawConfig.getLotteryCode(), response);
            return;
        }
        ArrayList<Ffc360scAM> issues = null;
        try {
            issues = gson.fromJson(StringUtils.trim(response), new TypeToken<ArrayList<Ffc360scAM>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (issues == null) {
            return;
        }
        if (CollectionUtils.isEmpty(issues)) {
            return;
        }
        logger.info("get360ffscaiFileFile lottery:{} size:{}", drawConfig.getLotteryCode(), issues.size());
        for (int i = 0; i < 10; i++) {
            Ffc360scAM issueBean = issues.get(i);
            if (!queue.contains(issueBean.getIssue())) {
                queue.add(issueBean.getIssue());
                String issue = issueBean.getIssue();
                Date onlineDateTime = null;
                String time = issueBean.getTime();
                String timeStr = null;
                onlineDateTime = new Date(Long.parseLong(time));
                timeStr = SimpleParse.format(onlineDateTime);

                LotteryIssueResult issueResult = new LotteryIssueResult();
                issueResult.setCode(issueBean.getCode());
                issueResult.setIssue(issue);
                issueResult.setTime(timeStr);
                issueResult.setOpentimestamp(time);
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
            }
        }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
    }

    private void getBcbCaiFile(DrawConfig drawConfig, Queue<String> queue) {
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("getBcbCaiFile host:为空");
            return;
        }
        String url = spiderIp + "/outhistory?cz_id=";

        if (drawConfig.getLotteryCode().contains("BCB60SSC")) {
            url += "9001";
        }
        if (drawConfig.getLotteryCode().contains("BCB180SSC")) {
            url += "9002";
        }
        if (drawConfig.getLotteryCode().contains("BCB300SSC")) {
            url += "9003";
        }
        if (drawConfig.getLotteryCode().contains("BCB600SSC")) {
            url += "9004";
        }
        String response = httpConnectionManager.get(url);

        logger.debug("getBcbCaiFile url:{} ,lottery:{},json:{}", url, drawConfig.getLotteryCode(), response);
        if (StringUtils.isBlank(response)) {
            logger.error("getBcbCaiFile response为空 lottery:{},json:{}", drawConfig.getLotteryCode(), response);
            return;
        }
        BcbCodeAM am = null;
        try {
            am = gson.fromJson(StringUtils.trim(response), new TypeToken<BcbCodeAM>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (am == null) {
            return;
        }
        List<BcbCodeItem> issues = am.getData();
        if (CollectionUtils.isEmpty(issues)) {
            return;
        }
        logger.info("getBcbCaiFile lottery:{} size:{}", drawConfig.getLotteryCode(), issues.size());
        for (int i = 0; i < 10; i++) {
            BcbCodeItem issueBean = issues.get(i);
            // for (BcbCodeItem issueBean : issues) {
            if (!queue.contains(issueBean.getNumber())) {
                queue.add(issueBean.getNumber());
                String issue = issueBean.getNumber();
                if (drawConfig.getConverter() != null) {
                    issue = drawConfig.getConverter().convert(issue);
                }
                Date onlineDateTime = null;
                String time = issueBean.getTime() + "000";
                String timeStr = null;
                onlineDateTime = new Date(Long.parseLong(time));
                timeStr = SimpleParse.format(onlineDateTime);

                LotteryIssueResult issueResult = new LotteryIssueResult();
                issueResult.setCode(issueBean.getData());
                issueResult.setIssue(issue);
                issueResult.setTime(timeStr);
                issueResult.setOpentimestamp(time);
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
            }
        }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }

    }

    private void getxingyuncaiFile(DrawConfig drawConfig, Queue<String> queue) {
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("getxingyuncai:为空");
            return;
        }
        String url = "/history";
        if (!drawConfig.getToCode().equals("XY60SSC") && !drawConfig.getToCode().equals("xyffc")) {
            url += "?code=" + drawConfig.getToCode();
        }
        String hostDomain = StringUtils.trim(spiderIp);
        String response = get(hostDomain, url);
        logger.debug("getxingyuncai lottery:{},json:{}", drawConfig.getLotteryCode(), response);
        XycIssueAM xycAm = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                xycAm = gson.fromJson(StringUtils.trim(response), XycIssueAM.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (xycAm == null) {
            logger.error("getxingyuncai:获取接口返回为空");
            return;
        }
        Integer code = xycAm.getCode();
        if (code == null || code.intValue() != 200) {
            logger.error("getxingyuncai:获取接口返回code不对:{}", code);
            return;
        }
        XycIssueInfo data = xycAm.getData();
        if (data == null || CollectionUtils.isEmpty(data.getInfo())) {
            logger.error("getxingyuncai:获取接口返回XycIssueInfo不对:{}", response);
            return;
        }
        List<XycIssueItem> issues = data.getInfo();
        if (CollectionUtils.isEmpty(issues)) {
            logger.error("getxingyuncai:获取接口返回XycIssueInfo不对:{}", response);
            return;
        }
        int count = 0;
        for (XycIssueItem issueBean : issues) {
            if (count >= 5) {
                break;
            }
            String qihao = issueBean.getQihao();
            if (drawConfig.getConverter() != null) {
                qihao = drawConfig.getConverter().convert(qihao);
            }
            if (!queue.contains(qihao)) {
                queue.add(qihao);
                String open_no = issueBean.getOpen_no();
                String lotteryNumber = getSplitNumber(open_no);
                String timeStamp = issueBean.getOpen_time() + "000";
                Date d = new Date(Long.parseLong(timeStamp));
                String timeStr = SimpleParse.format(d);

                LotteryIssueResult issueResult = new LotteryIssueResult();

                issueResult.setCode(lotteryNumber);
                issueResult.setIssue(qihao);

                String[] splitArr = qihao.split("-");
                int index = 0;
                if (splitArr.length == 2) {
                    index = Integer.parseInt(splitArr[1]);
                }
                issueResult.setIndex(index);
                issueResult.setTime(timeStr);
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
            }
            count++;
        }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }

    }

    private void getBitcoinumFile(DrawConfig drawConfig, Queue<String> queue) {
        String url = "https://blockchain.info/unconfirmed-transactions?format=json";
        String response = httpConnectionManager.get(url);

        logger.debug("getBitcoinumFile lottery:{},json:{}", drawConfig.getLotteryCode(), response);
        if (StringUtils.isBlank(response)) {
            logger.error("getBitcoinumFile response为空 lottery:{},json:{}", drawConfig.getLotteryCode(), response);
            return;
        }
        BitcoiNumAM bitcoiNumAM = null;

        if (!StringUtils.isEmpty(response)) {
            try {
                bitcoiNumAM = gson.fromJson(StringUtils.trim(response), BitcoiNumAM.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bitcoiNumAM == null) {
            logger.error("getBitcoinumFile:获取接口返回为空");
            return;
        }
        List<BitcoinNumItem> txs = bitcoiNumAM.getTxs();

        if (CollectionUtils.isEmpty(txs)) {
            logger.error("getBitcoinumFile:获取txs返回的size为空");
            return;
        }
        for (BitcoinNumItem item : txs) {
            Long timeL = Long.parseLong(String.valueOf(item.getTime()) + "000");
            Date d = new Date(timeL);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            String dateStr = SimpleParse.format(d);
            int second = c.get(Calendar.SECOND);
            logger.info("timeL:{},dateStr:{} second:{}", timeL, dateStr, second);
            String str = dateStr + "   " + item.getHash() + "   " + item.getVer() + "   " + item.getLock_time() + "  "
                    + item.getTx_index();
            try {
                FileUtils.writeStringToFile(new File("C:\\1.txt"), str + "\r\n", "UTF-8", true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (second != 1 && second != 2) {
                continue;
            }

            String hash = item.getHash();
            String key = dateStr + "@" + hash;

            if (!queue.contains(key)) {
                logger.info("date:{},hash:{}", dateStr, hash);
                queue.add(key);
            }
            while (queue.size() > 100) {
                queue.poll();
            }
        }

    }

    private void getManycaiFile(DrawConfig drawConfig, Queue<String> queue) {
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("getManycai host:为空");
            return;
        }
        String token = drawConfig.getToken();
        if (StringUtils.isBlank(token)) {
            logger.error("getManycai token:为空");
            return;
        }
        String url = "/" + drawConfig.getToken();

        
        if(drawConfig.getLotteryCode().contains("HENEI60SSC")||drawConfig.getLotteryCode().contains("HENEI300SSC")) {
       	 if (drawConfig.getLotteryCode().contains("HENEI60SSC")) {
                url += "/hn60-5.json";
            }
            if (drawConfig.getLotteryCode().contains("HENEI300SSC")) {
                url += "/hn300-5.json";
            }
       }else {
       	 url +="/"+drawConfig.getToCode()+ "-5.json";
       }
        String hostDomain = StringUtils.trim(spiderIp);
        String response = get(hostDomain, url);

        logger.info("   url:{} ,lottery:{},json:{}", url, drawConfig.getLotteryCode(), response);
        if (StringUtils.isBlank(response)) {
            logger.error("getManycai response为空 lottery:{},json:{}", drawConfig.getLotteryCode(), response);
            return;
        }
        ArrayList<ManyCaiAM> issues = null;
        try {
            issues = gson.fromJson(StringUtils.trim(response), new TypeToken<ArrayList<ManyCaiAM>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();  
        }

        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }
        for (ManyCaiAM issueBean : issues) {
            String issue = issueBean.getIssue();
            if(drawConfig.getToCode().contains("GD11X5")||drawConfig.getToCode().contains("SD11X5")) {
            	issue = "20"+issue;
            }
            if (drawConfig.getConverter() != null) {
                issue = drawConfig.getConverter().convert(issue);
            }
            if (!queue.contains(issueBean.getIssue())) {
                Date onlineDateTime = null;
                String onlineStr = issueBean.getOpendate();
                try {
                    onlineDateTime = SimpleParse.parse(String.valueOf(onlineStr));
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
             
                String code1 = issueBean.getCode();
                if(drawConfig.getToCode().contains("11X5")) {
                	 StringBuffer codeStr=new StringBuffer();
                     String []codeArr = issueBean.getCode().split(",");
                     for(String tempCode: codeArr) {
//                     	logger.info("*************************tempCode:{} ",tempCode);
                     	
                     	 String formatCode =  String.format("%02d", Integer.parseInt(tempCode));
                     	 if(codeStr.length()>0) {
                     		 codeStr.append(",");
                     	 }
                     	 codeStr.append(formatCode);
                     	 code1=codeStr.toString();
                     }
                 }
               
                LotteryIssueResult issueResult = new LotteryIssueResult();
                issueResult.setCode(code1);
                issueResult.setIssue(issue);
                issueResult.setTime(onlineStr);
                issueResult.setOpentimestamp(String.valueOf(onlineDateTime.getTime()));
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
                queue.add(issueBean.getIssue());
            }
        }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }

    }

    private void getManycai(DrawConfig drawConfig, Queue<String> queue) {
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("getManycai host:为空");
            return;
        }
        String token = drawConfig.getToken();
        if (StringUtils.isBlank(token)) {
            logger.error("getManycai token:为空");
            return;
        }
        String url = "/" + drawConfig.getToken();
        if(drawConfig.getLotteryCode().contains("HENEI60SSC")||drawConfig.getLotteryCode().contains("HENEI300SSC")) {
        	 if (drawConfig.getLotteryCode().contains("HENEI60SSC")) {
                 url += "/hn60-5.json";
             }
             if (drawConfig.getLotteryCode().contains("HENEI300SSC")) {
                 url += "/hn300-5.json";
             }
        }else {
        	 url +="/"+drawConfig.getToCode()+ "-5.json";
        }
       
        
        String hostDomain = StringUtils.trim(spiderIp);
        String response = get(hostDomain, url);

        logger.info("getManycai lottery:{},json:{}", drawConfig.getLotteryCode(), response);
        if (StringUtils.isBlank(response)) {
            logger.error("getManycai response为空 lottery:{},json:{}", drawConfig.getLotteryCode(), response);
            return;
        }
        ArrayList<ManyCaiAM> issues = null;
        try {
            issues = gson.fromJson(StringUtils.trim(response), new TypeToken<ArrayList<ManyCaiAM>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (ManyCaiAM entry : issues) {
        	 if (queue.contains(entry.getIssue())) {
        		 continue;
        	 }
            Date onlineDateTime = null;
            String issue = entry.getIssue();
            
            if(drawConfig.getToCode().contains("GD11X5")||drawConfig.getToCode().contains("SD11X5")) {
            	issue = "20"+entry.getIssue();
            }
            if(drawConfig.getToCode().contains("XGLHC")) {
            	issue = "20"+entry.getIssue();
            	String [] issueArr =entry.getIssue().split("/");
            	issue = issueArr[1];
            }
            if (drawConfig.getConverter() != null) {
                issue = drawConfig.getConverter().convert(issue);
            }
            String onlineStr = entry.getOpendate();
            try {
                onlineDateTime = SimpleParse.parse(String.valueOf(onlineStr));
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
            String lotteryNumber = null;
            lotteryNumber = entry.getCode();
            if(drawConfig.getToCode().contains("XGLHC")) {
            	lotteryNumber=lotteryNumber.replaceAll("+", ",");
            }
            if(drawConfig.getToCode().contains("11X5")||drawConfig.getToCode().contains("XGLHC")) {
            	StringBuffer codeStr=new StringBuffer();
                String []codeArr = lotteryNumber.split(",");
                for(String tempCode: codeArr) {
	            	 String formatCode =  String.format("%02d", Integer.parseInt(tempCode));
	            	 if(codeStr.length()>0) {
	            		 codeStr.append(",");
	            	 }
	            	 codeStr.append(formatCode);
                }
            	 lotteryNumber=codeStr.toString();
            }
            notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, lotteryNumber, onlineStr,
                    String.valueOf(onlineDateTime.getTime() / 1000), null);
            queue.add(issue);
        }
        while (queue.size() > 10) {
            queue.poll();
        }

    }

    private void getxingyuncai(DrawConfig drawConfig, Queue<String> queue) {
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("getxingyuncai:为空");
            return;
        }
        String url = "/history";
        if (!drawConfig.getToCode().equals("XY60SSC") && !drawConfig.getToCode().equals("xyffc")) {
            url += "?code=" + drawConfig.getToCode();
        }
        String hostDomain = StringUtils.trim(spiderIp);
        String response = get(hostDomain, url);
        logger.debug("getxingyuncai lottery:{},json:{}", drawConfig.getLotteryCode(), response);
        XycIssueAM xycAm = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                xycAm = gson.fromJson(StringUtils.trim(response), XycIssueAM.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (xycAm == null) {
            logger.error("getxingyuncai:获取接口返回为空");
            return;
        }
        Integer code = xycAm.getCode();
        if (code == null || code.intValue() != 200) {
            logger.error("getxingyuncai:获取接口返回code不对:{}", code);
            return;
        }
        XycIssueInfo data = xycAm.getData();
        if (data == null || CollectionUtils.isEmpty(data.getInfo())) {
            logger.error("getxingyuncai:获取接口返回XycIssueInfo不对:{}", response);
            return;
        }
        List<XycIssueItem> issues = data.getInfo();

        for (XycIssueItem issueBean : issues) {
            if (!queue.contains(issueBean.getQihao())) {
                String open_no = issueBean.getOpen_no();
                String lotteryNumber = getSplitNumber(open_no);
                String timeStamp = issueBean.getOpen_time() + "000";
                Date d = new Date(Long.parseLong(timeStamp));
                String timeStr = SimpleParse.format(d);
                notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issueBean.getQihao(), lotteryNumber, timeStr,
                        timeStamp, null);

                queue.add(issueBean.getQihao());
            }
        }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
    }

    private String getSplitNumber(String number) {
        StringBuilder sb = new StringBuilder();
        char[] charNumber = number.toCharArray();
        for (char c : charNumber)
            sb.append(c + ",");
        String newNumber = sb.toString();
        return newNumber.substring(0, newNumber.length() - 1);
    }

    private void getKenoZjFile(DrawConfig drawConfig, Queue<String> queue) {
        String url = "/";
        if (drawConfig.getToCode().contains("TXSSC")) {
            url = url + "txssc/results/recent.json";
        } else {
            url = url + drawConfig.getToCode() + "/results/recent.json";
        }
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("getKenoZjFile:ip为空");
            return;
        }
        String hostDomain = StringUtils.trim(spiderIp);

        String response = get(hostDomain, url);
        logger.info("getKenoZjFile lottery:{},json:{}", drawConfig.getLotteryCode(), response);

        ArrayList<LotteryIssueResult> issues = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                issues = gson.fromJson(StringUtils.trim(response), new TypeToken<ArrayList<LotteryIssueResult>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (LotteryIssueResult issueBean : issues) {
            String lotteryCode = StringUtils.trim(drawConfig.getLotteryCode());
            String toLotteryCode = StringUtils.trim(drawConfig.getToCode());
            boolean isTxSerise = handTxSscSeries(lotteryCode, issueBean);
            if (toLotteryCode.equals("TXSSC") && !isTxSerise) {
                continue;
            }
            boolean isXySerise = handXySscSeries(lotteryCode, issueBean);

            if (toLotteryCode.equals("XY60SSC") && !isXySerise) {
                continue;
            }
            boolean isQiQuSerise = handQiQuSscSeries(lotteryCode, issueBean);

            if (toLotteryCode.equals("QIQU60SSC") && !isQiQuSerise) {
                continue;
            }
            
            boolean isNewQiQuSerise = handNewQiQuSscSeries(lotteryCode, issueBean);

            if (toLotteryCode.equals("NQIQU60SSC") && !isNewQiQuSerise) {
                continue;
            }
            
            if (!queue.contains(issueBean.getIssue())) {
                queue.add(issueBean.getIssue());
                String kenoCode = issueBean.getCode();
                if (StringUtils.isBlank(drawConfig.getType())) {
                    kenoCode = ConvertToKenoUtil.convert(issueBean.getCode());
                }

                LotteryIssueResult issueResult = new LotteryIssueResult();
                issueResult.setCode(kenoCode);
                issueResult.setIssue(issueBean.getIssue());
                issueResult.setTime(issueBean.getTime());
                issueResult.setOpentimestamp(issueBean.getOpentimestamp());
                issueResult.setYuLiu(issueBean.getYuLiu());
                issueResult.setCode1(issueBean.getCode1());
                issueResult.setCode2(issueBean.getCode2());
                try {
                    if (lotteryCode.contains("TX60PK10")) {
                        StringBuffer strBu = new StringBuffer(kenoCode);
                        strBu.append(issueBean.getTime());
                        String[] arr = kenoCode.split("");
                        int sum = 0;
                        for (String str : arr) {
                            sum += Integer.parseInt(str);
                        }
                        strBu.append(String.valueOf(sum));
                        String sha512Str = EncryptSHA.SHA512(strBu.toString());
                        issueResult.setHashValue(sha512Str);
                        String pk10Code = jiequPk10NumFromHashV(sha512Str);
                        issueResult.setPk10Code(pk10Code);
                    } else {
                        issueResult.setHashValue(issueBean.getHashValue());
                        issueResult.setPk10Code(issueBean.getPk10Code());
                    }
                } catch (NumberFormatException e) {
                    logger.error(e.getMessage(), e);
                }
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
            }
        }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }

    }

    private boolean handQiQuSscSeries(String lotteryCode, LotteryIssueResult issueBean) {
        String txffcIssue = issueBean.getIssue();
        if (!lotteryCode.equals("QIQU180SSC") && !lotteryCode.equals("QIQU300SSC")
                && !lotteryCode.equals("QIQU600SSC")) {
            return true;
        }
        String dateStr = txffcIssue.split("-")[0];
        int issueIndex = Integer.parseInt(txffcIssue.split("-")[1]);

        if (lotteryCode.equals("QIQU180SSC") && issueIndex % 3 != 0) {
            return false;
        }
        if (lotteryCode.equals("QIQU300SSC") && issueIndex % 5 != 0) {
            return false;
        }
        if (lotteryCode.equals("QIQU600SSC") && issueIndex % 10 != 0) {
            return false;
        }
        String issueNum = null;

        int mod = 0;
        if (lotteryCode.equals("QIQU300SSC")) {
            mod = issueIndex / 5;
        }
        if (lotteryCode.equals("QIQU180SSC")) {
            mod = issueIndex / 3;
        }
        if (lotteryCode.equals("QIQU600SSC")) {
            mod = issueIndex / 10;
        }
        issueNum = dateStr + "-" + String.format("%03d", mod);
        logger.info("***************奇趣彩：{},本来奖期：{},需要转换成:{}", lotteryCode, txffcIssue, issueNum);
        issueBean.setIssue(issueNum);
        return true;
    }
    
    private boolean handNewQiQuSscSeries(String lotteryCode, LotteryIssueResult issueBean) {
        String txffcIssue = issueBean.getIssue();
        if (!lotteryCode.equals("NQIQU180SSC") && !lotteryCode.equals("NQIQU300SSC")
                && !lotteryCode.equals("NQIQU600SSC")) {
            return true;
        }
        String dateStr = txffcIssue.split("-")[0];
        int issueIndex = Integer.parseInt(txffcIssue.split("-")[1]);

        if (lotteryCode.equals("NQIQU180SSC") && issueIndex % 3 != 0) {
            return false;
        }
        if (lotteryCode.equals("NQIQU300SSC") && issueIndex % 5 != 0) {
            return false;
        }
        if (lotteryCode.equals("NQIQU600SSC") && issueIndex % 10 != 0) {
            return false;
        }
        String issueNum = null;

        int mod = 0;
        if (lotteryCode.equals("NQIQU300SSC")) {
            mod = issueIndex / 5;
        }
        if (lotteryCode.equals("NQIQU180SSC")) {
            mod = issueIndex / 3;
        }
        if (lotteryCode.equals("NQIQU600SSC")) {
            mod = issueIndex / 10;
        }
        issueNum = dateStr + "-" + String.format("%03d", mod);
        logger.info("***************新奇趣彩：{},本来奖期：{},需要转换成:{}", lotteryCode, txffcIssue, issueNum);
        issueBean.setIssue(issueNum);
        return true;
    }

    private boolean handXySscSeries(String lotteryCode, LotteryIssueResult issueBean) {
        String txffcIssue = issueBean.getIssue();
        if (!lotteryCode.contains("XY180SSC") && !lotteryCode.contains("XY300SSC")
                && !lotteryCode.contains("XY600SSC")) {
            return true;
        }
        String dateStr = txffcIssue.split("-")[0];
        int issueIndex = Integer.parseInt(txffcIssue.split("-")[1]);

        if (lotteryCode.contains("XY180SSC") && issueIndex % 3 != 0) {
            return false;
        }
        if (lotteryCode.contains("XY300SSC") && issueIndex % 5 != 0) {
            return false;
        }
        if (lotteryCode.contains("XY600SSC") && issueIndex % 10 != 0) {
            return false;
        }
        String issueNum = null;

        int mod = 0;
        if (lotteryCode.contains("XY600SSC")) {
            mod = issueIndex / 10;

        }
        if (lotteryCode.contains("XY300SSC")) {
            mod = issueIndex / 5;
        }
        if (lotteryCode.contains("XY180SSC")) {
            mod = issueIndex / 3;
        }
        issueNum = dateStr + "-" + String.format("%03d", mod);
        logger.info("***************幸运：{},本来奖期：{},需要转换成:{}", lotteryCode, txffcIssue, issueNum);
        issueBean.setIssue(issueNum);
        return true;
    }

    private boolean handTxSscSeries(String lotteryCode, LotteryIssueResult issueBean) {
        String txffcIssue = issueBean.getIssue();
        if (!lotteryCode.contains("TXN300") && !lotteryCode.contains("TX600") && !lotteryCode.contains("TX120")) {
            return true;
        }
        String dateStr = txffcIssue.split("-")[0];
        int issueIndex = Integer.parseInt(txffcIssue.split("-")[1]);

        if (lotteryCode.contains("TX120SSC") && issueIndex % 2 != 0) {
            return false;
        }
        if (lotteryCode.contains("TX120DSSC") && issueIndex % 2 == 0) {
            return false;
        }
        if (lotteryCode.contains("TXN300") && issueIndex % 5 != 0) {
            return false;
        }
        if (lotteryCode.contains("TX600") && issueIndex % 10 != 0) {
            return false;
        }
        String issueNum = null;

        int mod = 0;
        if (lotteryCode.contains("TX600")) {
            mod = issueIndex / 10;

        }
        if (lotteryCode.contains("TXN300")) {
            mod = issueIndex / 5;
        }
        if (lotteryCode.contains("TX120SSC")) {
            mod = issueIndex / 2;
        }
        if (lotteryCode.contains("TX120DSSC")) {
            mod = issueIndex / 2 + 1;
        }
        issueNum = dateStr + "-" + String.format("%03d", mod);
        issueBean.setIssue(issueNum);
        return true;
    }

    private String jiequPk10NumFromHashV(String str) {
        str = str.trim();
        List<Integer> list = new ArrayList<>();
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    Integer charAt = str.charAt(i) - '0';
                    list.add(charAt);
                }
            }
        }
        List<String> codeList = new ArrayList<>();

        for (Integer num : list) {
            if (codeList.size() == 10) {
                break;
            }
            String kk = null;
            if (num.intValue() == 0) {
                kk = "10";
            } else if (num.intValue() < 10) {
                kk = "0" + num;
            }
            if (!codeList.contains(kk)) {
                codeList.add(kk);
            }
        }
        return StringUtils.join(codeList, ",");
    }

    private void getFromKcwZj(DrawConfig drawConfig, Queue<String> queue) {
        String url = "/";
        url = url + drawConfig.getToCode() + "/results/recent.json";
        String spiderIp = drawConfig.getSpiderIp();
        if (StringUtils.isBlank(spiderIp)) {
            logger.error("getFromKcwZj:ip为空");
            return;
        }
        String hostDomain = StringUtils.trim(spiderIp);

        String response = get(hostDomain, url);
        logger.info("getFromApiPlus lottery:{},json:{}", drawConfig.getLotteryCode(), response);

        ArrayList<LotteryIssueResult> issues = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                issues = gson.fromJson(StringUtils.trim(response), new TypeToken<ArrayList<LotteryIssueResult>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (LotteryIssueResult issueBean : issues) {
            if (!queue.contains(issueBean.getIssue())) {
                String lotteryNumber = issueBean.getCode();
                if (drawConfig.getLotteryCode().contains("XYFTSSC")
                        || drawConfig.getLotteryCode().contains("BJSCSSC")) {
                    lotteryNumber = buildPk10ToSSCCode(issueBean.getCode());
                }
                if (drawConfig.getLotteryCode().contains("BJPCDDXJW")) {
                    lotteryNumber = buildPCDDCode(issueBean.getCode());
                }
                if (drawConfig.getLotteryCode().contains("CNDPCDDXJW")) {
                    lotteryNumber = buildCndPCDDCode(issueBean.getCode());
                }
                notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issueBean.getIssue(), lotteryNumber,
                        issueBean.getTime(), issueBean.getOpentimestamp(), null);
                queue.add(issueBean.getIssue());
            }
        }
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
    }

    private String buildCndPCDDCode(String codeNode) {
        if (codeNode.contains("+")) {
            String[] codes = codeNode.split("\\+");
            codeNode = codes[0];
        }
        String[] numStrs = codeNode.split(",");
        int sum1 = Integer.valueOf(numStrs[1]) + Integer.valueOf(numStrs[4]) + Integer.valueOf(numStrs[7])
                + Integer.valueOf(numStrs[10]) + Integer.valueOf(numStrs[13]) + Integer.valueOf(numStrs[16]);
        int a = sum1 % 10;

        int sum2 = Integer.valueOf(numStrs[2]) + Integer.valueOf(numStrs[5]) + Integer.valueOf(numStrs[8])
                + Integer.valueOf(numStrs[11]) + Integer.valueOf(numStrs[14]) + Integer.valueOf(numStrs[17]);
        int b = sum2 % 10;

        int sum3 = Integer.valueOf(numStrs[3]) + Integer.valueOf(numStrs[6]) + Integer.valueOf(numStrs[9])
                + Integer.valueOf(numStrs[12]) + Integer.valueOf(numStrs[15]) + Integer.valueOf(numStrs[18]);
        int c = sum3 % 10;

        int d = a + b + c;

        return a + "," + b + "," + c + "," + d;
    }

    private String buildPCDDCode(String codeNode) {
        if (codeNode.contains("+")) {
            String[] codes = codeNode.split("\\+");
            codeNode = codes[0];
        }

        String[] numStrs = codeNode.split(",");
        Integer[] codes = new Integer[numStrs.length];

        for (int i = 0; i < numStrs.length; i++) {
            codes[i] = Integer.valueOf(numStrs[i]);
        }

        Arrays.sort(codes);

        int[] resultNums = new int[4];

        for (int i = 0; i < codes.length - 2; i++) {
            resultNums[i / 6] += codes[i];
        }

        for (int i = 0; i < resultNums.length - 1; i++) {
            resultNums[i] = resultNums[i] % 10;
        }
        resultNums[3] = resultNums[0] + resultNums[1] + resultNums[2];
        String resultCode = StringUtils.join(resultNums, ',');
        return resultCode;
    }

    private String buildPk10ToSSCCode(String code) {
        String[] codeArr = code.split(",");
        int[] numberArr = new int[5];
        numberArr[0] = (Integer.parseInt(StringUtils.trim(codeArr[0])) + Integer.parseInt(StringUtils.trim(codeArr[1])))
                % 10;
        numberArr[1] = (Integer.parseInt(StringUtils.trim(codeArr[2])) + Integer.parseInt(StringUtils.trim(codeArr[3])))
                % 10;
        numberArr[2] = (Integer.parseInt(StringUtils.trim(codeArr[4])) + Integer.parseInt(StringUtils.trim(codeArr[5])))
                % 10;
        numberArr[3] = (Integer.parseInt(StringUtils.trim(codeArr[6])) + Integer.parseInt(StringUtils.trim(codeArr[7])))
                % 10;
        numberArr[4] = (Integer.parseInt(StringUtils.trim(codeArr[8])) + Integer.parseInt(StringUtils.trim(codeArr[9])))
                % 10;
        return StringUtils.join(numberArr, ',');
    }

    private void getFromKcwFile(DrawConfig drawConfig, Queue<String> queue) {
        String url = buildApiPlus(drawConfig);

        String response = get(ApiPlusHost, url);
        logger.info("getFromApiPlus lottery:{},json:{}", drawConfig.getLotteryCode(), response);
        ApiPlusBean bean = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                bean = gson.fromJson(response, ApiPlusBean.class);
            } catch (Exception e) {
            }
        }

        if (bean == null) {
            return;
        }

        List<ApiPlushDataBean> issues = bean.getData();
        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (ApiPlushDataBean issueBean : issues) {
            if (!queue.contains(issueBean.getExpect())) {

                LotteryIssueResult issueResult = new LotteryIssueResult();
                issueResult.setCode(issueBean.getOpencode());
                issueResult.setIssue(issueBean.getExpect());
                issueResult.setTime(issueBean.getOpentime());
                issueResult.setOpentimestamp(issueBean.getOpentimestamp());
                writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
                queue.add(issueBean.getExpect());
            }
        }

        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }
    }
    
   

    private void getFromZjsh(DrawConfig drawConfig, Queue<String> queue) {
        String url = buildZjtxUrl(drawConfig);
        String spiderIp = drawConfig.getSpiderIp();
        String hostDomain = tx30sscHost;
        if (StringUtils.isNotBlank(spiderIp)) {
            hostDomain = StringUtils.trim(spiderIp);
        }
        String response = get(hostDomain, url);
        logger.info("getFromApiPlus lottery:{},json:{}", drawConfig.getLotteryCode(), response);

        ArrayList<TxZjshSscAM> issues = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                issues = gson.fromJson(StringUtils.trim(response), new TypeToken<ArrayList<TxZjshSscAM>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (TxZjshSscAM entry : issues) {
            Date onlineDateTime = null;
            String issue = entry.getIssue();
            String onlineStr = entry.getTime();
            try {
                onlineDateTime = SimpleParse.parse(String.valueOf(onlineStr));
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
            if (!queue.contains(issue)) {
                TxZjshSscAM tencentOnline = entry;
                String lotteryNumber = null;
                String codeType = drawConfig.getType();
                String onlineCode = tencentOnline.getCode();
                if (StringUtils.isNotBlank(codeType)) {
                    lotteryNumber = tencentOnline.getCode();
                    onlineCode = tencentOnline.getYuLiu();
                } else {
                    if (drawConfig.getLotteryCode().contains("PK10")) {
                        lotteryNumber = tencentOnline.getPk10Code();
                    } else {
                        lotteryNumber = buildTencentNumber(tencentOnline.getCode());
                    }

                }
                notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, lotteryNumber, onlineStr,
                        String.valueOf(onlineDateTime.getTime() / 1000), onlineCode);
                queue.add(issue);
            }
        }

        while (queue.size() > 10) {
            queue.poll();
        }
    }

    private String buildZjtxUrl(DrawConfig drawConfig) {
        String url = "/";

        if (drawConfig.getToCode().contains("TX30SSC")) {
            url = url + "tx30/results/recent.json";
            return url;
        }
        if (drawConfig.getToCode().contains("QQ60SSC")) {
            url = url + "qq60/results/recent.json";
            return url;
        }
        if (drawConfig.getToCode().contains("TXSSC")) {
            url = url + "txssc/results/recent.json";
            return url;
        }
        url = url + drawConfig.getToCode() + "/results/recent.json";
        return url;
    }

    private void getFromDsjtTxffc(DrawConfig drawConfig, Queue<String> queue) {
        String url = "/qq/get_new_code";
        String response = get(flbSscHost, url);

        DjstTxffcAM responseVo = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                responseVo = gson.fromJson(response, new TypeToken<DjstTxffcAM>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (responseVo == null || responseVo.getData() == null || responseVo.getData().size() <= 0) {
            return;
        }
        List<DjstTxffcItem> list = responseVo.getData();
        for (DjstTxffcItem item : list) {

            if (item == null) {
                continue;
            }
            String issue = item.getExpect();
            if (!queue.contains(issue)) {
                // 鏍煎紡鍖栧鏈�
                String timestamp = item.getOpentime();
                try {
                    timestamp = SimpleParse.parse(item.getOpentime()).getTime() / 1000 + "";
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, item.getOpencode(), item.getOpentime(),
                        timestamp, null);
                queue.add(issue);
            }
            while (queue.size() > QUEUE_MAX_SIZE) {
                queue.poll();
            }
        }

    }

    private void getFromTecentcjiujiu(DrawConfig drawConfig, Queue<String> queue) {
        String url = tecentCjiujiuHost;
        String result = null;
        try {
            HttpResponse response = HttpClientUtil.sendHttpsGet(url, "");

            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("TXSSC Cjiujiu spider:" + result);

        if (StringUtils.isBlank(result)) {
            return;
        }
        int beginIndex = result.indexOf("﻿ret(");
        String res = result.substring(beginIndex + 5, result.length() - 1);

        ArrayList<TecentCjiujiuAM> issues = null;
        try {
            issues = gson.fromJson(res, new TypeToken<ArrayList<TecentCjiujiuAM>>() {
            }.getType());
        } catch (Exception e) {
        }

        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (TecentCjiujiuAM entry : issues) {
            Date onlineDateTime = null;
            try {
                onlineDateTime = SimpleParse.parse(entry.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String issue = buidlTencentIssue(entry.getTime(), onlineDateTime);
            if (!queue.contains(issue)) {
                TecentCjiujiuAM tencentOnline = entry;
                String onlineTime = tencentOnline.getTime();
                String lotteryNumber = buildTencentNumber(tencentOnline.getNum());

                notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, lotteryNumber, onlineTime,
                        String.valueOf(onlineDateTime.getTime() / 1000), tencentOnline.getNum());
                // if (queue.size() > QUEUE_MAX_SIZE) {
                //
                // }
                queue.add(issue);
            }

        }

        while (queue.size() > 10) {
            queue.poll();
        }

    }

    private void getFromTecentMa(DrawConfig drawConfig, Queue<String> queue) {
        String url = "https://" + tecentMmaHost + "/cgi-bin/im/online";
        String response = httpConnectionManager.get(url);
        // String response = get(tecentMmaHost, url);

        TecentMmaIssue responseVo = null;
        if (!StringUtils.isEmpty(response)) {
            int beginIndex = response.indexOf("{");
            int endIndex = response.indexOf("}");
            String res = response.substring(beginIndex, endIndex + 1);
            logger.info("TXSSC spider:" + res);

            try {
                responseVo = gson.fromJson(res, new TypeToken<TecentMmaIssue>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (responseVo == null || responseVo.getC() == null) {
                return;
            }
            String nowIssue = "";
            if (drawConfig.getLotteryCode().equals("TX30SSC")) {
                nowIssue = TecentMmaService.buidlTencent30SSCIssue();
            } else {
                nowIssue = TecentMmaService.buidlTencentIssue();
            }
            Long openCode = responseVo.getC();
            String key = "TXFFC:" + nowIssue + ":" + openCode;
            Long count = jedisTemplate.incr(key);

            if (count.intValue() > 2) {
                String issue = nowIssue;
                if (!queue.contains(issue)) {
                    Date onlineDateTime = null;
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MILLISECOND, 0);
                    onlineDateTime = c.getTime();

                    logger.info("TXSSC open:" + openCode);
                    String onlineTime = SimpleParse.format(onlineDateTime);
                    String lotteryNumber = buildTencentNumber(String.valueOf(openCode));

                    notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, lotteryNumber, onlineTime,
                            String.valueOf(onlineDateTime.getTime() / 1000), String.valueOf(openCode));
                    queue.add(issue);
                }
            }
        }
        while (queue.size() > 10) {
            queue.poll();
        }

    }

    private void getFromTecentMaFile(DrawConfig drawConfig, Queue<String> queue) {
        String url = "https://" + tecentMmaHost + "/cgi-bin/im/online";
        String response = httpConnectionManager.get(url);

        TecentMmaIssue responseVo = null;
        if (!StringUtils.isEmpty(response)) {
            int beginIndex = response.indexOf("{");
            int endIndex = response.indexOf("}");
            String res = response.substring(beginIndex, endIndex + 1);
            logger.info("TXSSC spider:" + res);

            try {
                responseVo = gson.fromJson(res, new TypeToken<TecentMmaIssue>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (responseVo == null || responseVo.getC() == null) {
                return;
            }
            String nowIssue = "";
            if (drawConfig.getLotteryCode().equals("TX30SSC")) {
                nowIssue = TecentMmaService.buidlTencent30SSCIssue();
            } else {
                nowIssue = TecentMmaService.buidlTencentIssue();
            }
            Long openCode = responseVo.getC();
            String key = "TXFFC:" + nowIssue + ":" + openCode;
            Long count = jedisTemplate.incr(key);

            if (count.intValue() > 3) {
                String issue = nowIssue;
                if (!queue.contains(issue)) {
                    Date onlineDateTime = null;
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MILLISECOND, 0);
                    onlineDateTime = c.getTime();

                    logger.info("TXSSC open:" + openCode);
                    String onlineTime = SimpleParse.format(onlineDateTime);
                    String lotteryNumber = buildTencentNumber(String.valueOf(openCode));

                    LotteryIssueResult issueResult = new LotteryIssueResult();

                    issueResult.setCode(lotteryNumber);
                    issueResult.setIssue(issue);
                    issueResult.setYuLiu(String.valueOf(openCode));
                    String[] splitArr = issue.split("-");
                    int index = 0;
                    if (splitArr.length == 2) {
                        index = Integer.parseInt(splitArr[1]);
                    }
                    issueResult.setIndex(index);
                    issueResult.setTime(onlineTime);
                    writeFilePool.submit(new LotteryResultSaveTask(drawConfig, issueResult));
                    queue.add(issue);
                }
            }
        }
        while (queue.size() > 10) {
            queue.poll();
        }
    }

    private void getFromDsjtxssc(DrawConfig drawConfig, Queue<String> queue) {
        String url = buildDsjtUrl(drawConfig);
        String response = get(flbSscHost, url);

        FlbSScIssue responseVo = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                responseVo = gson.fromJson(response, new TypeToken<FlbSScIssue>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (responseVo == null || responseVo.getData() == null || responseVo.getData().size() <= 0) {
            return;
        }
        Set<String> keySet = responseVo.getData().keySet();
        for (String key : keySet) {
            List<FlbSScIssueDetail> issues = responseVo.getData().get(key);

            if (CollectionUtils.isEmpty(issues)) {
                continue;
            }
            for (FlbSScIssueDetail detail : issues) {
                String issue = detail.getIssue();
                if (!queue.contains(issue)) {
                    // 鏍煎紡鍖栧鏈�
                    String timestamp = detail.getAddtime();
                    Date now = new Date();
                    timestamp = now.getTime() / 1000 + "";
                    String timeStr = SimpleParse.format(now);
                    notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, detail.getCode(), timeStr, timestamp,
                            null);

                    // if (queue.size() > QUEUE_MAX_SIZE) {
                    //
                    // }
                    queue.add(issue);
                }
            }

            while (queue.size() > QUEUE_MAX_SIZE) {
                queue.poll();
            }
        }

    }

    private String buildDsjtUrl(DrawConfig drawConfig) {
        int codeNum = 1;
        if (drawConfig.getLotteryCode().contains("FLB2SSC")) {
            codeNum = 2;
        }
        if (drawConfig.getLotteryCode().contains("FLB5SSC")) {
            codeNum = 3;
        }
        if (drawConfig.getLotteryCode().contains("HGSSC")) {
            codeNum = 4;
        }
        if (drawConfig.getLotteryCode().contains("DJSSC")) {
            codeNum = 5;
        }
        if (drawConfig.getLotteryCode().contains("SHOUERSSC")) {
            codeNum = 6;
        }
        if (drawConfig.getLotteryCode().contains("NEWYOSSC")) {
            codeNum = 7;
        }

        String url = "/api/get_lottery_numbers?ids=" + codeNum;
        return url;
    }

    private void getFrom6vcsTxssc(DrawConfig drawConfig, Queue<String> queue) {
        String url = "/Import/Online";
        String response = get(Tencent6vcsHost, url);

        ArrayList<Tencent6vcsIssue> issues = null;
        if (!StringUtils.isEmpty(response)) {
            response = response.replace("\\", "");
            response = response.substring(1, response.length() - 1);
            try {
                issues = gson.fromJson(response, new TypeToken<ArrayList<Tencent6vcsIssue>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (Tencent6vcsIssue entry : issues) {
            Date onlineDateTime = null;
            String issueStr = entry.getIssue();
            String index = issueStr.substring(issueStr.length() - 4, issueStr.length());
            String dateStr = issueStr.substring(0, issueStr.length() - 4);
            String issue = "20" + dateStr + "-" + index;
            onlineDateTime = new Date(Long.parseLong(entry.getTimeStamp()) * 1000);
            String onlineStr = SimpleParse.format(onlineDateTime);
            if (!queue.contains(issue)) {
                // 鏍煎紡鍖栧鏈�
                Tencent6vcsIssue tencentOnline = entry;
                String lotteryNumber = buildTencentNumber(tencentOnline.getOnlineNumber());

                notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, lotteryNumber, onlineStr,
                        String.valueOf(onlineDateTime.getTime() / 1000), null);

                // if (queue.size() > QUEUE_MAX_SIZE) {
                //
                // }
                queue.add(issue);
            }
        }

        while (queue.size() > 10) {
            queue.poll();
        }
    }

    private void getFrom77tj(DrawConfig drawConfig, Queue<String> queue) {
        String url = Tencent77tjHost+"/api/tencent/onlineim";
        
        
        String response = httpConnectionManager.get(url);

       // String response = get(Tencent77tjHost, url);
        logger.info("TXSSC spider:" + response);
        ArrayList<TencentOnline> issues = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                issues = gson.fromJson(response, new TypeToken<ArrayList<TencentOnline>>() {
                }.getType());
            } catch (Exception e) {
            }
        }

        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (TencentOnline entry : issues) {
            Date onlineDateTime = null;
            try {
                onlineDateTime = SimpleParse.parse(entry.getOnlinetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String issue = "";
            if (drawConfig.getLotteryCode().equals("TX30SSC")) {
                issue = buidlTencent30SSCIssue(entry.getOnlinetime(), onlineDateTime);
            } else {
                issue = buidlTencentIssue(entry.getOnlinetime(), onlineDateTime);
            }
            if (!queue.contains(issue)) {
                // 鏍煎紡鍖栧鏈�
                TencentOnline tencentOnline = entry;
                String onlineTime = tencentOnline.getOnlinetime();
                String lotteryNumber = buildTencentNumber(tencentOnline.getOnlinenumber());

                notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, lotteryNumber, onlineTime,
                        String.valueOf(onlineDateTime.getTime() / 1000), tencentOnline.getOnlinenumber());

                // if (queue.size() > QUEUE_MAX_SIZE) {
                //
                // }
                queue.add(issue);
            }
        }

        while (queue.size() > 10) {
            queue.poll();
        }

    }

    private String buidlTencent30SSCIssue(String onlinetime, Date onlineDateTime) {
        String dayTime = onlinetime.split(" ")[0].replaceAll("-", "");

        Calendar c = Calendar.getInstance();
        c.setTime(onlineDateTime);
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);

        int index = 0;
        if (h > 0 || m > 0) {
            index = h * 60 + m;
        } else {
            Integer dayTimeIndex = Integer.valueOf(dayTime);
            dayTime = String.valueOf(dayTimeIndex - 1);
            index = 1440;
        }
        index = index * 2;
        String issue = dayTime + "-" + String.format("%04d", index);
        return issue;
    }

    private static String buidlTencentIssue(String onlinetime, Date onlineDateTime) {
        String dayTime = onlinetime.split(" ")[0].replaceAll("-", "");

        Calendar c = Calendar.getInstance();
        c.setTime(onlineDateTime);
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);

        int index = 0;
        if (h > 0 || m > 0) {
            index = h * 60 + m;
        } else {
            Integer dayTimeIndex = Integer.valueOf(dayTime);
            dayTime = String.valueOf(dayTimeIndex - 1);
            index = 1440;
        }

        String issue = dayTime + "-" + String.format("%04d", index);

        return issue;
    }

    private static String buildTencentNumber(String onlineCount) {
        int size = onlineCount.length();
        char[] chars = new char[size];
        onlineCount.getChars(0, size, chars, 0);

        char[] nums = new char[5];

        int sum = 0;
        for (int i = 0; i < chars.length; i++) {
            if (i < 4) {
                nums[4 - i] = chars[size - i - 1];
            }

            sum += (chars[i] - 48);
        }
        nums[0] = (char) (sum % 10 + 48);

        String result = StringUtils.join(nums, ',');

        return result;
    }

    private void getFromCaipiaokong(DrawConfig drawConfig, Queue<String> queue) {
        String url = buildCaipiaokongUrl(drawConfig);

        String response = get(CaipiaokongHost, url);
        Map<String, CaipiaoKongBean> issues = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                issues = gson.fromJson(response, new TypeToken<Map<String, CaipiaoKongBean>>() {
                }.getType());
            } catch (Exception e) {
            }
        }

        if (issues == null) {
            return;
        }

        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (Entry<String, CaipiaoKongBean> entry : issues.entrySet()) {
            String issue = entry.getKey();
            String convertCpkIssue = convertCpkIssue(drawConfig.getLotteryCode(), issue);

            if (StringUtils.isNotBlank(convertCpkIssue)) {
                issue = convertCpkIssue;
            }
            if (!queue.contains(issue)) {
                // 鏍煎紡鍖栧鏈�
                CaipiaoKongBean caipiaoKong = entry.getValue();
                String timestamp = caipiaoKong.getDateline();
                try {
                    timestamp = SimpleParse.parse(caipiaoKong.getDateline()).getTime() / 1000 + "";
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issue, caipiaoKong.getNumber(),
                        caipiaoKong.getDateline(), timestamp, null);

                // if (queue.size() > QUEUE_MAX_SIZE) {
                //
                // }
                queue.add(issue);
            }
        }

        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }

    }

    private String convertCpkIssue(String lotteryCode, String issue) {
        if (lotteryCode.contains("GD11Y") || lotteryCode.contains("JSK3") || lotteryCode.contains("JS11Y")) {
            return "20" + issue;
        }
        return null;
    }

    private String buildCaipiaokongUrl(DrawConfig drawConfig) {
        String url = "/lottery/?format=json&name=" + drawConfig.getToCode() + "&uid=" + CaipiaokongUid + "&token="
                + CaipiaokongToken;
        return url;
    }

    private void getFromApiPlus(DrawConfig drawConfig, Queue<String> queue) {
        String url = buildApiPlus(drawConfig);

        String response = get(ApiPlusHost, url);
        logger.debug("getFromApiPlus lottery:{},json:{}", drawConfig.getLotteryCode(), response);
        ApiPlusBean bean = null;
        if (!StringUtils.isEmpty(response)) {
            try {
                bean = gson.fromJson(response, ApiPlusBean.class);
            } catch (Exception e) {
            }
        }

        if (bean == null) {
            return;
        }

        List<ApiPlushDataBean> issues = bean.getData();
        if (CollectionUtils.isEmpty(issues)) {
            return;
        }

        for (ApiPlushDataBean issueBean : issues) {
            if (!queue.contains(issueBean.getExpect())) {
                // 鏍煎紡鍖栧鏈�
                notifyNewIssue(drawConfig.getLotteryCode(), drawConfig, issueBean.getExpect(), issueBean.getOpencode(),
                        issueBean.getOpentime(), issueBean.getOpentimestamp(), null);

                // if (queue.size() > QUEUE_MAX_SIZE) {
                //
                // }
                queue.add(issueBean.getExpect());
            }
        }

        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.poll();
        }

    }

    private void notifyNewIssue(String lotteryCode, DrawConfig drawConfig, String issue, String openCode, String time,
            String timestamp, String yuliu) {
        // convert..

        String code = openCode;
        String lottery = lotteryCode;

        if (drawConfig.getConverter() != null) {
            issue = drawConfig.getConverter().convert(issue);
        }
        logger.info("NotifyNewIssue lottery:{},issue:{},code:{}", lotteryCode, issue, openCode);

        DrawResult drawResult = new DrawResult(lottery, issue, code, time, timestamp, yuliu);

        String json = gson.toJson(drawResult);

        Boolean added = jedisTemplate.hsetnx(buildStoreIssueKey(lottery, issue), issue, json);
        if (added) {
            for (String string : bossGroup.getPlatforms()) {
                jedisTemplate.lpush(string, json);
            }
        }

    }

    public String buildStoreIssueKey(String lottery, String issue) {
        return "LotteryCode:" + lottery;
    }

    private String buildApiPlus(DrawConfig drawConfig) {
        String type = "last";

        if (type.equals("last")) {
            String url = "/newly.do?token=" + ApiPlusToken + "&format=json&code=" + drawConfig.getToCode();
            return url;

        }

        return "";
    }

    public boolean listen(PlatformConfig platformConfig) {
        String redisjson = null;
        try {
            redisjson = jedisTemplate.brpop(10, platformConfig.getName());

            if (redisjson == null) {
                return false;
            }

            DrawResult drawResult = gson.fromJson(redisjson, DrawResult.class);

            LotteryDrawRequestBean bean = LotteryDrawRequestBean.convert(drawResult);

            convertLottery(bean);

            logger.debug("tuisong:" + gson.toJson(bean));

            String url = getPlatformUrl(platformConfig);
            String result = post(url, gson.toJson(bean));

            if (StringUtils.isEmpty(result)) {
                toErrorList(redisjson, platformConfig);
                return false;
            }

            LotteryDrawResponseBean resultBean = gson.fromJson(result, LotteryDrawResponseBean.class);

            int rc = resultBean.getRc();

            // 寮�濂栧け璐�
            if (rc == -10) {
                toErrorList(redisjson, platformConfig);
            }

        } catch (Exception e) {
            logger.error("Error listening..platform:{}", platformConfig.getName());
            toErrorList(redisjson, platformConfig);
            return false;
        }

        return true;
    }

    private void convertLottery(LotteryDrawRequestBean bean) {
        // if (bean.getLotteryCode().contains("HGSSC")) {
        // bean.setLotteryNumber(buildKoreanCode(bean.getLotteryNumber()));
        // } else if (bean.getLotteryCode().equals("XJPSSC") || bean.getLotteryCode().contains("DJSSC")
        // || bean.getLotteryCode().equals("BJKL8SSC") || bean.getLotteryCode().equals("TWSSC")
        // || bean.getLotteryCode().equals("CNDSSC")) {
        // bean.setLotteryNumber(buildKoreanCode(bean.getLotteryNumber()));
        // } else if (bean.getLotteryCode().equals("BJKLCXJW")) {
        // if (bean.getLotteryNumber().contains("+")) {
        // String[] codes = bean.getLotteryNumber().split("\\+");
        // bean.setLotteryNumber(codes[0]);
        // }
        // } else if (bean.getLotteryCode().contains("FLB15SSC") || bean.getLotteryCode().contains("FLB2SSC")
        // || bean.getLotteryCode().contains("FLB5SSC")) {
        // bean.setLotteryNumber(buildFLBSSCCode(bean.getLotteryNumber()));
        // } else if (bean.getLotteryCode().contains("NEWYOSSC")) {
        // bean.setLotteryNumber(buildNEWYOSSCCode(bean.getLotteryNumber()));
        // } else if (bean.getLotteryCode().contains("KLC3DFC")) {
        // bean.setLotteryNumber(buildKLC3DFCCode(bean.getLotteryNumber()));
        // }
        if (bean.getLotteryCode().equals("XJPSSC") || bean.getLotteryCode().equals("BJKL8SSC")
                || bean.getLotteryCode().equals("TWSSC") || bean.getLotteryCode().equals("CNDSSC")) {
            bean.setLotteryNumber(buildKoreanCode(bean.getLotteryNumber()));
        } else if (bean.getLotteryCode().equals("BJKLCXJW")) {
            if (bean.getLotteryNumber().contains("+")) {
                String[] codes = bean.getLotteryNumber().split("\\+");
                bean.setLotteryNumber(codes[0]);
            }
        } else if (bean.getLotteryCode().contains("KLC3DFC")) {
            bean.setLotteryNumber(buildKLC3DFCCode(bean.getLotteryNumber()));
        }
    }

    private String buildKLC3DFCCode(String codeNode) {
        if (codeNode.contains("+")) {
            String[] codes = codeNode.split("\\+");
            codeNode = codes[0];
        }

        String[] numStrs = codeNode.split(",");
        Integer[] codes = new Integer[numStrs.length];

        for (int i = 0; i < numStrs.length; i++) {
            codes[i] = Integer.valueOf(numStrs[i]);
        }

        Arrays.sort(codes);

        int[] resultNums = new int[3];

        for (int i = 0; i < codes.length - 2; i++) {
            resultNums[i / 6] += codes[i];
        }

        for (int i = 0; i < resultNums.length; i++) {
            resultNums[i] = resultNums[i] % 10;
        }
        String resultCode = StringUtils.join(resultNums, ',');
        return resultCode;
    }

    private String getPlatformUrl(PlatformConfig platformConfig) {
        return platformConfig.getUrl();
    }

    public String get(String host, String queryParams) {
        HttpHost target = HttpHost.create(host);

        ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
        String result = null;
        try {
            Future<BasicPoolEntry> future = pool.lease(target, null);

            boolean reusable = false;
            BasicPoolEntry entry = future.get();
            try {
                HttpClientConnection conn = entry.getConnection();
                conn.setSocketTimeout(CLIENT_SOCKET_TIMEOUT);
                HttpCoreContext coreContext = HttpCoreContext.create();
                coreContext.setTargetHost(target);

                BasicHttpRequest request = new BasicHttpRequest("GET", queryParams);
                logger.debug(">> Request URI: " + request.getRequestLine().getUri());

                httpexecutor.preProcess(request, httpproc, coreContext);
                HttpResponse response = httpexecutor.execute(request, conn, coreContext);
                httpexecutor.postProcess(response, httpproc, coreContext);

                logger.debug("<< Response: " + response.getStatusLine());

                result = EntityUtils.toString(response.getEntity(), "UTF-8");

                reusable = connStrategy.keepAlive(response, coreContext);

            } catch (IOException ex) {
                logger.error("Error getting response,host:{},url:{}", host, queryParams, ex);
            } catch (HttpException ex) {
                logger.error("Error getting response,host:{},url:{}", host, queryParams, ex);
            } finally {
                // if (reusable) {
                // System.out.println("Connection kept alive...");
                // }
                pool.release(entry, reusable);
            }
        } catch (Exception ex) {
            logger.error("Error getting response,host:{},url:{}", host, queryParams, ex);
        }

        return result;
    }

    private void toErrorList(String redisjson, PlatformConfig platformConfig) {
        if (!StringUtils.isEmpty(redisjson)) {
            if (platformConfig.getName().contains("error")) {
                jedisTemplate.lpush(platformConfig.getName(), redisjson);
            } else {
                jedisTemplate.lpush(platformConfig.getName() + "_error", redisjson);
            }
        }

    }

    public String post(String url, String dataJson) {
        ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("data", dataJson));
        UrlEncodedFormEntity postEntity;
        try {
            postEntity = new UrlEncodedFormEntity(formparams);
            httpPost.setEntity(postEntity);

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String rsp = EntityUtils.toString(entity, "UTF-8");
            return rsp;

        } catch (IOException e) {

        } catch (Exception e) {

        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
        }

        return null;
    }

    private static String buildNEWYOSSCCode(String codeNode) {
        String[] numStrs = codeNode.split(",");

        int[] resultNums = new int[5];

        for (int i = 0; i < 5; i++) {
            resultNums[i] = Integer.valueOf(numStrs[i]);
        }
        String resultCode = StringUtils.join(resultNums, ',');
        return resultCode;
    }

    private static String buildFLBSSCCode(String codeNode) {
        if (codeNode.contains("+")) {
            String[] codes = codeNode.split("\\+");
            codeNode = codes[0];
        }

        String[] numStrs = codeNode.split(",");
        Integer[] codes = new Integer[numStrs.length];

        for (int i = 0; i < numStrs.length; i++) {
            codes[i] = Integer.valueOf(numStrs[i]);
        }

        int[] resultNums = new int[5];

        for (int i = 0; i < codes.length; i++) {
            resultNums[i / 4] += codes[i];
        }

        for (int i = 0; i < resultNums.length; i++) {
            resultNums[i] = resultNums[i] % 10;
        }

        String resultCode = StringUtils.join(resultNums, ',');

        return resultCode;
    }

    private static String buildKoreanCode(String codeNode) {
        if (codeNode.contains("+")) {
            String[] codes = codeNode.split("\\+");
            codeNode = codes[0];
        }

        String[] numStrs = codeNode.split(",");
        Integer[] codes = new Integer[numStrs.length];

        for (int i = 0; i < numStrs.length; i++) {
            codes[i] = Integer.valueOf(numStrs[i]);
        }

        Arrays.sort(codes);

        int[] resultNums = new int[5];

        for (int i = 0; i < codes.length; i++) {
            resultNums[i / 4] += codes[i];
        }

        for (int i = 0; i < resultNums.length; i++) {
            resultNums[i] = resultNums[i] % 10;
        }

        String resultCode = StringUtils.join(resultNums, ',');

        return resultCode;
    }
    
    public static String formatLow10OpenCode(String code){
   	    String[] numStrs = code.split(",");

        String[] resultNums = new String[numStrs.length];

        for (int i = 0; i < numStrs.length; i++) {
        	String tempStr = String.valueOf(numStrs[i]);
        	int a = Integer.valueOf(tempStr);
        
        	if(a<10) {
        		tempStr="0"+a;
        	}
            resultNums[i] = tempStr;
        }
        String resultCode = StringUtils.join(resultNums, ',');
        return resultCode;
   }
    
    public static String formatOpenCode(String code){
    	 String[] numStrs = code.split("");

         int[] resultNums = new int[numStrs.length];

         for (int i = 0; i < numStrs.length; i++) {
             resultNums[i] = Integer.valueOf(numStrs[i]);
         }
         String resultCode = StringUtils.join(resultNums, ',');
         return resultCode;
    }

    public static void main1(String[] args) throws ParseException {
        String codeNode = "03,15,16,17,22,23,25,33,35,37,38,47,51,61,63,67,69,73,75,77+02";
        if (codeNode.contains("+")) {
            String[] codes = codeNode.split("\\+");
            codeNode = codes[0];
        }

        String[] numStrs = codeNode.split(",");
        Integer[] codes = new Integer[numStrs.length];

        for (int i = 0; i < numStrs.length; i++) {
            codes[i] = Integer.valueOf(numStrs[i]);
        }

        Arrays.sort(codes);

        int[] resultNums = new int[3];

        for (int i = 0; i < codes.length - 2; i++) {
            resultNums[i / 6] += codes[i];
        }

        for (int i = 0; i < resultNums.length; i++) {
            resultNums[i] = resultNums[i] % 10;
        }

        String resultCode = StringUtils.join(resultNums, ',');
        System.out.println(resultCode);
        // String issue = "20170329018";
        // String substring = issue.substring(0, issue.length() - 3);
        // String substring2 = issue.substring(substring.length(), issue.length());
        // System.out.println(substring);
        // System.out.println(substring2);
        // System.out.println(System.currentTimeMillis());
    }

    public static void main(String[] args) throws ParseException {
    
        // Calendar c = Calendar.getInstance();
        // c.setTimeInMillis(new Date().getTime());
        // System.out.println(c.getTime());
        // String buildKoreanCode = buildKoreanCode("01,03,04,09,22,23,32,35,37,44,49,55,57,58,59,61,67,75,76,80+09");
        // System.out.println(buildKoreanCode);
        // String buildTencentNumber = "311107253";
        // String[] arr = buildTencentNumber.split("");
        // int sum = 0;
        // for (String str : arr) {
        // sum += Integer.parseInt(str);
        // }
        // System.out.println(sum);
        // String str =
        // "1b82211c0aa8e5b05a383227146fd840201b7b01905fc3499028eacf2b0debd96d79ae51c85f4c5d6fa4af34a5d0f8c1afc3b49bee0a74a69145e575ec022000";
        // str = str.trim();
        // List<Integer> list = new ArrayList<>();
        // if (str != null && !"".equals(str)) {
        // for (int i = 0; i < str.length(); i++) {
        // if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
        // Integer charAt = str.charAt(i) - '0';
        // list.add(charAt);
        // }
        // }
        // }
        //
        // List<String> codeList = new ArrayList<>();
        //
        // for (Integer num : list) {
        // if (codeList.size() == 10) {
        // break;
        // }
        // String kk = null;
        // if (num.intValue() == 0) {
        // kk = "10";
        // } else if (num.intValue() < 10) {
        // kk = "0" + num;
        // }
        // if (!codeList.contains(kk)) {
        // codeList.add(kk);
        // }
        // }
        // System.out.println(StringUtils.join(codeList, ","));
        // String str = "2018-08-11 00:00:00";
        // Date date = SimpleParse.parse(str);
        // Calendar c = Calendar.getInstance();
        // c.setTime(date);
        // int hour = c.get(Calendar.HOUR_OF_DAY);
        // int min = c.get(Calendar.MINUTE);
        // // String kk = str.trim().split(" ")[1].split(":")[1];
        // // int h = Integer.parseInt("02");
        // int kk = hour * 60 + min;
        // System.out.println(min);
        // System.out.println(kk);
        // System.out.println(kk % 5);
        // System.out.println(kk / 5);
        // if (min == 0 && hour == 0) {
        // c.add(Calendar.DATE, -1);
        // String dateStr = SimpleParseByDate.format(c.getTime());
        // System.out.println("dateStr:" + dateStr);
        // }
        // System.out.println(String.format("%03d", 4));
        // String[] codeArr = "02,08,07,03,01,10,04,09,05,06".split(",");
        // int[] numberArr = new int[5];
        // numberArr[0] = (Integer.parseInt(StringUtils.trim(codeArr[0])) + Integer.parseInt(StringUtils.trim(codeArr[1])))
        // % 10;
        // numberArr[1] = (Integer.parseInt(StringUtils.trim(codeArr[2])) + Integer.parseInt(StringUtils.trim(codeArr[3])))
        // % 10;
        // numberArr[2] = (Integer.parseInt(StringUtils.trim(codeArr[4])) + Integer.parseInt(StringUtils.trim(codeArr[5])))
        // % 10;
        // numberArr[3] = (Integer.parseInt(StringUtils.trim(codeArr[6])) + Integer.parseInt(StringUtils.trim(codeArr[7])))
        // % 10;
        // numberArr[4] = (Integer.parseInt(StringUtils.trim(codeArr[8])) + Integer.parseInt(StringUtils.trim(codeArr[9])))
        // % 10;
        // String s = StringUtils.join(numberArr, ',');
        // System.out.println(s.toString());
        // System.out.println(3 / 2);

        // String[] numStrs = "04,05,08,15,16,22,24,25,26,33,35,36,40,41,44,45,49,51,67,69".split(",");
        //
        // int[] resultNums = new int[5];
        //
        // for (int i = 0; i < 5; i++) {
        // resultNums[i] = Integer.valueOf(numStrs[i]);
        // }
        // String resultCode = StringUtils.join(resultNums, ',');
        // System.out.println(resultCode);
        // String buildTencentNumber = buildTencentNumber("333070926");
        // System.out.println(buildTencentNumber);
        // Date d = new Date(1557292872000L);
        // System.out.println(d);
        //long currentTimeMillis = System.currentTimeMillis();
//        String issue = String.format("%04d", 1003);
//        System.out.println(issue);
        // 1574066468
//   	 String[] numStrs = "98786".split("");
//
//     int[] resultNums = new int[numStrs.length];
//
//     for (int i = 0; i < resultNums.length; i++) {
//         resultNums[i] = Integer.valueOf(numStrs[i]);
//     }
//     String resultCode = StringUtils.join(resultNums, ',');
//     System.out.println(resultCode);
    	int a = 7;
    	int b=5;
    	int c=(a+b)%10;
    	System.out.println(c);
    }

}
