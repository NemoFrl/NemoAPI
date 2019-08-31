package nemofrl.nemoapi.service.impl;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nemofrl.nemoapi.config.NemoAPIConfig;
import nemofrl.nemoapi.entity.DstServerListVO;
import nemofrl.nemoapi.entity.DstServerVO;
import nemofrl.nemoapi.entity.IpVO;
import nemofrl.nemoapi.entity.StreamUserInfo;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.DstServerService;
import nemofrl.nemoapi.service.StreamService;
import nemofrl.nemoapi.util.HttpUtil;
import nemofrl.nemoapi.util.HttpUtil.HttpMethod;

@Service("dstServerService")
public class DstServerServiceImpl implements DstServerService{
	@Autowired
	private NemoAPIConfig nemoAPIConfig;
	
	private static final Logger logger = LogManager.getLogger(DstServerServiceImpl.class);
	
	private List<DstServerVO> dstServerVOList;
	
	@Autowired
	private StreamService streamService;

	@Autowired
	private HttpUtil httpUtil;
	
	private Date cacheTime;
	
	public void getServerList() throws NemoAPIException {
		String url="https://d2fr86khx60an2.cloudfront.net/China-Steam-noevent.json.gz";
		String result=httpUtil.httpReq(true, url, HttpMethod.GET,null);
		Gson gson=new Gson();
		DstServerListVO dstServerListVO=gson.fromJson(result, DstServerListVO.class);
		dstServerVOList = dstServerListVO.getGET();
		cacheTime=new Date();
	}



	@Override
	public Map<String,Object> getServerListFromCache(int currentPage,int pageSize,String search) throws NemoAPIException {
		Map<String,Object> result=new HashMap<String, Object>();
//		Date now=new Date();
//		long cachedTime=1000*30;
//		long dataLifeTime=cachedTime;
//		if(cacheTime!=null)
//			dataLifeTime=now.getTime()-cacheTime.getTime();
//		if(dataLifeTime>=cachedTime||dstServerVOList==null||dstServerVOList.size()==0) {
//			logger.info("数据已过时，重新拉取新数据");
//			getServerList();
//		} else logger.info("数据未过时，读取缓存数据");
		if(dstServerVOList==null||dstServerVOList.size()==0) {
			logger.info("服务器数据为空，拉取新数据");
			getServerList();
		}
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
			recordCount=afterSearch.size();
			pageCount=(recordCount/pageSize)==(1.0*recordCount/pageSize)?(recordCount/pageSize):(recordCount/pageSize+1);
			if(currentPage<=pageCount) {
				int startIndex=(currentPage-1)*pageSize;
				int endIndex=currentPage*pageSize>recordCount?recordCount:currentPage*pageSize;
				pageList=afterSearch.subList(startIndex, endIndex);
			}
		} else {
			if(currentPage<=pageCount) {
				int startIndex=(currentPage-1)*pageSize;
				int endIndex=currentPage*pageSize>recordCount?recordCount:currentPage*pageSize;
				pageList=dstServerVOList.subList(startIndex, endIndex);
			}
		}
		
		ExecutorService es=Executors.newFixedThreadPool(10);
		List<Future<IpVO>> ftIpVOList = new ArrayList<Future<IpVO>>(); 
		for(int i=0;i<pageList.size();i++) {
			DstServerVO dstServerVO=pageList.get(i);
			Future<IpVO> ftIpVO=es.submit(new IpInfoExecutor(dstServerVO.get__addr()));
			ftIpVOList.add(ftIpVO);
		}
		 //遍历任务的结果
        for (Future<IpVO> fs : ftIpVOList) { 
                try { 
                	IpVO ipVO = fs.get();
                	if(ipVO.getStatus().equals("success")) {
                		for(DstServerVO dstServerVO:pageList) {
                			if(dstServerVO.get__addr().equals(ipVO.getQuery())) {
		            			dstServerVO.setCity(ipVO.getCity());
		            			dstServerVO.setCountry(ipVO.getCountry());
		            			dstServerVO.setOrg(ipVO.getOrg());
		            			dstServerVO.setRegionName(ipVO.getRegionName());
		            			break;
                			}
                		}
            		} 
                } catch (InterruptedException e) { 
                        e.printStackTrace(); 
                } catch (ExecutionException e) { 
                        e.printStackTrace(); 
                } finally { 
                        //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。
                	es.shutdown(); 
                } 
        } 
		//IpVO ipVO=getIpInfo(dstServerVO.get__addr());
		
		result.put("rows", pageList);
		result.put("total", recordCount);
		return result;
	}



	@Override
	public Map<String, Object> getServerInfo(String token, String rowId) throws NemoAPIException {
		
		Map<String,Object> params=new HashMap<>();
		Map<String,String> query=new HashMap<>();
		if(StringUtils.isBlank(token))
			token=nemoAPIConfig.getDstToken();
		query.put("__rowId", rowId);
		params.put("__gameId", "DontStarveTogether");
		params.put("__token", token);
		params.put("query", query);
		
		Gson gson=new Gson();
		HttpEntity entity=new StringEntity(gson.toJson(params),StandardCharsets.UTF_8);
		
		String url="https://lobby-china.kleientertainment.com/lobby/read";
		String result=httpUtil.httpReq(true, url, HttpMethod.POST,entity);
		logger.info("dstsever detail："+result);
		
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
		Type type=new TypeToken<List<Map<String, Object>>>() {}.getType();
		Type type2=new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> serverDataMap=gson.fromJson(data, type2);
		List<Map<String, Object>> playersMap=gson.fromJson(players, type);
		String streamids="";
		for(int i=0;i<playersMap.size();i++) {
			Map<String, Object> map=playersMap.get(i);
			streamids+=(String) map.get("netid")+",";
		}
		List<StreamUserInfo> infoList=streamService.getStreamUserInfo(streamids);
		for(int i=0;i<playersMap.size();i++) {
			Map<String, Object> map=playersMap.get(i);
			StreamUserInfo currentInfo=null;
			for(StreamUserInfo info:infoList) {
				if(info.getSteamid().equals(map.get("netid"))) {
					currentInfo=info;
					break;
				}
			}
			String infoStr=gson.toJson(currentInfo);
			Map<String, Object> infoMap=gson.fromJson(infoStr, type2);
			map.putAll(infoMap);
		}
		returnResult.put("serverData", serverDataMap);
		returnResult.put("players", playersMap);
		return returnResult;
	}



	@Override
	public DstServerVO searchDstPlayer(String userName) throws NemoAPIException {
		Gson gson=new Gson();
		Map<String,Object> params=new HashMap<>();
		Map<String,String> query=new HashMap<>();
		
		for(int i=0;i<dstServerVOList.size();i++) {
			DstServerVO dstServerVO=dstServerVOList.get(i);
			if(dstServerVO.getConnected()==0)
				continue;
			String rowId=dstServerVO.get__rowId();

			String token=nemoAPIConfig.getDstToken();
			query.put("__rowId", rowId);
			params.put("__gameId", "DontStarveTogether");
			params.put("__token", token);
			params.put("query", query);
			
			HttpEntity entity=new StringEntity(gson.toJson(params),StandardCharsets.UTF_8);
			
			String url="https://lobby-china.kleientertainment.com/lobby/read";
			String result=httpUtil.httpReq(true, url, HttpMethod.POST,entity);
			if(result.contains(userName))
				return dstServerVO;
		}
		return null;
	}
	
}
