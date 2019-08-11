package nemofrl.nemoapi.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import nemofrl.nemoapi.exception.NemoAPIException;
import nemofrl.nemoapi.service.ShellService;


@Service("shellService")
public class ShellServiceImpl implements ShellService{
	
	private static final Logger logger = LogManager.getLogger(ShellServiceImpl.class);

	public String shServer(String command,String ip,String userName,String passwd,int port) throws NemoAPIException {

		logger.info("exc shell command:" + command);
		
		try {
			Connection conn=loginLinuxServer(ip, userName, passwd, port);
			Session session = conn.openSession();// 打开一个会话
			session.execCommand(command);
			String result = processStdout(session.getStdout(), "UTF-8");
			if(StringUtils.isBlank(result))
				result = processStdout(session.getStderr(), "UTF-8");
			if (StringUtils.isNotBlank(result))
				result = result.substring(0, result.length() - 1);
			session.close();
			conn.close();
			return result;
		} catch (IOException e) {
			throw new NemoAPIException("执行服务器命令失败",NemoAPIException.ERROR_SYSTEM, e);
		} 

	}
	public  Connection loginLinuxServer(String ip,String userName,String passwd,int port) throws  NemoAPIException {
		
		Connection conn = new Connection(ip,port);
		
		boolean flag=false;
		try {
			conn.connect();// 连接
			flag = conn.authenticateWithPassword(userName, passwd);// 认证
		} catch (Exception e) {
			throw new NemoAPIException("连接服务器失败",NemoAPIException.ERROR_NETCONNECT,e);
		}
		if (!flag) {
			throw new NemoAPIException("认证服务器失败",NemoAPIException.ERROR_CERTIFICATION);
		}
		return conn;
	}
	
	public String processStdout(InputStream in, String charset) throws NemoAPIException {
		InputStream stdout = new StreamGobbler(in);
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line + "\n");
			}
			br.close();
			stdout.close();
		} catch (UnsupportedEncodingException e) {
			throw new NemoAPIException("processStdout-UnsupportedEncodingException",NemoAPIException.ERROR_SYSTEM, e);
		} catch (IOException e) {
			throw new NemoAPIException("processStdout-IOException",NemoAPIException.ERROR_SYSTEM, e);
		}
		return buffer.toString();
	}
}
