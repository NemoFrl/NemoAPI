package nemofrl.nemoapi.service.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nemofrl.nemoapi.config.NemoAPIConfig;
import nemofrl.nemoapi.entity.DstServerListVO;
import nemofrl.nemoapi.entity.DstServerVO;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.DstServerService;

@Service("dstServerService")
public class DstServerServiceImpl implements DstServerService{
	@Autowired
	private NemoAPIConfig nemoAPIConfig;
	
	private static final Logger logger = LogManager.getLogger(DstServerServiceImpl.class);
	
	private List<DstServerVO> dstServerVOList;
	private Date cacheTime;
	public void getServerList() throws NemoAPIException {
		Builder builder = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000);
		if (nemoAPIConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(nemoAPIConfig.getProxyIp(), nemoAPIConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("https://d2fr86khx60an2.cloudfront.net/China-Steam-noevent.json.gz");
		httpGet.setConfig(requestConfig);
		String result;
		try {
			HttpResponse resp = httpClient.execute(httpGet);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				result = EntityUtils.toString(resp.getEntity(), "UTF-8");
			else throw new NemoAPIException("klei api响应码错误", NemoAPIException.ERROR_NETSTATUS);
		} catch (IOException e) {
			throw new NemoAPIException("请求klei api失败", NemoAPIException.ERROR_NETCONNECT, e);
		}
		Gson gson=new Gson();
		DstServerListVO dstServerListVO=gson.fromJson(result, DstServerListVO.class);
		dstServerVOList = dstServerListVO.getGET();
		cacheTime=new Date();
	}



	@Override
	public Map<String,Object> getServerListFromCache(int currentPage,int pageSize,String search) throws NemoAPIException {
		Map<String,Object> result=new HashMap<String, Object>();
		Date now=new Date();
		long cachedTime=1000*30;
		long dataLifeTime=cachedTime;
		if(cacheTime!=null)
			dataLifeTime=now.getTime()-cacheTime.getTime();
		if(dataLifeTime>=cachedTime||dstServerVOList==null||dstServerVOList.size()==0) {
			logger.info("数据已过时，重新拉取新数据");
			getServerList();
		} else logger.info("数据未过时，读取缓存数据");
		int recordCount=dstServerVOList.size();
		int pageCount=(recordCount/pageSize)==(1.0*recordCount/pageSize)?(recordCount/pageSize):(recordCount/pageSize+1);
		List<DstServerVO> pageList=new ArrayList<>(); 
		List<DstServerVO> afterSearch=new ArrayList<>();
		if(StringUtils.isNotBlank(search)) {
			for(int i=0;i<dstServerVOList.size();i++) {
				String parent=dstServerVOList.get(i).getName();
				Pattern pattern=Pattern.compile("^.*"+search+".*$");
				Matcher matcher=pattern.matcher(parent);
				if(matcher.find()) {
					afterSearch.add(dstServerVOList.get(i));
				}
			}
			if(currentPage<=pageCount) {
				int startIndex=(currentPage-1)*pageSize;
				int endIndex=currentPage*pageSize>afterSearch.size()?afterSearch.size():currentPage*pageSize;
				pageList=afterSearch.subList(startIndex, endIndex);
			}
		} else {
			if(currentPage<=pageCount) {
				int startIndex=(currentPage-1)*pageSize;
				int endIndex=currentPage*pageSize>recordCount?recordCount:currentPage*pageSize;
				pageList=dstServerVOList.subList(startIndex, endIndex);
			}
		}
		result.put("rows", pageList);
		result.put("total", recordCount);
		return result;
	}



	@Override
	public Map<String, Object> getServerInfo(String token, String rowId) throws NemoAPIException {
		Builder builder = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000);
		if (nemoAPIConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(nemoAPIConfig.getProxyIp(), nemoAPIConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://lobby-china.kleientertainment.com/lobby/read");
		httpPost.setConfig(requestConfig);
		
		Map<String,Object> params=new HashMap<>();
		Map<String,String> query=new HashMap<>();
		query.put("__rowId", rowId);
		params.put("__gameId", "DontStarveTogether");
		params.put("__token", token);
		params.put("query", query);
		
		Gson gson=new Gson();
		HttpEntity entity=new StringEntity(gson.toJson(params),StandardCharsets.UTF_8);
		httpPost.setEntity(entity);
		String result;
		try {
			HttpResponse resp = httpClient.execute(httpPost);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				result = EntityUtils.toString(resp.getEntity(), "UTF-8");
			else throw new NemoAPIException("klei api响应码错误", NemoAPIException.ERROR_NETSTATUS);
		} catch (IOException e) {
			throw new NemoAPIException("请求klei api失败", NemoAPIException.ERROR_NETCONNECT, e);
		}
		DstServerListVO voList=gson.fromJson(result, DstServerListVO.class);
		
		Map<String,Object> returnResult=new HashMap<String, Object>();
		if(voList.getGET()==null||voList.getGET().size()==0) {
			returnResult.put("serverData", new HashMap());
			returnResult.put("players", new ArrayList());
			return returnResult;
		}
		DstServerVO vo=voList.getGET().get(0);
		String data=vo.getData();
		String players=vo.getPlayers();
		data=data.replace("return", "");
		data=data.replaceAll("=", ":");
		players=players.replace("return", "");
		players=players.replaceAll("=", ":");
		int fristLeft=players.indexOf("{");
		players=players.substring(fristLeft+1, players.length()-1);
		
		
		data=data.replaceAll("\\[", "");
		data=data.replaceAll("\\]", "");
		//data=data.replaceAll("\"", "");
		
		players=players.replaceAll("\\[", "");
		players=players.replaceAll("\\]", "");
		//players=players.replaceAll("\"", "");
		
		players="["+players+"]";
		Type type=new TypeToken<List<Map<String, String>>>() {}.getType();
		Map serverDataMap=gson.fromJson(data, Map.class);
		List playersMap=gson.fromJson(players, type);
//		Pattern p=Pattern.compile("\\[\\\".{0,25}\\\"\\]=.*?[, ]");
//		Matcher m=p.matcher(data);
//		
//		Map<String,Integer> serverDataMap=new HashMap<>();
//		
//		while(m.find()) {
//			String find=m.group();
//			String num=find.substring(find.indexOf("=")+1);
//			String key=find.substring(find.indexOf("\"")+1,find.lastIndexOf("=")-2);
//			num=num.replaceAll(" ", "");
//			num=num.replaceAll(",", "");
//			
//			serverDataMap.put(key, Integer.valueOf(num));
//		}
//		
//		m=p.matcher(players);
//		Map<String,String> playersMap=new HashMap<>();
//		
//		while(m.find()) {
//			String find=m.group();
//			String value=find.substring(find.indexOf("=")+1);
//			String key=find.substring(find.indexOf("\"")+1,find.lastIndexOf("=")-2);
//			value=value.replaceAll(" ", "");
//			value=value.replaceAll(",", "");
//			value=value.replaceAll("\"", "");
//			playersMap.put(key, value);
//		}
		returnResult.put("serverData", serverDataMap);
		returnResult.put("players", playersMap);
		return returnResult;
	}
	 
}
