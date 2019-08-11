package nemofrl.nemoapi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.HaizeiService;

@Service("haizeiService")
public class HaizeiServiceImpl implements HaizeiService{
	
	public List<Map<String,String>> getHaizeiList(String h) throws NemoAPIException {
		HttpClient client = HttpClientBuilder.create().build();
		try {
		String version=getHaizeiVersion();
		if(StringUtils.isBlank(version))
				return null;
		HttpGet httpGet=new HttpGet("https://prod-api.ishuhui.com/ver/"+version+"/anime/detail?id=1&type=comics&.json");
		HttpResponse response = client.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String json = EntityUtils.toString(response.getEntity(), "UTF-8");
			Gson gson = new Gson();
			Map<String,Object> map=gson.fromJson(json, Map.class);
			map=(Map<String, Object>) map.get("data");
			map=(Map<String, Object>) map.get("comicsIndexes");
			map=(Map<String, Object>) map.get("1");
			int maxNum;
			if(StringUtils.isBlank(h))
				maxNum=((Double)map.get("maxNum")).intValue();
			else maxNum=Integer.valueOf(h);
			int showLen=((Double) map.get("showLen")).intValue();
			int startNum=maxNum/showLen==(maxNum*1.0/showLen)?maxNum-showLen:maxNum/showLen*showLen;
			int endNum=startNum+showLen;
			String numKey=(startNum+1)+"-"+endNum;
			map=(Map<String, Object>) map.get("nums");
			map=(Map<String, Object>) map.get(numKey+"");
			int num=maxNum;

			List<Map<String,String>> resultList=new ArrayList<>();
			for(int i=0;i<map.size();i++) {
				Map<String,String> resultMap=new HashMap<>();
				ArrayList<LinkedTreeMap<String, Object>> allpoint=(ArrayList<LinkedTreeMap<String, Object>>) map.get(num+"");
				Map<String, Object> point=allpoint.get(0);
				int id=((Double) point.get("id")).intValue();
				String title=(String) point.get("title");
				resultMap.put("title", "[海贼王] "+num+"话 "+title);
				resultMap.put("link", "http://www.hanhuazu.cc/comics/detail/"+id);
				resultList.add(resultMap);
				num--;
			}
			return resultList;
		} else throw new NemoAPIException("海贼api响应码错误", NemoAPIException.ERROR_NETSTATUS);
		} catch (ClientProtocolException e) {
			throw new NemoAPIException("海贼api-ClientProtocolException", NemoAPIException.ERROR_SYSTEM);
		} catch (IOException e) {
			throw new NemoAPIException("海贼api-IOException", NemoAPIException.ERROR_NETCONNECT);
		}
	}
	private String getHaizeiVersion() throws ClientProtocolException, IOException, NemoAPIException  {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet verGet=new HttpGet("https://prod-u.ishuhui.com/ver");
		HttpResponse response = client.execute(verGet);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String json = EntityUtils.toString(response.getEntity(), "UTF-8");
			Gson gson = new Gson();
			Map<String,Object> map=gson.fromJson(json, Map.class);
			map=(Map<String, Object>) map.get("data");
			return (String) map.get("comics");
		} else throw new NemoAPIException("海贼api响应码错误", NemoAPIException.ERROR_NETSTATUS);
	}
}
