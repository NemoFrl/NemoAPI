package nemofrl.nemoapi.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import nemofrl.nemoapi.config.NemoAPIConfig;
import nemofrl.nemoapi.service.RssService;

@Component
public class DstRssSchedule {
	@Autowired
	private RssService rssService;
	@Autowired
	private NemoAPIConfig nemoAPIConfig;
	
	@Scheduled(cron="${dstRssSchedule}")
	public void process() {
		if(nemoAPIConfig.isDstRssScheduleOpen())
			rssService.getStreamRss();
	}
}
