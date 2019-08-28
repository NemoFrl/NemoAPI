package nemofrl.nemoapi.service.impl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.util.HttpUtil;


public class PixivDownloadExecutor implements Runnable {
	private String pictureUrl;
	private String id;
	private String date;
	private boolean openProxy;
	private static final Logger logger=LogManager.getLogger(PixivDownloadExecutor.class);
	private int proxyPort;
	private String proxyIp;
	
	public PixivDownloadExecutor(boolean openProxy,String proxyIp,int proxyPort,String pictureUrl, String id, String date) {
		this.pictureUrl = pictureUrl;
		this.id = id;
		this.date = date;
		this.openProxy=openProxy;
		this.proxyPort=proxyPort;
	    this.proxyIp=proxyIp;
	}

	public void run() {
		File photo = new File("pixiv/" + date + "/"+id+".jpg");
		if(photo.exists())
		return;
		Builder builder= RequestConfig.custom().setConnectionRequestTimeout(3000).setSocketTimeout(10000).setConnectTimeout(3000);
		if(openProxy) {
			HttpHost proxy = new HttpHost(proxyIp, proxyPort, "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		HttpClient client;
		try {
			client = HttpUtil.getHttpClient();
		} catch (NemoAPIException e1) {
			logger.error("NemoAPIException",e1.getE());
			return;
		}
		HttpGet get = new HttpGet(pictureUrl.replace("\\", ""));
		get.setHeader("Host", " i.pximg.net");
		get.setHeader("Connection", "keep-alive");
		get.setHeader("Cache-Contro", "max-age=0");
		get.setHeader("Upgrade-Insecure-Requests", "1");
		get.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3510.2 Safari/537.36");
		get.addHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		get.setHeader("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + id);
		get.setHeader("Accept-Encoding", "gzip, deflate, br");
		get.setHeader("Accept-Language", " zh-CN,zh;q=0.9");
		get.setConfig(requestConfig);
		try {
			HttpResponse response = client.execute(get);
			InputStream input = response.getEntity().getContent();
			File file = new File("pixiv/" + date + "/");
			if (!file.exists())
				file.mkdirs();
			
			FileOutputStream output = new FileOutputStream("pixiv/" + date + "/" + id + ".jpg");
			byte[] bs = new byte[1024];
			int j;
			while ((j = input.read(bs)) != -1) {
				output.write(bs, 0, j);
			}
			output.close();

		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException",e);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException",e);
		} catch (IOException e) {
			logger.error("IOException",e);
		} 
	}

}
