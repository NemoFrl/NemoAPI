package nemofrl.nemoapi.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import nemofrl.nemoapi.service.RssService;

@Component
public class DstRssSchedule {
	@Autowired
	private RssService rssService;
	
	@Scheduled(cron="${dstRssSchedule}")
	public void process() {
		
		rssService.getStreamRss();
		
	}
}
