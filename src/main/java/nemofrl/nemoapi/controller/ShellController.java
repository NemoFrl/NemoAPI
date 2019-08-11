package nemofrl.nemoapi.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nemofrl.nemoapi.entity.RespDTO;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.ShellService;

@RestController
public class ShellController {

	@Autowired
	private ShellService shellService;
	
	private static final Logger logger = LogManager.getLogger(ShellController.class);
	
	@RequestMapping(value = "/execShellCommand", produces = { "application/json;charset=UTF-8" })
	public RespDTO execShellCommand(HttpServletResponse resp,String command,String ip,String userName,String passwd,Integer port) {
		RespDTO respDTO=new RespDTO("请求成功",RespDTO.SUCCESS,null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		if(port==null)
			port=22;
		if(StringUtils.isAnyBlank(command,ip,userName,passwd)) {
			respDTO.setCode(RespDTO.ERROR_PARAMSLACK);
			respDTO.setMsg("请把参数填写完整哦！");
			return respDTO;
		}
		String result=null;
		try {
			result = shellService.shServer(command, ip, userName, passwd, port);
			respDTO.setData(result);
		} catch (NemoAPIException e) {
			logger.error(e.getMsg(),e);
			respDTO.setCode(e.getCode());
			respDTO.setMsg(e.getMsg());
		}
		return respDTO;
	}
}
