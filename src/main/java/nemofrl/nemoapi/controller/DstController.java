package nemofrl.nemoapi.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import nemofrl.nemoapi.entity.DstServerDTO;
import nemofrl.nemoapi.entity.RespDTO;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.ShellService;

@RestController
public class DstController {
	@Autowired
	private ShellService shellService;

	private static final Logger logger = LogManager.getLogger(DstController.class);

	@RequestMapping(value = "/dst", produces = { "application/json;charset=UTF-8" })
	public RespDTO dst(HttpServletResponse resp, String dstServerDTOJsonStr) {
		RespDTO respDTO = new RespDTO("命令执行成功", RespDTO.SUCCESS, null);
		resp.setHeader("Access-Control-Allow-Origin", "*");

		Gson gson = new Gson();
		DstServerDTO dstServerDTO;
		try {
			dstServerDTO = gson.fromJson(dstServerDTOJsonStr, DstServerDTO.class);
		} catch (Exception e) {
			logger.error("请求参数错误");
			respDTO.setCode(RespDTO.ERROR_PARAMSERROR);
			respDTO.setMsg("请求参数错误");
			return respDTO;
		}
		if (StringUtils.isAnyBlank(dstServerDTO.getCluster(), dstServerDTO.getCommand(), dstServerDTO.getIp(),
				dstServerDTO.getPasswd(), dstServerDTO.getUserName()) || dstServerDTO.getPort() == null) {
			logger.error("请求参数缺乏");
			respDTO.setCode(RespDTO.ERROR_PARAMSLACK);
			respDTO.setMsg("请求参数缺乏");
			return respDTO;
		}

		String result;
		try {
			Method[] methods=this.getClass().getDeclaredMethods();
			for(int i=0;i<methods.length;i++) {
				if(methods[i].getName().equals(dstServerDTO.getCommand())) {
					result=(String) methods[i].invoke(this, dstServerDTO);
					respDTO.setData(result);
					break;
				}
			}
		} catch (IllegalAccessException e) {
			logger.error("IllegalAccessException", e);
			respDTO.setCode(NemoAPIException.ERROR_SYSTEM);
			respDTO.setMsg("系统错误");
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException", e);
			respDTO.setCode(NemoAPIException.ERROR_SYSTEM);
			respDTO.setMsg("系统错误");
		} catch (InvocationTargetException e) {
			NemoAPIException e1=(NemoAPIException)e.getCause();
			logger.error(e1.getMsg(), e1);
			respDTO.setCode(e1.getCode());
			respDTO.setMsg(e1.getMsg());
		} 
		return respDTO;
	}

	private String execCmd(String command, DstServerDTO dstServerDTO) throws NemoAPIException {
		return shellService.shServer(command, dstServerDTO.getIp(), dstServerDTO.getUserName(),
				dstServerDTO.getPasswd(), dstServerDTO.getPort());
	}

	private String execDstCommand(DstServerDTO dstServerDTO) throws NemoAPIException {
		String screenName = "Master Server " + dstServerDTO.getCluster();
		String command = "screen -S \"" + screenName + "\" -p 0 -X stuff \"" + dstServerDTO.getParam()
				+ "$(printf \\\\r)\"\r\n";
		String result = execCmd(command, dstServerDTO);
		return result;
	}
	
	private String initSteamCmd(DstServerDTO dstServerDTO) throws NemoAPIException {
		String result = execCmd("apt-get update", dstServerDTO);
		result += execCmd("apt-get -y install lib32gcc1", dstServerDTO) + "\n";
		result += execCmd("apt-get -y install lib32stdc++6", dstServerDTO)+ "\n";
		result += execCmd("apt-get -y install libcurl4-gnutls-dev:i386", dstServerDTO)+ "\n";
		result += execCmd("apt-get -y install htop", dstServerDTO)+ "\n";
		result += execCmd("apt-get -y install screen", dstServerDTO)+ "\n";
		result += execCmd("mkdir steamcmd", dstServerDTO)+ "\n";
		result += execCmd("cd steamcmd;wget https://steamcdn-a.akamaihd.net/client/installer/steamcmd_linux.tar.gz",
				dstServerDTO)+ "\n";
		result += execCmd("cd steamcmd;tar -xvzf steamcmd_linux.tar.gz", dstServerDTO)+ "\n";
		result += execCmd("cd steamcmd;rm -f steamcmd_linux.tar.gz", dstServerDTO)+ "\n";
		result += execCmd("cd steamcmd/linux32;./steamcmd", dstServerDTO)+ "\n";
		result += execCmd("mkdir -p .klei/DoNotStarveTogether", dstServerDTO)+ "\n";
		return result;
	}

	private String stopMaster(DstServerDTO dstServerDTO) throws NemoAPIException {

		String screenName = "Master Server " + dstServerDTO.getCluster();
		String stop = "pkill -f '" + screenName + "'";
		String result = execCmd(stop, dstServerDTO);
		return result;
	}

	private String restartMaster(DstServerDTO dstServerDTO) throws NemoAPIException {
		String result = stopMaster(dstServerDTO)+ "\n";
		String screenName = "Master Server " + dstServerDTO.getCluster();
		String start = "cd Steam/steamapps/common/Don\\'t\\ Starve\\ Together\\ Dedicated\\ Server/bin;screen -dmS \""
				+ screenName + "\" ./dontstarve_dedicated_server_nullrenderer -console -cluster "
				+ dstServerDTO.getCluster() + " -monitor_parent_process $ -shard Master";
		result += execCmd(start, dstServerDTO);
		return result;
	}

