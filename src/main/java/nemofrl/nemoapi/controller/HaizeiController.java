package nemofrl.nemoapi.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nemofrl.nemoapi.entity.RespDTO;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.HaizeiService;

@RestController
public class HaizeiController {
	
	@Autowired
	private HaizeiService haizeiService;
	
	private static final Logger logger = LogManager.getLogger(HaizeiController.class);
	
	@RequestMapping(value = "/getNewHaizei", produces = { "application/json;charset=UTF-8" })
	public RespDTO getNewHaizei(HttpServletResponse resp,String h) {
		RespDTO respDTO=new RespDTO("请求成功",RespDTO.SUCCESS,null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		List<Map<String, String>> result;
		try {
			result = haizeiService.getHaizeiList(h);
			respDTO.setData(result);
		} catch (NemoAPIException e) {
			logger.error(e.getMsg(),e.getE());
			respDTO.setCode(e.getCode());
			respDTO.setMsg(e.getMsg());
		}
		
		return respDTO;
	}

}
