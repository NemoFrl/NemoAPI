package nemofrl.nemoapi.service.impl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.GoogleService;
import nemofrl.nemoapi.util.HttpUtil;
import nemofrl.nemoapi.util.HttpUtil.HttpMethod;

@Service("googleService")
public class GoogleServiceImpl implements GoogleService {

	@Autowired
	private HttpUtil httpUtil;

	private static final Logger logger = LogManager.getLogger(HaizeiServiceImpl.class);

	public List<Map<String, Object>> getGoogleUrl(String search, Integer page) throws NemoAPIException {

		String encodeSearch = UriUtils.encode(search, StandardCharsets.UTF_8.name());

		int start = (page - 1) * 10 + 1;

		String token = getToken();
		String url = "https://cse.google.com/cse/element/v1?cx=008994770321798397457:en4da-ly3wc&cse_tok=" + token
				+ "&callback=google.search.cse.api4001&q=" + encodeSearch + "&start=" + start;

		String json = httpUtil.httpReq(true, url, HttpMethod.GET, null);

		json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);

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

		String url = "https://cse.google.com/cse.js?h=xgdxg&hpg=1&cx=008994770321798397457:en4da-ly3wc";
		String result = httpUtil.httpReq(true, url, HttpMethod.GET, null);
		int jsonStartIndex = result.lastIndexOf(")(") + 2;
		int jsonEndIndex = result.lastIndexOf(")");
		result = result.substring(jsonStartIndex, jsonEndIndex);
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(result, Map.class);
		String token = (String) map.get("cse_token");
		return token;
	}

	public String translation(String to, String source) throws NemoAPIException {

		String url = "http://translate.google.cn/translate_a/single";

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
			HttpEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
			String json = httpUtil.httpReq(true, url, HttpMethod.POST, entity);
			Gson gson = new Gson();
			Map<String, Object> map = gson.fromJson(json, Map.class);
			ArrayList<LinkedTreeMap<String, Object>> sentences = (ArrayList<LinkedTreeMap<String, Object>>) map
					.get("sentences");
			String result = "";
			for (int i = 0; i < sentences.size(); i++) {
				result += sentences.get(i).get("trans");
			}

			return result;

		} catch (UnsupportedEncodingException e) {
			throw new NemoAPIException("谷歌翻译-UnsupportedEncodingException", NemoAPIException.ERROR_SYSTEM, e);
		}

	}
}
