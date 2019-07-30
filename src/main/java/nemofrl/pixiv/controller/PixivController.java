package nemofrl.pixiv.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nemofrl.pixiv.config.PixivConfig;
import nemofrl.pixiv.exception.PixivException;
import nemofrl.pixiv.service.Authentication;
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
	@Autowired
	private Authentication au;
	
	@RequestMapping(value = "/getOneShetu", produces = { "application/json;charset=UTF-8" })
	public String getOneShetu() {

		String cookies = null;
		try {
			logger.info("开始获取postKey和cookie");
			Map<String, String> keyCookie = au.getPostKeyAndCookie();
			logger.info("cookies:" + keyCookie.get("cookies") + "postkey:" + keyCookie.get("postKey")
					+ "，注入账号密码拟登录获取cookie");
			cookies = au.getCookie(keyCookie, "369143075@qq.com", "ins53545464886");
			logger.info("获取cookies成功:" + cookies);
		} catch (Exception e) {
			if (e instanceof PixivException) {
				PixivException e1 = (PixivException) e;
				logger.error(e1.getMessage(), e1);
			} else
				logger.error("系统错误!", e);
		}
		if (cookies == null) {
			logger.info("cookie为空，程序无法继续");
			return "error";
		}
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String timeStr = simpleDateFormat.format(time);
		//1-20页随机
		int page = (int) (Math.random() * 4) + 1;

		logger.info("开始获取图片id");
		ArrayList<String> ids = getIdList.getIdList(page, timeStr);
		logger.info("获取图片id成功，num:" + ids.size());
		
		int index=(int) (Math.random() * ids.size());
		String randomId = ids.get(index);

		String randomUrl = new GetPictureUrl(pixivConfig.isOpenProxy(),
				pixivConfig.getProxyIp(),pixivConfig.getProxyPort(),
				randomId, cookies).call();

		new DownloadExecutor(pixivConfig.isOpenProxy(),pixivConfig.getProxyIp(),pixivConfig.getProxyPort(),
				randomUrl, randomId, timeStr).run();
		
		return "localhost:8080/image/pixiv/" + timeStr + "/"+randomId+".jpg";
	}

}
