package nemofrl.nemoapi.util;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nemofrl.nemoapi.config.NemoAPIConfig;
import nemofrl.nemoapi.exception.NemoAPIException;
@Component
public class HttpUtil {

	@Autowired
	private NemoAPIConfig nemoAPIConfig;
	
	private static final Logger logger = LogManager.getLogger(HttpUtil.class);
	
	public enum HttpMethod {  
		GET, POST
	}
	public static HttpClient getHttpClient() throws NemoAPIException {

		SSLConnectionSocketFactory sslsf;
		PoolingHttpClientConnectionManager cm;
		try {
			SSLContext sslContext = SSLContext.getDefault();
			sslsf  = new SSLConnectionSocketFactory(sslContext, new String[] {"TLSv1.2"}, null,
			            NoopHostnameVerifier.INSTANCE);
		    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();

		    cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		} catch (NoSuchAlgorithmException e1) {
			throw new NemoAPIException("NoSuchAlgorithmException", NemoAPIException.ERROR_NETCONNECT, e1);
		} 

		HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).setConnectionManager(cm).build();
		return httpClient;
	}
	
	public  HttpPost getHttpPost(boolean openProxy,String url) {
		HttpPost httpPost = new HttpPost(url);
		Builder builder = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000);
		if (openProxy&&nemoAPIConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(nemoAPIConfig.getProxyIp(), nemoAPIConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		httpPost.setConfig(requestConfig);
		return httpPost;
	}
	
	public  HttpGet getHttpGet(boolean openProxy,String url) {
		HttpGet httpGet = new HttpGet(url);
		Builder builder = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000);
		if (openProxy&&nemoAPIConfig.isOpenProxy()) {
			HttpHost proxy = new HttpHost(nemoAPIConfig.getProxyIp(), nemoAPIConfig.getProxyPort(), "http");
			builder.setProxy(proxy);
		}
		RequestConfig requestConfig = builder.build();
		httpGet.setConfig(requestConfig);
		return httpGet;
	}
	
	public String httpReq(HttpClient httpClient,HttpUriRequest request) throws NemoAPIException {
		URI uri=request.getURI();
		logger.debug("start httpReq,url:"+uri.toString());
		Date before=new Date();
		String result;
		try {
			HttpResponse resp = httpClient.execute(request);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				result = EntityUtils.toString(resp.getEntity(), "UTF-8");
			else {
				String errorMsg="响应码错误,url:"+uri.toString()+",code:"+resp.getStatusLine().getStatusCode();
				throw new NemoAPIException(errorMsg, NemoAPIException.ERROR_NETSTATUS);
			}
		} catch (IOException e) {
			String errorMsg="请求错误,url:"+request.getURI().getPath();
			throw new NemoAPIException(errorMsg, NemoAPIException.ERROR_NETCONNECT, e);
		}
		Date after=new Date();
		long useTime=after.getTime()-before.getTime();
		logger.debug("httpReq success,url:"+uri.toString()+",useTime:"+useTime);
		return result;
	}
	
	public String httpReq(boolean openProxy,String url,HttpMethod httpMethod,HttpEntity entity) throws NemoAPIException {
		HttpClient httpClient=getHttpClient();
		HttpUriRequest request;
		if(httpMethod==HttpMethod.GET) {
			request=this.getHttpGet(openProxy, url);
		} else if(httpMethod==HttpMethod.POST) {
		    request=this.getHttpPost(openProxy, url);
		    ((HttpPost)request).setEntity(entity);
		}
		else throw new NemoAPIException("unsupport httpMethod", NemoAPIException.ERROR_SYSTEM);
		String result=httpReq(httpClient, request);
		return result;
	}
	
	
}
