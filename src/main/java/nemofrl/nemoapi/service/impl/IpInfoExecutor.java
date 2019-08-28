package nemofrl.nemoapi.service.impl;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import nemofrl.nemoapi.entity.IpVO;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.util.HttpUtil;
import nemofrl.nemoapi.util.HttpUtil.HttpMethod;

public class IpInfoExecutor implements Callable<IpVO>{

	private String ip;
	
	public IpInfoExecutor(String ip) {
		this.ip=ip;
	}
	
	@Override
	public IpVO call() throws Exception {
		HttpUtil httpUtil=new HttpUtil();
		String url="http://ip-api.com/json/"+ip+"?lang=zh-CN";
		String result=httpUtil.httpReq(false, url, HttpMethod.GET, null);
		
		Gson gson=new Gson();
		IpVO ipVO=gson.fromJson(result, IpVO.class);
		return ipVO;
	}

}
