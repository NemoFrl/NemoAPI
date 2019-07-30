package nemofrl.pixiv.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nemofrl.pixiv.config.PixivConfig;
import nemofrl.pixiv.exception.PixivException;

@Component
public class GetIdList {

	@Autowired
	private PixivConfig pixivConfig;
	
	private static final Logger logger=LogManager.getLogger(GetIdList.class);
	
	public ArrayList<String> getIdList(int page, String timeStr) {
		String pixivUrl = "https://www.pixiv.net/ranking.php?mode=monthly&content=illust&format=json&p=" + page
				+ "&month=" + timeStr;
		Builder builder = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000);
		if(pixivConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(pixivConfig.getProxyIp(), pixivConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(pixivUrl);
		get.setConfig(requestConfig);
		ArrayList<String> ids = new ArrayList<String>();
		try {
			HttpResponse response = client.execute(get);
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				String encodeJson = EntityUtils.toString(entity, "UTF-8");
				Pattern pattern1 = Pattern.compile("illust_id\":[0-9]{0,20}");
				Matcher matcher1 = pattern1.matcher(encodeJson);
				while (matcher1.find()) {
					String find = matcher1.group();
					ids.add(find.substring(11, find.length()));
				}
				return ids;
			} else
				throw new PixivException("http请求失败", PixivException.ERROR_HTTPSTATUS);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} catch (PixivException e) {
			logger.error(e.getMessage(), e);
		}
		return ids;
	}
}
