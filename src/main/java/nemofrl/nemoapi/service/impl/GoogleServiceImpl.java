package nemofrl.nemoapi.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import nemofrl.nemoapi.config.NemoAPIConfig;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.GoogleService;

@Service("googleService")
public class GoogleServiceImpl implements GoogleService {

	@Autowired
	private NemoAPIConfig nemoAPIConfig;

	private static final Logger logger = LogManager.getLogger(HaizeiServiceImpl.class);

	public List<Map<String, Object>> getGoogleUrl(String search, Integer page) throws NemoAPIException {
		Builder builder = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000);
		if (nemoAPIConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(nemoAPIConfig.getProxyIp(), nemoAPIConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		String encodeSearch = UriUtils.encode(search, StandardCharsets.UTF_8.name());

		HttpClient httpClient = HttpClientBuilder.create().build();
		
		int start= (page - 1) * 10 +1;
		
		String token=getToken();
		
		HttpGet httpGet = new HttpGet(
				"https://cse.google.com/cse/element/v1?cx=008994770321798397457:en4da-ly3wc&cse_tok="+token+"&callback=google.search.cse.api4001&q="
						+ encodeSearch + "&start=" + start);
		httpGet.setConfig(requestConfig);
		String json = null;
		try {
			HttpResponse resp = httpClient.execute(httpGet);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				json = EntityUtils.toString(resp.getEntity(), "UTF-8");
			else throw new NemoAPIException("谷歌api响应码错误", NemoAPIException.ERROR_NETSTATUS);
		} catch (IOException e) {
			throw new NemoAPIException("请求谷歌api失败", NemoAPIException.ERROR_NETCONNECT, e);
		}

		json=json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
		
		Gson gson = new Gson();

		Map<String, Object> map = gson.fromJson(json, Map.class);
		List<LinkedTreeMap> items = (List<LinkedTreeMap>) map.get("results");
		List<Map<String, Object>> resultItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < items.size(); i++) {
			Map<String, Object> resultItem = new HashMap<String, Object>();
			LinkedTreeMap item = items.get(i);
			String title = (String) item.get("titleNoFormatting");
			String link = (String) item.get("unescapedUrl");
			resultItem.put("title", title);
			resultItem.put("link", link);
			resultItems.add(resultItem);
		}

		return resultItems;
	}
	
	private String getToken() throws NemoAPIException {
		Builder builder = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000);
		if (nemoAPIConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(nemoAPIConfig.getProxyIp(), nemoAPIConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("https://cse.google.com/cse.js?h=xgdxg&hpg=1&cx=008994770321798397457:en4da-ly3wc");
		httpGet.setConfig(requestConfig);
		String result;
		try {
			HttpResponse resp = httpClient.execute(httpGet);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				result = EntityUtils.toString(resp.getEntity(), "UTF-8");
			else throw new NemoAPIException("谷歌api响应码错误", NemoAPIException.ERROR_NETSTATUS);
		} catch (IOException e) {
			throw new NemoAPIException("请求谷歌token api失败", NemoAPIException.ERROR_NETCONNECT, e);
		}
		int jsonStartIndex=result.lastIndexOf(")(")+2;
		int jsonEndIndex=result.lastIndexOf(")");
		result = result.substring(jsonStartIndex,jsonEndIndex);
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(result, Map.class);
		String token = (String) map.get("cse_token");
		return token;
	}
	
	public String translation(String to, String source) throws NemoAPIException {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("http://translate.google.cn/translate_a/single");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ie", "UTF-8"));
		nvps.add(new BasicNameValuePair("client", "gtx"));
		nvps.add(new BasicNameValuePair("sl", "auto"));
		nvps.add(new BasicNameValuePair("tl", to));
		nvps.add(new BasicNameValuePair("dt", "t"));
		nvps.add(new BasicNameValuePair("dj", "1"));
		nvps.add(new BasicNameValuePair("q", source));
		// 设置参数到请求对象中
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(response.getEntity(), "UTF-8");
				Gson gson = new Gson();
				Map<String, Object> map = gson.fromJson(json, Map.class);
				ArrayList<LinkedTreeMap<String, Object>> sentences = (ArrayList<LinkedTreeMap<String, Object>>) map
						.get("sentences");
				String result = "";
				for (int i = 0; i < sentences.size(); i++) {
					result += sentences.get(i).get("trans");
				}

				return result;
			} else
				throw new NemoAPIException("谷歌翻译响应码错误", NemoAPIException.ERROR_NETSTATUS);

		} catch (UnsupportedEncodingException e) {
			throw new NemoAPIException("谷歌翻译-UnsupportedEncodingException", NemoAPIException.ERROR_SYSTEM, e);
		} catch (ClientProtocolException e) {
			throw new NemoAPIException("谷歌翻译-ClientProtocolException", NemoAPIException.ERROR_SYSTEM, e);
		} catch (IOException e) {
			throw new NemoAPIException("谷歌翻译-IOException", NemoAPIException.ERROR_NETCONNECT, e);
		}

	}
}
