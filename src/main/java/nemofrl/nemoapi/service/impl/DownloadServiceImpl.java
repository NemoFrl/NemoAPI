package nemofrl.nemoapi.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nemofrl.nemoapi.config.NemoAPIConfig;

public class DownloadServiceImpl implements Runnable {

	private static final Logger logger = LogManager.getLogger(DownloadServiceImpl.class);

	private NemoAPIConfig nemoAPIConfig;

	private String videoUrl;
	
	private String status="当前无下载任务 ";

	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String title;
	
	public DownloadServiceImpl(NemoAPIConfig nemoAPIConfig, String videoUrl) {
		this.videoUrl = videoUrl;
		this.nemoAPIConfig = nemoAPIConfig;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void run() {
		String command = "youtube-dl --proxy http://" + nemoAPIConfig.getProxyIp() + ":" + nemoAPIConfig.getProxyPort() + " "+videoUrl
				+ " -o \"" +title+".mp4\"";
		logger.info("exec command:" + command);
		String returnString = "";
		Process pro = null;
		Runtime runTime = Runtime.getRuntime();
		if (runTime == null) {
			logger.error("create runtime false!");
		}
		try {

			String[] commands = { "/bin/sh", "-c", command };
			pro = runTime.exec(commands, null, new File(nemoAPIConfig.getSaveVideoPath()));
			BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			BufferedReader errorinput = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
			PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));

			String line;
			while ((line = input.readLine()) != null) {
				returnString = returnString + line + "\n";
				logger.info(line);
				status=line;
			}
			while ((line = errorinput.readLine()) != null) {
				returnString = returnString + line + "\n";
				logger.info(line);
				status=line;
			}
			errorinput.close();
			input.close();
			output.close();
			pro.waitFor();
			pro.destroy();
		} catch (IOException | InterruptedException ex) {
			logger.error("download error", ex);
		}

	}

	public String getTitle() {
		String command = "youtube-dl --proxy http://" + nemoAPIConfig.getProxyIp() + ":" + nemoAPIConfig.getProxyPort()
				+ " -e " + videoUrl;
		logger.info("exec command:" + command);
		String returnString = "";
		Process pro = null;
		Runtime runTime = Runtime.getRuntime();
		if (runTime == null) {
			logger.error("create runtime false!");
		}
		try {
			pro = runTime.exec(command, null, new File(nemoAPIConfig.getSaveVideoPath()));
			BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			BufferedReader errorinput = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
			PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));

			String line;
			while ((line = input.readLine()) != null) {
				returnString = returnString + line;
				logger.info(line);
			}
			while ((line = errorinput.readLine()) != null) {
				returnString = returnString + line + "\n";
				logger.error(line);
				return null;
			}
			errorinput.close();
			input.close();
			output.close();
			pro.waitFor();
			pro.destroy();

		} catch (IOException | InterruptedException ex) {
			logger.error("download error", ex);
		}
		return returnString;
	}
}
