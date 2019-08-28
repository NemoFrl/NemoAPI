package nemofrl.nemoapi.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nemofrl.nemoapi.entity.RespDTO;

@RestController
public class StreamController {
	private static final Logger logger = LogManager.getLogger(StreamController.class);

//	@Autowired
//	private StreamService streamService;

	@RequestMapping(value = "/getStreamUserInfo", produces = { "application/json;charset=UTF-8" })
	public RespDTO getStreamUserInfo(HttpServletResponse resp, String streamId) {
		RespDTO respDTO = new RespDTO("请求成功", RespDTO.SUCCESS, null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
//		try {
//			StreamUserInfo streamUserInfo = streamService.getStreamUserInfo(streamId);
//			respDTO.setData(streamUserInfo);
//		} catch (NemoAPIException e) {
//			logger.error(e.getMsg(), e.getE());
//			respDTO.setCode(e.getCode());
//			respDTO.setMsg(e.getMsg());
//		}
		return respDTO;
	}
}
