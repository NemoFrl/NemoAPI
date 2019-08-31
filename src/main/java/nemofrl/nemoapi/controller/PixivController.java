package nemofrl.nemoapi.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nemofrl.nemoapi.config.NemoAPIConfig;
import nemofrl.nemoapi.entity.RespDTO;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.impl.PixivDownloadExecutor;
import nemofrl.nemoapi.service.impl.PixivGetIdList;
import nemofrl.nemoapi.service.impl.PixivGetPictureUrl;

@RestController
public class PixivController {

	private static final Logger logger = LogManager.getLogger(PixivController.class);
	
	@Autowired
	private PixivGetIdList getIdList;
	@Autowired
	private NemoAPIConfig nemoAPIConfig;

	@RequestMapping(value = "/getOneShetu", produces = { "application/json;charset=UTF-8" })
	public RespDTO getOneShetu(HttpServletResponse resp,String id) {
		RespDTO respDTO=new RespDTO("请求成功",RespDTO.SUCCESS,null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		
		String cookies = nemoAPIConfig.getCookie();

		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String timeStr = simpleDateFormat.format(time);
		//1-5页随机
		int page = (int) (Math.random() * 2) + 1;

		logger.info("开始获取图片id");
		ArrayList<String> ids;
		try {
			ids = getIdList.getIdList(page, timeStr);
		} catch (NemoAPIException e) {
			logger.error(e.getMsg(),e.getE());
			respDTO.setCode(e.getCode());
			respDTO.setMsg(e.getMsg());
			return respDTO;
		}
		logger.info("获取图片id成功，num:" + ids.size());
		
		int index=(int) (Math.random() * ids.size());
		String randomId = ids.get(index);
		
		if(StringUtils.isBlank(id)||id.equals("null"))
			id=randomId;
		logger.info("开始获取图片url");
		String url = new PixivGetPictureUrl(nemoAPIConfig.isOpenProxy(),
				nemoAPIConfig.getProxyIp(),nemoAPIConfig.getProxyPort(),
				id, cookies).call();
		
		if(url==null) {
			respDTO.setCode(NemoAPIException.ERROR_SYSTEM);
			respDTO.setMsg("获取色图失败了，重新gkd试试看？");
			return respDTO;
		}
		logger.info("获取图片url成功，url："+url);
		
		logger.info("开始下载图片");
		new PixivDownloadExecutor(nemoAPIConfig.isOpenProxy(),nemoAPIConfig.getProxyIp(),nemoAPIConfig.getProxyPort(),
				url, id, timeStr).run();
		logger.info("图片下载成功");
		
		File file=new File("pixiv/" + timeStr + "/" + id + ".jpg");
		if(!file.exists()) {
			respDTO.setCode(NemoAPIException.ERROR_SYSTEM);
			respDTO.setMsg("获取色图失败了，重新gkd试试看？");
			return respDTO;
		}
		
		String shetuUrl="/image/pixiv/" + timeStr + "/"+id+".jpg";
		logger.info("响应色图url："+shetuUrl);
		respDTO.setData(shetuUrl);
		
		return respDTO;
	}
	
	
}
