package nemofrl.nemoapi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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
import nemofrl.nemoapi.service.EmojiService;

@Service("emojiService")
public class EmojiServiceImpl implements EmojiService{

	public List<LinkedTreeMap> getSogoEmoji(String search,Integer page) throws NemoAPIException {
		Base64.Encoder encoder = Base64.getEncoder();
		String cands=encoder.encodeToString(search.getBytes());
		cands=upDownCase(cands);
		String url="http://config.pinyin.sogou.com/picface/interface/query_zb.php?cands="+cands+"&tp=0&page="+page+"&h=D9BB9D40B88283286D79B44EB3849EBE&v=8.9.0.2180&r=0000_sogoupinyin_8.9c&pv=6.1.7601&sdk=1.1.0.1819";
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		
		HttpResponse resp;
		try {
			resp = httpClient.execute(httpGet);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(resp.getEntity(), "UTF-8");
				Gson gson=new Gson();
				Map<String,Object> map=gson.fromJson(json, Map.class);
				if(map!=null) {
					List<LinkedTreeMap> imageList = (List<LinkedTreeMap>) map.get("imglist");
					return imageList;
				}
				else return new ArrayList<>();
				
			} else throw new NemoAPIException("请求搜狗表情包api响应码错误", NemoAPIException.ERROR_NETSTATUS); 
		} catch (ClientProtocolException e) {
			throw new NemoAPIException("请求搜狗表情包api失败", NemoAPIException.ERROR_NETCONNECT, e);
		} catch (IOException e) {
			throw new NemoAPIException("请求搜狗表情包api失败", NemoAPIException.ERROR_NETCONNECT, e);
		}
		
	}
	
	private String upDownCase(String source) {
		char[] sourceChar=source.toCharArray();
		String output="";
		for(int i=0;i<sourceChar.length;i++) {
			char cr=sourceChar[i];
			if(cr>'A'&&cr<'Z')
				output+=String.valueOf(cr).toLowerCase();
			else if(cr>'a'&&cr<'z')
				output+=String.valueOf(cr).toUpperCase();
			else output+=String.valueOf(cr);
		}
		return output;
	}
}
