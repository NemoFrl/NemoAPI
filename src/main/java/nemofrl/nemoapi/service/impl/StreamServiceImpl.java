package nemofrl.nemoapi.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import nemofrl.nemoapi.entity.StreamRespDTO;
import nemofrl.nemoapi.entity.StreamUserInfo;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.StreamService;
import nemofrl.nemoapi.util.HttpUtil;
import nemofrl.nemoapi.util.HttpUtil.HttpMethod;

@Service("streamServiceImpl")
public class StreamServiceImpl implements StreamService{
	private static final Logger logger = LogManager.getLogger(StreamServiceImpl.class);
	
	@Autowired
	private HttpUtil httpUtil;
	
	public List<StreamUserInfo> getStreamUserInfo(String streamIds) throws NemoAPIException {

		String url="https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?key=DBCF55763E2A6316B9D28C683912E111&steamids="+streamIds;
		String result=httpUtil.httpReq(true, url, HttpMethod.GET, null);
		
		Gson gson=new Gson();
		StreamRespDTO streamRespDTO=gson.fromJson(result, StreamRespDTO.class);

		logger.info("streamPlayerSummaries:"+result);
		return streamRespDTO.getResponse().getPlayers();
	}

	
}
