package nemofrl.nemoapi.service.impl;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import nemofrl.nemoapi.exception.NemoAPIException;

public class PixivGetPictureUrl implements Callable<String> {
	private String id;
	private String cookies;
	private boolean openProxy;
	private static final Logger logger=LogManager.getLogger(PixivGetPictureUrl.class);
	private int proxyPort;
	private String proxyIp;
	
	public PixivGetPictureUrl(boolean openProxy,String proxyIp,int proxyPort,String id, String cookies) {
		this.id = id;
		this.cookies = cookies;
		this.openProxy=openProxy;
		this.proxyPort=proxyPort;
		this.proxyIp=proxyIp;
	}

	public String call(){
		Builder builder= RequestConfig.custom().setConnectionRequestTimeout(3000).setSocketTimeout(3000).setConnectTimeout(3000);
		if(openProxy) {
			HttpHost proxy = new HttpHost(proxyIp, proxyPort, "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		HttpClient client2 = HttpClientBuilder.create().build();
		HttpGet get2 = new HttpGet("https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + id);
		get2.setHeader("accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		get2.setHeader("accept-language", "zh-CN,zh;q=0.9");
		get2.setHeader("cookie", cookies);
		get2.setHeader("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3510.2 Safari/537.36");
		get2.setConfig(requestConfig);
		try {
			HttpResponse response = client2.execute(get2);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				String pictureContent = EntityUtils.toString(response.getEntity(), "UTF-8");
//				System.out.println(pictureContent);
				Pattern pattern = Pattern.compile("\"original\":\".{0,100}\\}");
				Matcher matcher = pattern.matcher(pictureContent);
				if (matcher.find()) {
					String temp = matcher.group();
					return temp.substring(12, temp.length() - 2);
				} else throw new NemoAPIException("匹配original(图片地址)失败",NemoAPIException.ERROR_MATCHER);
			}else throw new NemoAPIException("http请求失败",NemoAPIException.ERROR_NETSTATUS);
		} catch(Exception e) {
			if(e instanceof NemoAPIException) {
				NemoAPIException e1=(NemoAPIException) e;
				logger.error(e1.getMsg());
			} else logger.error("系统错误",e);
			return null;
		}
	}

}
