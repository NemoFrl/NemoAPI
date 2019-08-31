package nemofrl.nemoapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NemoAPIConfig {
	@Value("${saveFilePath:}")
    private String saveFilePath;
	
	@Value("${saveVideoPath:}")
	private String saveVideoPath;
	
	@Value("${staticPath:}")
	private String staticPath;
	
	@Value("${proxyIp:}")
	private String proxyIp;
	
	@Value("${proxyPort:}")
	private int proxyPort;
	
	@Value("${openProxy:}")
	private boolean openProxy;
	
	@Value("${cookie:}")
	private String cookie;
	
	@Value("${dstToken:}")
	private String dstToken;
	
	@Value("${dstRssScheduleOpen:}")
	private boolean dstRssScheduleOpen;
	
	@Value("${dstServerScheduleOpen:}")
	private boolean dstServerScheduleOpen;
	
	@Value("${dstTokenScheduleOpen:}")
	private boolean dstTokenScheduleOpen;
	
	public String getSaveFilePath() {
		return saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isOpenProxy() {
		return openProxy;
	}

	public void setOpenProxy(boolean openProxy) {
		this.openProxy = openProxy;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getSaveVideoPath() {
		return saveVideoPath;
	}

	public void setSaveVideoPath(String saveVideoPath) {
		this.saveVideoPath = saveVideoPath;
	}

	public String getStaticPath() {
		return staticPath;
	}

	public void setStaticPath(String staticPath) {
		this.staticPath = staticPath;
	}

	public String getDstToken() {
		return dstToken;
	}

	public void setDstToken(String dstToken) {
		this.dstToken = dstToken;
	}

	public boolean isDstRssScheduleOpen() {
		return dstRssScheduleOpen;
	}

	public void setDstRssScheduleOpen(boolean dstRssScheduleOpen) {
		this.dstRssScheduleOpen = dstRssScheduleOpen;
	}

	public boolean isDstServerScheduleOpen() {
		return dstServerScheduleOpen;
	}

	public void setDstServerScheduleOpen(boolean dstServerScheduleOpen) {
		this.dstServerScheduleOpen = dstServerScheduleOpen;
	}

	public boolean isDstTokenScheduleOpen() {
		return dstTokenScheduleOpen;
	}

	public void setDstTokenScheduleOpen(boolean dstTokenScheduleOpen) {
		this.dstTokenScheduleOpen = dstTokenScheduleOpen;
	}

	
	
}
