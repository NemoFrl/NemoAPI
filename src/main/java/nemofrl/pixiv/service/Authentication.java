package nemofrl.pixiv.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nemofrl.pixiv.config.PixivConfig;
import nemofrl.pixiv.exception.PixivException;

@Component
public class Authentication{
	
	@Autowired
	private PixivConfig pixivConfig;
	
	public Map<String,String> getPostKeyAndCookie() throws PixivException, IOException{
		
		Map<String,String> map=new HashMap<String,String>();
		CookieStore cookieStore = new BasicCookieStore();
		Builder builder= RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000);
		if(pixivConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(pixivConfig.getProxyIp(), pixivConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
		HttpGet get = new HttpGet("https://accounts.pixiv.net/login?lang=zh&source=pc&view_type=page&ref=wwwtop_accounts_index");
		get.setConfig(requestConfig);
		try {
			HttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				String encodeJson = EntityUtils.toString(response.getEntity(), "UTF-8");
				//System.out.println(encodeJson);
				Pattern pattern1 = Pattern.compile("name=\"post_key\" value=\".{0,40}\">");
				Matcher matcher1 = pattern1.matcher(encodeJson);
				String postKey=null;
				if (matcher1.find()) {
					String find = matcher1.group();
					postKey=find.substring(23, find.length()-2);
				}else throw new PixivException("匹配postKey失败",PixivException.ERROR_MATCHER);
				List<Cookie> cookies = cookieStore.getCookies();
	            StringBuffer tmpcookies = new StringBuffer();
	            for (Cookie c : cookies) {
	                tmpcookies.append(c.getName()+"="+c.getValue() + ";");
	            }
	            map.put("postKey", postKey);
	            map.put("cookies", tmpcookies.toString());
				return map;
			} else throw new PixivException("http请求失败",PixivException.ERROR_HTTPSTATUS);
			
		} catch(ClientProtocolException e) {
			throw new PixivException("ClientProtocolException",PixivException.ERROR_HTTPCLIENT,e);
		} 
	}
	public String getCookie(Map<String,String> map,String username,String password) throws PixivException, IOException{
		Builder builder= RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000);
		if(pixivConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(pixivConfig.getProxyIp(), pixivConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		CookieStore cookieStore = new BasicCookieStore();
		HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
		HttpPost post = new HttpPost("https://accounts.pixiv.net/api/login?lang=zh");
		post.setConfig(requestConfig);
		post.setHeader("cookie",map.get("cookies"));
		post.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		post.setHeader("accept-language","zh-CN,zh;q=0.9");
		post.setHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3510.2 Safari/537.36");		   
		post.setHeader("Content-Type","application/x-www-form-urlencoded");
		List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>(); 
		pairList.add(new BasicNameValuePair("password", password));
		pairList.add(new BasicNameValuePair("pixiv_id", username));
		pairList.add(new BasicNameValuePair("post_key", map.get("postKey")));
		try {
			post.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
	            List<Cookie> cookies = cookieStore.getCookies();
	            StringBuffer tmpcookies = new StringBuffer();
	            for (Cookie c : cookies) {
	            	 tmpcookies.append(c.getName()+"="+c.getValue() + ";");
	            }
	            return tmpcookies.toString();
			} else throw new PixivException("http请求失败",PixivException.ERROR_HTTPSTATUS);
		}catch(ClientProtocolException e) {
			throw new PixivException("ClientProtocolException",PixivException.ERROR_HTTPCLIENT,e);
		} 
	}
}