	private String stopCaves(DstServerDTO dstServerDTO) throws NemoAPIException {
		String screenName = "Caves Server " + dstServerDTO.getCluster();
		String stop = "pkill -f '" + screenName + "'";
		String result = execCmd(stop, dstServerDTO);
		return result;
	}

	private String restartCaves(DstServerDTO dstServerDTO) throws NemoAPIException {
		String result = stopCaves(dstServerDTO)+ "\n";
		String screenName = "Caves Server " + dstServerDTO.getCluster();
		String start = "cd Steam/steamapps/common/Don\\'t\\ Starve\\ Together\\ Dedicated\\ Server/bin;screen -dmS \""
				+ screenName + "\" ./dontstarve_dedicated_server_nullrenderer -console -cluster "
				+ dstServerDTO.getCluster() + " -monitor_parent_process $ -shard Caves";
		result += execCmd(start, dstServerDTO);
		return result;
	}

	private String dstUpdate(DstServerDTO dstServerDTO) throws NemoAPIException {
		String command = "cd steamcmd;./steamcmd.sh +login anonymous +app_update 343050 validate +quit";
		String result = execCmd(command, dstServerDTO);
		return result;
	}

//	private String dstUpdateBeta(DstServerDTO dstServerDTO) throws NemoAPIException {
//		String command = "cd steamcmd;./steamcmd.sh +login anonymous +app_update 343050 -beta returnofthembeta +quit";
//		String result = execCmd(command, dstServerDTO);
//		return result;
//	}

	private String dstMod(DstServerDTO dstServerDTO) throws NemoAPIException {
		
		String contentBody = dstServerDTO.getParam();

		String getMods = "cd Steam/steamapps/common/Don\\'t\\ Starve\\ Together\\ Dedicated\\ Server/mods/;cat dedicated_server_mods_setup.lua";
		String result = null;

		result = execCmd(getMods, dstServerDTO);

		if (result == null) {
			result += "读取mod配置文件失败，请检查是否已安装饥荒服务器";
			return result;
		}
		if (!result.contains(contentBody)) {
			String command = "cd Steam/steamapps/common/Don\\'t\\ Starve\\ Together\\ Dedicated\\ Server/mods/;echo \"ServerModSetup(\\\""
					+ contentBody + "\\\")\" >> dedicated_server_mods_setup.lua";
			result = execCmd(command, dstServerDTO);
		} else
			result = "mod添加失败，该mod已存在";
		return result;
	}

	private String dstKick(DstServerDTO dstServerDTO) throws NemoAPIException {
		String contentBody = dstServerDTO.getParam();

		String screenName = "Master Server " + dstServerDTO.getCluster();
		String command = "screen -S \"" + screenName + "\" -p 0 -X stuff \"TheNet:Kick(\\\"" + contentBody
				+ "\\\")$(printf \\\\r)\"\r\n";
		String result = execCmd(command, dstServerDTO);
		return result;
	}

	private String dstBan(DstServerDTO dstServerDTO) throws NemoAPIException {
		String contentBody = dstServerDTO.getParam();

		String screenName = "Master Server " + dstServerDTO.getCluster();
		String command = "screen -S \"" + screenName + "\" -p 0 -X stuff \"TheNet:BanForTime(\\\"" + contentBody
				+ "\\\",120)$(printf \\\\r)\"\r\n";
		String result = execCmd(command, dstServerDTO);
		return result;
	}

	private String dstMsg(DstServerDTO dstServerDTO) throws NemoAPIException {
		String contentBody = dstServerDTO.getParam();

		String screenName = "Master Server " + dstServerDTO.getCluster();
		String command = "screen -S \"" + screenName + "\" -p 0 -X stuff \"TheNet:SystemMessage(\\\"" + contentBody
				+ "\\\")$(printf \\\\r)\"\r\n";
		String result = execCmd(command, dstServerDTO);
		return result;
	}

	private String dstBack(DstServerDTO dstServerDTO) throws NemoAPIException {
		String contentBody = dstServerDTO.getParam();

		String screenName = "Master Server " + dstServerDTO.getCluster();
		String command = "screen -S \"" + screenName + "\" -p 0 -X stuff \"c_rollback(" + contentBody
				+ ")$(printf \\\\r)\"\r\n";
		String result = execCmd(command, dstServerDTO);
		return result;
	}

	private String dstList(DstServerDTO dstServerDTO) throws NemoAPIException {
		String contentBody = dstServerDTO.getParam();

		String path = ".klei/DoNotStarveTogether/" + dstServerDTO.getCluster() + "/Master";
		String screenName = "Master Server " + dstServerDTO.getCluster();
		String printList = "screen -S \"" + screenName
				+ "\" -p 0 -X stuff \"c_listplayers()$(printf \\\\r)\"\r\n sleep 1";
		String getList = "grep -A " + contentBody + " 'c_listplayers()" + "' " + path + "/server_log.txt"
				+ " | tail -n " + contentBody;

		String result = execCmd(printList + "&&" + getList, dstServerDTO);
		if (StringUtils.isNotBlank(result)) {
			int index = result.lastIndexOf("c_listplayers()");
			if (index != -1)
				result = result.substring(index + 16);
			if (result.length() > 0) {
				result = result.substring(1);

			} else
				return "没人在玩气球仔哦";
		} else
			return "没人在玩气球仔哦";

		return result;
	}
}
