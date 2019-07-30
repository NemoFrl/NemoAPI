package nemofrl.pixiv.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nemofrl.pixiv.config.PixivConfig;
import nemofrl.pixiv.service.DownloadExecutor;
import nemofrl.pixiv.service.GetIdList;
import nemofrl.pixiv.service.GetPictureUrl;

@RestController
public class PixivController {

	private static final Logger logger = LogManager.getLogger(PixivController.class);
	
	@Autowired
	private GetIdList getIdList;
	@Autowired
	private PixivConfig pixivConfig;
	
	@RequestMapping(value = "/getOneShetu", produces = { "application/json;charset=UTF-8" })
	public String getOneShetu(String id) {

		String cookies = pixivConfig.getCookie();

		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String timeStr = simpleDateFormat.format(time);
		//1-5页随机
		int page = (int) (Math.random() * 4) + 1;

		logger.info("开始获取图片id");
		ArrayList<String> ids = getIdList.getIdList(page, timeStr);
		logger.info("获取图片id成功，num:" + ids.size());
		
		int index=(int) (Math.random() * ids.size());
		String randomId = ids.get(index);
		
		if(StringUtils.isBlank(id)||id.equals("null"))
			id=randomId;
		logger.info("开始获取图片url");
		String url = new GetPictureUrl(pixivConfig.isOpenProxy(),
				pixivConfig.getProxyIp(),pixivConfig.getProxyPort(),
				id, cookies).call();
		logger.info("获取图片url成功，url："+url);
		
		logger.info("开始下载图片");
		new DownloadExecutor(pixivConfig.isOpenProxy(),pixivConfig.getProxyIp(),pixivConfig.getProxyPort(),
				url, id, timeStr).run();
		logger.info("图片下载成功");
		
		String shetuUrl="/image/pixiv/" + timeStr + "/"+id+".jpg";
		logger.info("响应色图url："+shetuUrl);
		return shetuUrl;
	}

}
