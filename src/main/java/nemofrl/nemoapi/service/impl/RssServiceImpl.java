package nemofrl.nemoapi.service.impl;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import nemofrl.nemoapi.config.NemoAPIConfig;
import nemofrl.nemoapi.service.PostsService;
import nemofrl.nemoapi.service.RssService;

@Service("rssService")
public class RssServiceImpl implements RssService {

	@Autowired
	private NemoAPIConfig nemoAPIConfig;
	@Autowired
	private PostsService postsService;
	
	private static final Logger logger = LogManager.getLogger(RssServiceImpl.class);

	public void getStreamRss() {
		String rss = "https://steamcommunity.com/games/322330/rss/";

		try {
			Proxy proxy = new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress(nemoAPIConfig.getProxyIp(), nemoAPIConfig.getProxyPort()));
			URLConnection feedUrl;
			if(nemoAPIConfig.isOpenProxy())
				feedUrl= new URL(rss).openConnection(proxy);
			else feedUrl= new URL(rss).openConnection();
			feedUrl.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
			// 读取Rss源
			XmlReader reader = new XmlReader(feedUrl);
			SyndFeedInput input = new SyndFeedInput();
			// 得到SyndFeed对象，即得到Rss源里的所有信息
			SyndFeed feed = input.build(reader);
			// 得到Rss新闻中子项列表
			List entries = feed.getEntries();
			// 循环得到每个子项信息
			for (int i = 0; i < entries.size(); i++) {
				SyndEntry entry = (SyndEntry) entries.get(i);
				// 标题、连接地址、标题简介、时间是一个Rss源项最基本的组成部分
				SyndContent description = entry.getDescription();
				String content=description.getValue();
				String title=entry.getTitle();
//				String content=description.getValue().substring(description.getValue().indexOf("value=")+1);
//				String title=entry.getTitle().substring(entry.getTitle().indexOf("value=")+1);
				postsService.publish(content, title, entry.getLink(),entry.getPublishedDate(),13);
//				System.out.println("标题：" + entry.getTitle());
//				System.out.println("连接地址：" + entry.getLink());
//				System.out.println("标题简介：" + description.getValue());
//				System.out.println("发布时间：" + entry.getPublishedDate());
//				System.out.println("标题的作者：" + entry.getAuthor());
			}
		} catch (Exception e) {
			logger.error("读取饥荒rss源失败", e);
		}
	}
}
