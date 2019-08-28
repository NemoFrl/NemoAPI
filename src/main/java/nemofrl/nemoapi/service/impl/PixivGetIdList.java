package nemofrl.nemoapi.service.impl;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.util.HttpUtil;
import nemofrl.nemoapi.util.HttpUtil.HttpMethod;

@Component
public class PixivGetIdList {

	@Autowired
	private HttpUtil httpUtil;

	private static final Logger logger = LogManager.getLogger(PixivGetIdList.class);

	public ArrayList<String> getIdList(int page, String timeStr) throws NemoAPIException {
		String pixivUrl = "https://www.pixiv.net/ranking.php?mode=monthly&content=illust&format=json&p=" + page
				+ "&month=" + timeStr;
		String result = httpUtil.httpReq(true, pixivUrl, HttpMethod.GET, null);
		ArrayList<String> ids = new ArrayList<String>();

		Pattern pattern1 = Pattern.compile("illust_id\":[0-9]{0,20}");
		Matcher matcher1 = pattern1.matcher(result);
		while (matcher1.find()) {
			String find = matcher1.group();
			ids.add(find.substring(11, find.length()));
		}
		return ids;

	}
}
