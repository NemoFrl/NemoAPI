package nemofrl.nemoapi.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import nemofrl.nemoapi.config.NemoAPIConfig;
import nemofrl.nemoapi.entity.RespDTO;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.impl.DownloadServiceImpl;

@RestController
public class YoutubeController {

	@Autowired
	private NemoAPIConfig nemoAPIConfig;

	private static final Logger logger = LogManager.getLogger(YoutubeController.class);
	
	private DownloadServiceImpl downloadServiceImpl;

	@RequestMapping(value = "/getVideoList", produces = { "application/json;charset=UTF-8" })
	public RespDTO getVideoList(HttpServletResponse resp,String search,Integer pageSize,String pageToken){
		RespDTO respDTO=new RespDTO("请求成功",RespDTO.SUCCESS,null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		
		Builder builder = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000);
		if(nemoAPIConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(nemoAPIConfig.getProxyIp(), nemoAPIConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		String encodeSearch =UriUtils.encode(search,StandardCharsets.UTF_8.name());
		
		HttpClient httpClient=HttpClientBuilder.create().build();
		
		HttpGet httpGet=new HttpGet("https://www.googleapis.com/youtube/v3/search?part=snippet&key=AIzaSyDBAUG0ZW6lA4EmGFBrXYEmVVlioyfkucA"
				+ "&q="+encodeSearch+"&maxResults="+pageSize+"&pageToken="+pageToken);
		httpGet.setConfig(requestConfig);
		String json=null;
		try {
			HttpResponse httpResp = httpClient.execute(httpGet);
			if(httpResp.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
				json = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			else {
				respDTO.setCode(NemoAPIException.ERROR_NETSTATUS);
				respDTO.setMsg("请求谷歌搜索YouTube列表API失败,请稍后重试");
				return respDTO;
			}
		} catch (IOException e) {
			logger.error("req google api error",e);
			respDTO.setCode(NemoAPIException.ERROR_NETCONNECT);
			respDTO.setMsg("请求谷歌搜索YouTube列表API失败,请稍后重试");
			return respDTO;
		}
		
		Gson gson=new Gson();
		
		Map<String,Object> map=gson.fromJson(json, Map.class);
		List<LinkedTreeMap> items=(List<LinkedTreeMap>) map.get("items");
		List<Map<String,Object>> resultItems=new ArrayList<Map<String,Object>>();
		
		for(int i=0;i<items.size();i++) {
			Map<String,Object> resultItem=new HashMap<String, Object>();
			LinkedTreeMap item=items.get(i);
			LinkedTreeMap id=(LinkedTreeMap) item.get("id");
			String videoId=(String) id.get("videoId");
			LinkedTreeMap snippet = (LinkedTreeMap) item.get("snippet");
			String title=(String) snippet.get("title");
			resultItem.put("title", title);
			resultItem.put("videoId", videoId);
			resultItems.add(resultItem);
		}
		respDTO.setData(resultItems);
		return respDTO;
	}

	@RequestMapping(value = "/downloadVideo", produces = { "application/json;charset=UTF-8" })
	public RespDTO downloadVideo(HttpServletResponse resp,String videoId) {
		RespDTO respDTO=new RespDTO("请求成功",RespDTO.SUCCESS,null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		
		downloadServiceImpl=new DownloadServiceImpl(nemoAPIConfig, "https://www.youtube.com/watch?v="+videoId);
		
		String title=downloadServiceImpl.getTitle();
		
		if(title==null) {
			respDTO.setCode(NemoAPIException.ERROR_NETCONNECT);
			respDTO.setMsg("请求谷歌下载YouTube视频API失败,请稍后重试");
			return respDTO;
		}
		
		title=title.replaceAll("\\?", "");
		
		downloadServiceImpl.setTitle(title);
		
		new Thread(downloadServiceImpl).start();
		
		String encodeTitle = UriUtils.encode(title,StandardCharsets.UTF_8.name());
		
		String url= "/video/"+encodeTitle+".mp4";
		
		respDTO.setData(url);
		
		return respDTO;
	}
	
	@RequestMapping(value = "/getDownloadStatus", produces = { "application/json;charset=UTF-8" })
	public RespDTO getDownloadStatus(HttpServletResponse resp) {
		RespDTO respDTO=new RespDTO("请求成功",RespDTO.SUCCESS,null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		
		String status = downloadServiceImpl.getStatus();
		
		respDTO.setData(status);
		
		return respDTO;
	}
	

}
