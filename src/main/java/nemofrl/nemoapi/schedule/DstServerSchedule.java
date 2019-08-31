package nemofrl.nemoapi.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import nemofrl.nemoapi.config.NemoAPIConfig;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.DstServerService;

@Component
public class DstServerSchedule {
	@Autowired
	private DstServerService dstServerService;
	@Autowired
	private NemoAPIConfig nemoAPIConfig;
	
	private static final Logger logger = LogManager.getLogger(DstServerSchedule.class);

	@Scheduled(cron="${dstSeverSchedule}")
	public void process() {
		if(nemoAPIConfig.isDstServerScheduleOpen()) {
			logger.info("定时拉取服务器信息");
			try {
				dstServerService.getServerList();
			} catch (NemoAPIException e) {
				logger.error("拉取服务器信息失败",e.getE());
			}
		}
	}
}
