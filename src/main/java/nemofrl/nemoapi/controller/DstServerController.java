package nemofrl.nemoapi.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nemofrl.nemoapi.entity.DstServerVO;
import nemofrl.nemoapi.entity.RespDTO;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.DstServerService;

@RestController
public class DstServerController {
	
	private static final Logger logger = LogManager.getLogger(DstServerController.class);
	
	@Autowired
	private DstServerService dstServerService;
	
	@RequestMapping(value = "/getServerList", produces = { "application/json;charset=UTF-8" })
	public RespDTO getServerList(HttpServletResponse resp) {
		RespDTO respDTO = new RespDTO("命令执行成功", RespDTO.SUCCESS, null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		
		try {
			dstServerService.getServerList();
		} catch (NemoAPIException e) {
			logger.error(e.getMsg(),e.getE());
			respDTO.setCode(e.getCode());
			respDTO.setMsg(e.getMsg());
		}
		return respDTO;
	}
	
	@RequestMapping(value = "/getServerListFromCache", produces = { "application/json;charset=UTF-8" })
	public RespDTO getServerListFromCache(HttpServletResponse resp,Integer currentPage,Integer pageSize,String search) {
		RespDTO respDTO = new RespDTO("命令执行成功", RespDTO.SUCCESS, null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		if(currentPage==null||pageSize==null) {
			respDTO.setCode(RespDTO.ERROR_PARAMSLACK);
			respDTO.setMsg("参数缺乏！");
			return respDTO;
		}
		try {
			Map<String,Object> result=dstServerService.getServerListFromCache(currentPage, pageSize,search);
			respDTO.setData(result);
		} catch (NemoAPIException e) {
			logger.error(e.getMsg(),e.getE());
			respDTO.setCode(e.getCode());
			respDTO.setMsg(e.getMsg());
		}
		return respDTO;
	}
	@RequestMapping(value = "/getServerInfo", produces = { "application/json;charset=UTF-8" })
	public RespDTO getServerInfo(HttpServletResponse resp,String token,String rowId) {
		
		RespDTO respDTO = new RespDTO("命令执行成功", RespDTO.SUCCESS, null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		
		if(StringUtils.isAnyBlank(rowId)) {
			respDTO.setCode(RespDTO.ERROR_PARAMSLACK);
			respDTO.setMsg("参数缺乏！");
			return respDTO;
		}
		try {
			Map<String,Object> result=dstServerService.getServerInfo( token, rowId);
			respDTO.setData(result);
		} catch (NemoAPIException e) {
			logger.error(e.getMsg(),e.getE());
			respDTO.setCode(e.getCode());
			respDTO.setMsg(e.getMsg());
		}
		return respDTO;
	}
	@RequestMapping(value = "/searchDstPlayer", produces = { "application/json;charset=UTF-8" })
	public RespDTO searchDstPlayer(HttpServletResponse resp,String userName) {
		RespDTO respDTO = new RespDTO("命令执行成功", RespDTO.SUCCESS, null);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		if(StringUtils.isAnyBlank(userName)) {
			respDTO.setCode(RespDTO.ERROR_PARAMSLACK);
			respDTO.setMsg("参数缺乏！");
			return respDTO;
		}
		try {
			DstServerVO result=dstServerService.searchDstPlayer(userName);
			respDTO.setData(result);
		} catch (NemoAPIException e) {
			logger.error(e.getMsg(),e.getE());
			respDTO.setCode(e.getCode());
			respDTO.setMsg(e.getMsg());
		}
		return respDTO;
	}
	
}
