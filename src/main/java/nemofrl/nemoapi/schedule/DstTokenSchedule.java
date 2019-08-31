package nemofrl.nemoapi.schedule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import nemofrl.nemoapi.config.NemoAPIConfig;

@Component
public class DstTokenSchedule {
	
	private static final Logger logger = LogManager.getLogger(DstTokenSchedule.class);

	@Autowired
	private NemoAPIConfig nemoAPIConfig;
	
	@Scheduled(cron="${dstTokenSchedule}")
	public void process() {
		if(nemoAPIConfig.isDstTokenScheduleOpen())
			task();
	}
	
	public void task() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://login.kleientertainment.com/login/TokenPurpose");

		String token="{\"Token\": \""+nemoAPIConfig.getDstToken()+"\"}";
		HttpEntity entity=new StringEntity(token,StandardCharsets.UTF_8);
		httpPost.setEntity(entity);
		String result;
		try {
			HttpResponse resp = httpClient.execute(httpPost);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				result = EntityUtils.toString(resp.getEntity(), "UTF-8");
			else {
				logger.error("klei api TokenPurpose 响应码错误");
				return;
			}
		} catch (IOException e) {
			logger.error("请求klei api TokenPurpose 失败",e);
			return;
		}
		logger.info("延续dst token时间成功,resp："+result);
	
	}
}
